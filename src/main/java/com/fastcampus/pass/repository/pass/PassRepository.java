package com.fastcampus.pass.repository.pass;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PassRepository extends JpaRepository<PassEntity, Integer> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE PassEntity p" +
            "          SET p.remainingCount = :remainingCount, " +
            "              p.modifiedAt = CURRENT_TIMESTAMP" +
            "        WHERE p.passSeq = :passSeq")
    int updateRemainingCount(Integer passSeq, Integer remainingCount);

    Optional<List<PassEntity>> findByUserId(String userId);
}
