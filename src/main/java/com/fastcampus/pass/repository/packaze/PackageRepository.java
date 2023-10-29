package com.fastcampus.pass.repository.packaze;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface PackageRepository extends JpaRepository<PackageEntity, Integer> {
    List<PackageEntity> findByCreatedAtAfter(LocalDateTime dateTime, Pageable pageable);

    // 적용될 Query 문을 직접 구현
    // 반환되는 int 값은 해당 method 작업에 의해 값의 수정이 발생한 record (행) 의 갯수를 의미
    @Transactional
    @Modifying  // 추가, 수정, 삭제 요청에 필요
    @Query( value = "UPDATE PackageEntity p " +
            "           SET p.count = :count," +
            "               p.period = :period" +
            "         WHERE p.packageSeq = :packageSeq"
    )
    int updateCountAndPeriod(Integer packageSeq, Integer count, Integer period);
}
