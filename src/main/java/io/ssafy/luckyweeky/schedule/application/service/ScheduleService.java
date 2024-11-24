package io.ssafy.luckyweeky.schedule.application.service;

import io.ssafy.luckyweeky.common.config.XmlBeanFactory;
import io.ssafy.luckyweeky.schedule.application.converter.ScheduleDtoToScheduleEntity;
import io.ssafy.luckyweeky.schedule.application.converter.ScheduleEntityToScheduleDto;
import io.ssafy.luckyweeky.schedule.application.dto.ScheduleDto;
import io.ssafy.luckyweeky.schedule.domain.model.ScheduleEntity;
import io.ssafy.luckyweeky.schedule.infrastructure.repository.ScheduleRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public ScheduleService() {
        this.scheduleRepository = (ScheduleRepository) XmlBeanFactory.getBean("scheduleRepository");
    }

    public boolean addSchedule(ScheduleDto scheduleDto) {
        ScheduleEntity scheduleEntity = ScheduleDtoToScheduleEntity.getInstance().convert(scheduleDto);
        return scheduleRepository.insertSchedule(scheduleEntity);
    }

    public List<ScheduleDto> getSchedulesByDateRange(Map<String,Object> params) {
        List<ScheduleEntity> scheduleEntities = scheduleRepository.getSchedulesByDateRange(params);
        List<ScheduleDto> scheduleDtos = new ArrayList<>();
        scheduleEntities.forEach(scheduleEntity -> {
           scheduleDtos.add(ScheduleEntityToScheduleDto.getInstance().convert(scheduleEntity));
        });
        return scheduleDtos;
    }



    public boolean deleteSubSchedule(String subScheduleTitle) {
        return scheduleRepository.deleteSubSchedule(subScheduleTitle);
    }

}
