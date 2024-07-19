package io.jenkins.plugins.teambition;


import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.Result;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.model.listeners.RunListener;
import io.jenkins.plugins.teambition.enums.RunOccasionEnum;
import io.jenkins.plugins.teambition.model.TbRestException;
import io.jenkins.plugins.teambition.model.WebhookContent;
import io.jenkins.plugins.teambition.resolver.DevOpsRunResolver;
import io.jenkins.plugins.teambition.service.TbRestService;


/**
 * 构建记录的触发
 *
 * @author qiuli
 */
@Extension
public class DevOpsRunListener extends RunListener<Run<?, ?>> {
    
    @Override
    public void onStarted(Run<?, ?> run, TaskListener listener) {
        this.send(run, listener, RunOccasionEnum.START);
    }
    
    @Override
    public void onCompleted(Run<?, ?> run, @NonNull TaskListener listener) {
        Result result = run.getResult();
        RunOccasionEnum runOccasion = RunOccasionEnum.getRunOccasion(result);
        this.send(run, listener, runOccasion);
    }
    
    private void send(Run<?, ?> run, TaskListener listener, RunOccasionEnum runOccasion) {
        
        TbLogger logger = new TbLogger(listener);
        logger.info(runOccasion.getDesc());
        
        DevOpsRunResolver devOpsRunResolver = new DevOpsRunResolver(run, listener);
        WebhookContent content = devOpsRunResolver.fromRunResolver(runOccasion);
        
        String jenkinsUrl = content.getJenkinsUrl();
        if (jenkinsUrl == null) {
            logger.error(Messages.DevOpsRunListener_JenkinsConfigUrlEmpty());
            return;
        }
        if (TbHelper.isNotBlank(jenkinsUrl) && !TbHelper.isURL(jenkinsUrl)) {
            logger.error(Messages.DevOpsRunListener_JenkinsConfigUrlError());
            return;
        }
        
        String tbEndpoint = content.getTeambitionEndpoint();
        if (tbEndpoint == null) {
            logger.error(Messages.DevOpsRunListener_OpenApiEndpointEmpty());
            return;
        }
        if (TbHelper.isNotBlank(tbEndpoint) && !TbHelper.isURL(tbEndpoint)) {
            logger.error(Messages.DevOpsRunListener_OpenApiEndpointError());
            return;
        }
        
        String tbOrgId = content.getTeambitionOrgId();
        if (tbOrgId == null) {
            logger.error(Messages.DevOpsRunListener_TeambitionOrgIdEmpty());
            return;
        }
        if (!TbHelper.isValidObjectId(tbOrgId)) {
            logger.error(Messages.DevOpsRunListener_TeambitionOrgIdError());
            return;
        }
        
        logger.info(TbHelper.prettyJSON(content));
        TbRestService service = new TbRestService();
        try {
            service.pushJobChanges(content);
        } catch (TbRestException e) {
            logger.error("Push webhook exception: " + e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        
    }
}
