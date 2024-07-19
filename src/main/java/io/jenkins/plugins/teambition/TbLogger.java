package io.jenkins.plugins.teambition;


import hudson.model.TaskListener;

import java.io.Serializable;

public class TbLogger implements Serializable {
    private static String LOG_PREFIX = "[Teambition-DevOps] - ";
    private static final long serialVersionUID = 1L;
    
    private final TaskListener listener;
    
    public TbLogger(TaskListener listener) {
        this.listener = listener;
    }
    
    public void info(String message) {
        this.listener.getLogger().println(LOG_PREFIX + "[INFO] " + message);
    }
    
    public void error(String message) {
        this.listener.getLogger().println(LOG_PREFIX + "[ERROR] " + message);
    }
}
