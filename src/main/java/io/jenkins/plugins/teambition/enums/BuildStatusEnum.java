package io.jenkins.plugins.teambition.enums;

import io.jenkins.plugins.teambition.Messages;
import lombok.Getter;

/**
 * 构建状态
 *
 * @author qiuli
 */
@Getter
public enum BuildStatusEnum {
    
    /**
     * 开始
     */
    START(Messages.BuildStatusType_start()),
    
    /**
     * 失败
     */
    FAILURE(Messages.BuildStatusType_failure()),
    
    /**
     * 成功
     */
    SUCCESS(Messages.BuildStatusType_success()),
    
    /**
     * 取消
     */
    ABORTED(Messages.BuildStatusType_aborted()),
    
    /**
     * 不稳定
     */
    UNSTABLE(Messages.BuildStatusType_unstable()),
    
    /**
     * 未构建
     */
    NOT_BUILT(Messages.BuildStatusType_not_built()),
    
    /**
     * 未知
     */
    UNKNOWN(Messages.BuildStatusType_unknown());
    
    
    private final String label;
    
    BuildStatusEnum(String label) {
        this.label = label;
    }
    
    public static BuildStatusEnum getBuildStatus(RunOccasionEnum runOccasion) {
        switch (runOccasion) {
            case START:
                return BuildStatusEnum.START;
            case SUCCESS:
                return BuildStatusEnum.SUCCESS;
            case FAILURE:
                return BuildStatusEnum.FAILURE;
            case ABORTED:
                return BuildStatusEnum.ABORTED;
            case UNSTABLE:
                return BuildStatusEnum.UNSTABLE;
            case NOT_BUILT:
                return BuildStatusEnum.NOT_BUILT;
            default:
                return BuildStatusEnum.UNKNOWN;
        }
    }
}
