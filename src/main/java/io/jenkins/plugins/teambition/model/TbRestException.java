package io.jenkins.plugins.teambition.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author qiuli
 */
public class TbRestException extends Exception {
    
    public static final long serialVersionUID = 123456789L;
    @Getter
    @Setter
    private String code;
    
    @Getter
    @Setter
    private String type;
    
    @Getter
    @Setter
    private String message;
    
    public TbRestException(String code, String type, String message) {
        super(message);
        setCode(code);
        setType(type);
        setMessage(message);
    }
    
}
