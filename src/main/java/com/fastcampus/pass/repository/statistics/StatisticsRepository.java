package com.fastcampus.pass.repository.statistics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface StatisticsRepository extends JpaRepository<StatisticsEntity, Integer> {

    @Query(value = "SELECT new com.fastcampus.pass.repository.statistics.AggregatedStatistics(s.statisticsAt, SUM(s.allCount), SUM(s.attendedCount), SUM(s.cancelledCount)) " +
            "         FROM StatisticsEntity s " +
            "        WHERE s.statisticsAt BETWEEN :from AND :to " + // from - to 사의 범위의 records 만 선택
            "     GROUP BY s.statisticsAt")     // s.statisticsAt 필드 값이 동일한 레코드들을 그룹화
    List<AggregatedStatistics> findByStatisticsAtBetweenAndGroupBy(@Param("from")LocalDateTime from, @Param("to") LocalDateTime to);
}
