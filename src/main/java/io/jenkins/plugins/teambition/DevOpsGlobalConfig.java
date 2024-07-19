package io.jenkins.plugins.teambition;

import hudson.Extension;
import hudson.Util;
import hudson.util.FormValidation;
import io.jenkins.plugins.teambition.model.TbRestException;
import io.jenkins.plugins.teambition.service.TbRestService;
import jenkins.model.GlobalConfiguration;
import jenkins.model.Jenkins;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.jenkinsci.Symbol;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.DoNotUse;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.verb.POST;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * 全局配置
 *
 * @author qiuli
 */
@Extension
@Symbol("teambitionDevOps")
public class DevOpsGlobalConfig extends GlobalConfiguration {
    public static final String DEFAULT_ENDPOINT = "https://guiyi.teambition.com";
    
    
    private String endpoint;
    private String teambitionOrgId;
    private String jenkinsConfigUrl;
    
    public DevOpsGlobalConfig() {
        load();
    }
    
    @Nonnull
    public static DevOpsGlobalConfig get() {
        return (DevOpsGlobalConfig) Jenkins.get().getDescriptorOrDie(DevOpsGlobalConfig.class);
    }
    
    public String getDefaultEndpoint() {
        return DevOpsGlobalConfig.DEFAULT_ENDPOINT;
    }
    
    public String getEndpoint() {
        if (endpoint == null) {
            return getDefaultEndpoint();
        }
        return endpoint;
    }
    
    @DataBoundSetter
    public void setEndpoint(String endpoint) {
        if (endpoint == null) {
            this.endpoint = getDefaultEndpoint();
        } else {
            this.endpoint = Util.fixEmptyAndTrim(endpoint);
        }
    }
    
    public String getTeambitionOrgId() {
        return teambitionOrgId;
    }
    
    @DataBoundSetter
    public void setTeambitionOrgId(String teambitionOrgId) {
        this.teambitionOrgId = Util.fixEmptyAndTrim(teambitionOrgId);
    }
    
    public String getDefaultJenkinsConfigUrl() {
        return Jenkins.get().getRootUrl();
    }
    
    public String getJenkinsConfigUrl() {
        if (jenkinsConfigUrl == null) {
            return getDefaultJenkinsConfigUrl();
        }
        return jenkinsConfigUrl;
    }
    
    @DataBoundSetter
    public void setJenkinsConfigUrl(String jenkinsConfigUrl) {
        if (jenkinsConfigUrl == null) {
            this.jenkinsConfigUrl = getDefaultJenkinsConfigUrl();
        } else {
            this.jenkinsConfigUrl = Util.fixEmptyAndTrim(jenkinsConfigUrl);
        }
        
    }
    
    @Override
    public boolean configure(StaplerRequest req, JSONObject formatData) throws FormException {
        try {
            req.bindJSON(this, formatData);
        } catch (Exception e) {
            throw new FormException(e.getMessage(), e, Messages.DevOpsGlobalConfig_GlobalConfigError());
        }
        save();
        return true;
    }
    
    @POST
    @Restricted(DoNotUse.class)
    @SuppressWarnings("unused")
    public FormValidation doTestConnection(@QueryParameter(value = "endpoint", fixEmpty = true) String endpoint,
                                           @QueryParameter(value = "teambitionOrgId", fixEmpty = true) String teambitionOrgId,
                                           @QueryParameter(value = "jenkinsConfigUrl", fixEmpty = true) String jenkinsConfigUrl) throws IOException {
        
        // Check permission, only ADMINISTER
        Jenkins.get().checkPermission(Jenkins.ADMINISTER);
        
        if (StringUtils.isEmpty(endpoint)) {
            return FormValidation.error(Messages.DevOpsGlobalConfig_OpenApiEndpointEmpty());
        }
        if (TbHelper.isNotBlank(endpoint) && !TbHelper.isURL(endpoint)) {
            return FormValidation.error(Messages.DevOpsGlobalConfig_OpenApiEndpointError());
        }
        if (StringUtils.isEmpty(teambitionOrgId)) {
            return FormValidation.error(Messages.DevOpsGlobalConfig_TeambitionOrgIdEmpty());
        }
        if (!TbHelper.isValidObjectId(teambitionOrgId)) {
            return FormValidation.error(Messages.DevOpsGlobalConfig_TeambitionOrgIdError());
        }
        
        if (StringUtils.isEmpty(jenkinsConfigUrl)) {
            return FormValidation.error(Messages.DevOpsGlobalConfig_JenkinsConfigUrlEmpty());
        }
        if (TbHelper.isNotBlank(jenkinsConfigUrl) && !TbHelper.isURL(jenkinsConfigUrl)) {
            return FormValidation.error(Messages.DevOpsGlobalConfig_JenkinsConfigUrlError());
        }
        
        TbRestService service = new TbRestService();
        
        try {
            service.doConnectTest(endpoint, teambitionOrgId, jenkinsConfigUrl);
            return FormValidation.ok(Messages.DevOpsGlobalConfig_DoTestConnectionSuccessfully());
        } catch (TbRestException e) {
            return FormValidation.error(e.getMessage());
        } catch (Exception e) {
            return FormValidation.error(Messages.DevOpsGlobalConfig_DoTestConnectionFailure() + ": " + e.getMessage());
        }
    }
    
}
