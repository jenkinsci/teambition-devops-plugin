package io.jenkins.plugins.teambition.service;


import io.jenkins.plugins.teambition.TbHelper;
import io.jenkins.plugins.teambition.client.RestApiClient;
import io.jenkins.plugins.teambition.model.TbHttpResponse;
import io.jenkins.plugins.teambition.model.WebhookContent;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.client.HttpClientErrorException;

import java.util.logging.Logger;

public class TbRestApiService {
    private final Logger log = Logger.getLogger(TbRestApiService.class.getName());
    private final String baseURL;
    private final RestApiClient restApiClient;
    
    public TbRestApiService(String endpoint) {
        this.baseURL = endpoint;
        this.restApiClient = new RestApiClient();
    }
    
    public TbHttpResponse doPostRequest(String url, String jsonStr) throws Exception {
        try {
            HttpResponse res = this.restApiClient.doPostRequest(url, jsonStr);
            log.info("doPostRequest result: " + res);
            int statusCode = res.getStatusLine().getStatusCode();
            HttpEntity httpEntity = res.getEntity();
            String resBody = EntityUtils.toString(httpEntity);
            try {
                JSONObject bodyJson = new JSONObject(resBody);
                return new TbHttpResponse() {{
                    setCode(bodyJson.getInt("code"));
                    setData(bodyJson.get("data"));
                    setMessage(bodyJson.getString("message"));
                    setType(bodyJson.getString("type"));
                }};
            } catch (JSONException e) {
                return new TbHttpResponse() {{
                    setCode(statusCode);
                    setData(resBody);
                    setMessage("TbHttpResponse");
                    setType("TbHttpResponse");
                }};
            }
            
        } catch (HttpClientErrorException e) {
            log.warning("doPostRequest HttpClientErrorException: " + e);
            int statusCode = e.getRawStatusCode();
            String resBody = e.getResponseBodyAsString();
            try {
                return new TbHttpResponse() {{
                    setCode(statusCode);
                    setData(new JSONObject(resBody));
                    setMessage("HttpClientErrorException");
                    setType("HttpClientErrorException");
                }};
            } catch (JSONException err) {
                return new TbHttpResponse() {{
                    setCode(statusCode);
                    setData(resBody);
                    setMessage("HttpClientErrorException");
                    setType("HttpClientErrorException");
                }};
            }
        }
    }
    
    public TbHttpResponse sendWebhookContent(WebhookContent content) throws Exception {
        String path = this.baseURL + "/api/callback/release/pipeline/webhook";
        return doPostRequest(path, TbHelper.prettyJSON(content));
    }
}
