package io.ssafy.luckyweeky.schedule.infrastructure.repository;

import io.ssafy.luckyweeky.common.infrastructure.persistence.MyBatisSqlSessionFactory;
import io.ssafy.luckyweeky.schedule.domain.model.ScheduleEntity;
import io.ssafy.luckyweeky.schedule.domain.repository.MainScheduleMapper;
import io.ssafy.luckyweeky.schedule.domain.repository.SubScheduleMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class ScheduleRepository {
    private final SqlSessionFactory sqlSessionFactory;

    public ScheduleRepository() {
        this.sqlSessionFactory = MyBatisSqlSessionFactory.getSqlSessionFactory();
    }

    public boolean insertSchedule(ScheduleEntity scheduleEntity) {
        try (SqlSession session = sqlSessionFactory.openSession(false)) {
            MainScheduleMapper mainScheduleMapper = session.getMapper(MainScheduleMapper.class);
            SubScheduleMapper subScheduleMapper = session.getMapper(SubScheduleMapper.class);
            mainScheduleMapper.insertMainSchedule(scheduleEntity.getMainScheduleEntity());
            scheduleEntity.getSubScheduleEntityList().forEach(subScheduleEntity -> {
                subScheduleMapper.insertSubSchedule(subScheduleEntity);
            });
            session.commit();
            return true;
        }catch (RuntimeException e){
            return false;
        }
    }
}
