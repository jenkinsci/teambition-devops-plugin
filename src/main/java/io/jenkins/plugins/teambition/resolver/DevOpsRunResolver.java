package io.jenkins.plugins.teambition.resolver;

import hudson.EnvVars;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.scm.ChangeLogSet;
import io.jenkins.plugins.teambition.DevOpsGlobalConfig;
import io.jenkins.plugins.teambition.context.PipelineEnvContext;
import io.jenkins.plugins.teambition.enums.RunOccasionEnum;
import io.jenkins.plugins.teambition.model.WebhookContent;
import jenkins.scm.RunWithSCM;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DevOpsRunResolver {
    private final Run<?, ?> run;
    private final TaskListener listener;
    
    public DevOpsRunResolver(final Run<?, ?> run, final TaskListener listener) {
        this.run = run;
        this.listener = listener;
    }
    
    public EnvVars getEnvVars() {
        EnvVars jobEnvVars;
        try {
            jobEnvVars = this.run.getEnvironment(this.listener);
        } catch (Exception e) {
            jobEnvVars = new EnvVars();
            Thread.currentThread().interrupt();
        }
        try {
            EnvVars pipelineEnvVars = PipelineEnvContext.get();
            jobEnvVars.overrideAll(pipelineEnvVars);
        } catch (Exception e) {
            // log.error(e);
        }
        return jobEnvVars;
    }
    
    public String getJobName(EnvVars envVars) {
        if (envVars != null && envVars.get("JOB_NAME") != null) {
            return envVars.get("JOB_NAME");
        }
        String buildDisplayName = this.run.getFullDisplayName();
        if (buildDisplayName != null) {
            return buildDisplayName.replaceAll(" » ", "/").split(" #")[0];
        }
        return null;
    }
    
    public String getJobName() {
        EnvVars envVars = getEnvVars();
        return getJobName(envVars);
    }
    
    public String getJenkinsConfigUrl(EnvVars envVars) {
        String urlInGlobalConfig = DevOpsGlobalConfig.get().getJenkinsConfigUrl();
        if (urlInGlobalConfig != null) {
            return urlInGlobalConfig;
        }
        String defaultConfigUrl = DevOpsGlobalConfig.get().getDefaultJenkinsConfigUrl();
        if (defaultConfigUrl != null) {
            return defaultConfigUrl;
        }
        
        return envVars.get("JENKINS_URL");
    }
    
    public String getJenkinsConfigUrl() {
        EnvVars envVars = getEnvVars();
        return getJenkinsConfigUrl(envVars);
    }
    
    public WebhookContent fromRunResolver(RunOccasionEnum runOccasion) {
        EnvVars envVars = getEnvVars();
        WebhookContent entity = new WebhookContent();
        entity.setType("build");
        entity.setAction(runOccasion.name());
        entity.setJenkinsUrl(getJenkinsConfigUrl(envVars));
        entity.setTeambitionEndpoint(DevOpsGlobalConfig.get().getEndpoint());
        entity.setTeambitionOrgId(DevOpsGlobalConfig.get().getTeambitionOrgId());
        entity.setJobName(getJobName(envVars));
        entity.setBuildNumber((long) this.run.getNumber());
        if (this.run.isBuilding()) {
            entity.setBuildStatus("BUILDING");
        } else if (this.run.getResult() != null) {
            entity.setBuildStatus(this.run.getResult().toString());
        } else {
            entity.setBuildStatus("UNKNOWN");
        }
        entity.setQueueId(this.run.getQueueId());
        
        List<String> commitArr = new ArrayList<>();
        if (run instanceof RunWithSCM) {
            List<ChangeLogSet<? extends ChangeLogSet.Entry>> changeLogSets = ((RunWithSCM<?, ?>) run).getChangeSets();
            for (final ChangeLogSet<?> changeLogSet : changeLogSets) {
                for (Object item : changeLogSet.getItems()) {
                    commitArr.add(((ChangeLogSet.Entry) item).getMsg());
                }
            }
            if (commitArr.size() > 0) {
                entity.setCommitTitles(commitArr);
                JSONObject extraJson = new JSONObject();
                extraJson.put("gitUrl", getEnvVars().get("GIT_URL"));
                entity.setExtra(extraJson);
            }
        }
        if (entity.getCommitTitles() == null) {
            entity.setCommitTitles(new ArrayList<>());
        }
        return entity;
    }
}
