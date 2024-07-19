package io.jenkins.plugins.teambition.model;

import lombok.Data;
import org.json.JSONObject;

import java.util.List;

@Data
public class WebhookContent {
    private final String provider = "jenkins";
    
    private String type;
    private String action;
    private String jenkinsUrl;
    private String teambitionEndpoint;
    private String teambitionOrgId;
    
    private String jobName;
    private String oldJobName;
    
    private Long buildNumber;
    private String buildStatus;
    private Long queueId;
    private List<String> commitTitles;
    
    private JSONObject lastBuild;
    private JSONObject extra;
    
    public static WebhookContent fromTestConnection(String endpoint, String teambitionOrgId, String jenkinsConfigUrl) {
        WebhookContent entity = new WebhookContent();
        entity.setType("testConnect");
        entity.setAction("testConnect");
        entity.setJenkinsUrl(jenkinsConfigUrl);
        entity.setTeambitionEndpoint(endpoint);
        entity.setTeambitionOrgId(teambitionOrgId);
        return entity;
    }
}
