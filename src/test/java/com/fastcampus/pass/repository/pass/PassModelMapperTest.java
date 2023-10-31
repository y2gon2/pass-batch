package com.fastcampus.pass.repository.pass;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PassModelMapperTest {

    @Test
    public void test_toPassEntity() {

        // Given
        final LocalDateTime now = LocalDateTime.now();
        final String userId = "A1000000";

        BulkPassEntity bulkPassEntity = BulkPassEntity.of(
                1,
                "GROUP",
                BulkPassStatus.COMPLETED,
                10,
                now.minusDays(60),
                now
        );

        // When
        final PassEntity passEntity = PassModelMapper.INSTANCE
                .toPassEntity(bulkPassEntity, userId);

        // Then
        assertEquals(1, passEntity.getPackageSeq());
        assertEquals(PassStatus.READY, passEntity.getStatus());
        assertEquals(10, passEntity.getRemainingCount());
        assertEquals(now.minusDays(60), passEntity.getStartedAt());
        assertEquals(now, passEntity.getEndedAt());
        assertEquals(userId, passEntity.getUserId() );
    }

}