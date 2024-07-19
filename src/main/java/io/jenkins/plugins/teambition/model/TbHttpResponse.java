package io.jenkins.plugins.teambition.model;

import lombok.Data;

@Data
public class TbHttpResponse {
    private int code;
    private Object data;
    private String message;
    private String type;
}

