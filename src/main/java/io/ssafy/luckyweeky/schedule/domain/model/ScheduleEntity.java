package io.ssafy.luckyweeky.schedule.domain.model;

import java.util.List;

public class ScheduleEntity {
    private final MainScheduleEntity mainScheduleEntity;
    private final List<SubScheduleEntity> subScheduleEntityList;

    public ScheduleEntity(final MainScheduleEntity mainScheduleEntity, final List<SubScheduleEntity> subScheduleEntityList) {
        this.mainScheduleEntity = mainScheduleEntity;
        this.subScheduleEntityList = subScheduleEntityList;
    }

    public MainScheduleEntity getMainScheduleEntity() {
        return mainScheduleEntity;
    }

    public List<SubScheduleEntity> getSubScheduleEntityList() {
        return subScheduleEntityList;
    }


}
