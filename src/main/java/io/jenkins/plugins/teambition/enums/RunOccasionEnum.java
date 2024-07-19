package io.jenkins.plugins.teambition.enums;

import hudson.model.Result;
import io.jenkins.plugins.teambition.Messages;
import lombok.Getter;

/**
 * @author qiuli
 */
public enum RunOccasionEnum {
    /**
     * 在启动构建时通知
     */
    START(Messages.RunOccasion_start()),
    
    /**
     * 构建中断时通知
     */
    ABORTED(Messages.RunOccasion_aborted()),
    
    /**
     * 构建失败时通知
     */
    FAILURE(Messages.RunOccasion_failure()),
    
    /**
     * 构建成功时通知
     */
    SUCCESS(Messages.RunOccasion_success()),
    
    /**
     * 构建不稳定时通知
     */
    UNSTABLE(Messages.RunOccasion_unstable()),
    
    /**
     * 在未构建时通知
     */
    NOT_BUILT(Messages.RunOccasion_not_built()),
    
    /**
     * 删除时通知 - 待废弃
     */
    DELETED(Messages.RunOccasion_deleted());
    
    @Getter
    private final String desc;
    
    RunOccasionEnum(String desc) {
        this.desc = desc;
    }
    
    
    public static RunOccasionEnum getRunOccasion(Result result) {
        if (Result.SUCCESS.equals(result)) {
            return RunOccasionEnum.SUCCESS;
        }
        if (Result.FAILURE.equals(result)) {
            return RunOccasionEnum.FAILURE;
        }
        if (Result.ABORTED.equals(result)) {
            return RunOccasionEnum.ABORTED;
        }
        if (Result.UNSTABLE.equals(result)) {
            return RunOccasionEnum.UNSTABLE;
        }
        if (Result.NOT_BUILT.equals(result)) {
            return RunOccasionEnum.NOT_BUILT;
        }
        return null;
    }
}
