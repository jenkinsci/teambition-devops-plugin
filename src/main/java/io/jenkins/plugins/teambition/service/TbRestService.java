package io.jenkins.plugins.teambition.service;

import io.jenkins.plugins.teambition.model.TbHttpResponse;
import io.jenkins.plugins.teambition.model.TbRestException;
import io.jenkins.plugins.teambition.model.WebhookContent;

/**
 * @author qiuli
 */
public class TbRestService {
    
    private TbRestApiService getTbRestService(String endpoint) {
        return new TbRestApiService(endpoint);
    }
    
    public void doConnectTest(String endpoint, String teambitionOrgId, String jenkinsConfigUrl) throws Exception {
        WebhookContent content = WebhookContent.fromTestConnection(endpoint, teambitionOrgId, jenkinsConfigUrl);
        TbHttpResponse res = this.getTbRestService(endpoint).sendWebhookContent(content);
        if (res.getCode() != 200) {
            throw new TbRestException(String.valueOf(res.getCode()), res.getType(), res.getMessage());
        }
    }
    
    public void pushJobChanges(WebhookContent content) throws Exception {
        TbHttpResponse res = this.getTbRestService(content.getTeambitionEndpoint()).sendWebhookContent(content);
        if (res.getCode() != 200) {
            throw new TbRestException(String.valueOf(res.getCode()), res.getType(), res.getMessage());
        }
    }
}
