package io.ssafy.luckyweeky.schedule.application.service;

import io.ssafy.luckyweeky.common.config.XmlBeanFactory;
import io.ssafy.luckyweeky.schedule.application.converter.ScheduleDtoToScheduleEntity;
import io.ssafy.luckyweeky.schedule.application.dto.ScheduleDto;
import io.ssafy.luckyweeky.schedule.domain.model.ScheduleEntity;
import io.ssafy.luckyweeky.schedule.infrastructure.repository.ScheduleRepository;

public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public ScheduleService() {
        this.scheduleRepository = (ScheduleRepository) XmlBeanFactory.getBean("scheduleRepository");
    }

    public boolean addSchedule(ScheduleDto scheduleDto) {
        ScheduleEntity scheduleEntity = ScheduleDtoToScheduleEntity.getInstance().convert(scheduleDto);
        return scheduleRepository.insertSchedule(scheduleEntity);
    }
}
