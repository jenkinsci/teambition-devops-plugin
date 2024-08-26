package io.jenkins.plugins.teambition.resolver;

import hudson.model.Job;
import hudson.model.Result;
import hudson.model.Run;
import io.jenkins.plugins.teambition.DevOpsGlobalConfig;
import io.jenkins.plugins.teambition.enums.JobOccasionEnum;
import io.jenkins.plugins.teambition.model.WebhookContent;
import org.json.JSONObject;

public class DevOpsJobResolver {
    private final Job job;
    private final String newName;
    private final String oldName;
    
    public DevOpsJobResolver(Job job) {
        this.job = job;
        this.newName = job.getFullName();
        this.oldName = null;
    }
    
    public DevOpsJobResolver(Job job, String oldName, String newName) {
        this.job = job;
        this.newName = newName;
        this.oldName = oldName;
    }
    
    public String getJenkinsConfigUrl() {
        String urlInGlobalConfig = DevOpsGlobalConfig.get().getJenkinsConfigUrl();
        if (urlInGlobalConfig != null) {
            return urlInGlobalConfig;
        }
        String defaultConfigUrl = DevOpsGlobalConfig.get().getDefaultJenkinsConfigUrl();
        if (defaultConfigUrl != null) {
            return defaultConfigUrl;
        }
        // envVars ？？
        return null;
    }
    
    public WebhookContent fromJobResolver(JobOccasionEnum jobOccasion) {
        WebhookContent entity = new WebhookContent();
        entity.setType("job");
        entity.setAction(jobOccasion.name());
        entity.setJenkinsUrl(getJenkinsConfigUrl());
        entity.setTeambitionEndpoint(DevOpsGlobalConfig.get().getEndpoint());
        entity.setTeambitionOrgId(DevOpsGlobalConfig.get().getTeambitionOrgId());
        entity.setJobName(this.newName);
        entity.setOldJobName(this.oldName);
        if (this.job.isBuilding()) {
            Run lastBuild = this.job.getLastBuild();
            
            if (lastBuild != null) {
                JSONObject buildObj = new JSONObject();
                buildObj.put("id", lastBuild.getId());
                buildObj.put("number", lastBuild.getNumber());
                buildObj.put("building", lastBuild.isBuilding());
                Result lastBuildResult = lastBuild.getResult();
                if (lastBuild.isBuilding()) {
                    buildObj.put("result", "BUILDING");
                } else if (lastBuildResult != null) {
                    buildObj.put("result", lastBuildResult.toString());
                }
                buildObj.put("queueId", lastBuild.getQueueId());
                entity.setLastBuild(buildObj);
            }
        }
        if (this.job.getQueueItem() != null) {
            entity.setQueueId(this.job.getQueueItem().getId());
        }
        return entity;
    }
}
