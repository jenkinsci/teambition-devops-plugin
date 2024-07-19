package io.jenkins.plugins.teambition;

import hudson.Extension;
import hudson.model.Item;
import hudson.model.Job;
import hudson.model.listeners.ItemListener;
import io.jenkins.plugins.teambition.enums.JobOccasionEnum;
import io.jenkins.plugins.teambition.model.WebhookContent;
import io.jenkins.plugins.teambition.resolver.DevOpsJobResolver;
import io.jenkins.plugins.teambition.service.TbRestService;

import java.util.logging.Logger;


/**
 * job的更新触发
 *
 * @author qiuli
 */
@Extension
public class DevOpsJobListener extends ItemListener {
    public static final Logger logger = Logger.getLogger(DevOpsJobListener.class.getName());
    
    @Override
    public void onRenamed(Item item, String oldName, String newName) {
        // covered by onLocationChanged
    }
    
    @Override
    public void onDeleted(Item item) {
        if (item instanceof Job) {
            DevOpsJobResolver jobResolver = new DevOpsJobResolver((Job) item);
            WebhookContent content = jobResolver.fromJobResolver(JobOccasionEnum.DELETED);
            send((Job) item, content);
        }
    }
    
    @Override
    public void onLocationChanged(Item item, String oldFullName, String newFullName) {
        if (item instanceof Job) {
            DevOpsJobResolver jobResolver = new DevOpsJobResolver((Job) item, oldFullName, newFullName);
            WebhookContent content = jobResolver.fromJobResolver(JobOccasionEnum.MOVED);
            send((Job) item, content);
        }
    }
    
    private void send(Job job, WebhookContent content) {
        String jenkinsUrl = content.getJenkinsUrl();
        if (jenkinsUrl == null) {
            return;
        }
        if (TbHelper.isNotBlank(jenkinsUrl) && !TbHelper.isURL(jenkinsUrl)) {
            return;
        }
        
        String tbEndpoint = content.getTeambitionEndpoint();
        if (tbEndpoint == null) {
            return;
        }
        if (TbHelper.isNotBlank(tbEndpoint) && !TbHelper.isURL(tbEndpoint)) {
            return;
        }
        
        String tbOrgId = content.getTeambitionOrgId();
        if (tbOrgId == null) {
            return;
        }
        if (!TbHelper.isValidObjectId(tbOrgId)) {
            return;
        }
        
        logger.info(TbHelper.prettyJSON(content));
        TbRestService service = new TbRestService();
        try {
            service.pushJobChanges(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}
