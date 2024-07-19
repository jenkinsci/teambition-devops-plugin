package io.jenkins.plugins.teambition.enums;


import io.jenkins.plugins.teambition.Messages;
import lombok.Getter;

public enum JobOccasionEnum {
    // /**
    //  * job重命名时（移动可覆盖重命名场景）
    //  */
    // RENAME(Messages.JobOccasion_rename()),
    
    /**
     * job被删除时
     */
    DELETED(Messages.JobOccasion_rename()),
    
    /**
     * job被移动时
     */
    MOVED(Messages.JobOccasion_rename());
    
    @Getter
    private final String desc;
    
    JobOccasionEnum(String desc) {
        this.desc = desc;
    }
    
}
