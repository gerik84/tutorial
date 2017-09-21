package models;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

/**
 * Created by pavel on 21.09.17.
 */
@MappedSuperclass
abstract public class ModerationModel extends BaseModel {

    public enum Status {
        APPROVED,
        BLOCKED,
        PENDING,
        DELETED
    }

    @Enumerated(EnumType.STRING)
    private Status status = Status.APPROVED;

    @Column(length = 1000)
    private String blockingReason;

    public boolean needSwitchToPending() {
        return true;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getBlockingReason() {
        return blockingReason;
    }

    public void setBlockingReason(String blockingReason) {
        this.blockingReason = blockingReason;
    }

}
