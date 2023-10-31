package com.fastcampus.pass.job.pass;

import com.fastcampus.pass.repository.pass.*;
import com.fastcampus.pass.repository.user.UserGroupMappingEntity;
import com.fastcampus.pass.repository.user.UserGroupMappingRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class AddPassesTaskletTest {
    // @InjectMocks 클래스의 인스턴스를 생성하고 @Mock 으로 생성된 객체를 주입
    @InjectMocks private AddPassesTasklet addPassesTasklet;

    // AddPassesTasklet fields
    @Mock private PassRepository passRepository;
    @Mock private BulkPassRepository bulkPassRepository;
    @Mock private UserGroupMappingRepository userGroupMappingRepository;

    // AddPassesTasklet 이 상속받은 Tasklet 의 fields
    @Mock private StepContribution stepContribution;
    @Mock private ChunkContext chunkContext;


    @Test
    public void test_execute() throws Exception {
        // Given
        final String userGroupId = "GROUP";
        final String userId = "A1000000";
        final Integer packageSeq = 1;
        final Integer count = 10;
        final LocalDateTime now = LocalDateTime.now();

        final BulkPassEntity bulkPassEntity = BulkPassEntity.of(
                packageSeq,
                userGroupId,
                BulkPassStatus.READY,
                count,
                now,
                now.plusDays(60)
        );

        final UserGroupMappingEntity userGroupMappingEntity = new UserGroupMappingEntity();
        userGroupMappingEntity.setUserGroupId(userGroupId);
        userGroupMappingEntity.setUserId(userId);

        // When
        // addPassesTasklet 내부에서 동작하는 BulkPassRepository.findByStatusAndStartedAtGreaterThan(),
        // userGroupMappingRepository.findByUserGroupId 에 대한 반환값 명시
        when(bulkPassRepository.findByStatusAndStartedAtGreaterThan(eq(BulkPassStatus.READY), any()))
                .thenReturn(List.of(bulkPassEntity));
        when(userGroupMappingRepository.findByUserGroupId(eq("GROUP")))
                .thenReturn(List.of(userGroupMappingEntity));

        RepeatStatus repeatStatus = addPassesTasklet.execute(stepContribution, chunkContext);

        // Then
        // execute의 return 값인 RepeatStatus 값을 확인
        assertEquals(RepeatStatus.FINISHED, repeatStatus);

        // 해당 test 는 Mock 을 사용하므로 실제 data 가 DB 에 반영되지는 않는다.
        // 그렇다면, test 에서 최소한 tasklet 을 통해서 관련 entity 가 생성되고,
        // 그 repository method call 이 발생했는지까지 확인해야 한다.
        // 그런데 해당 과정은 tasklet execute method 내부에서 작업이 진행되므로
        // 관련 작업을 중간에 캡쳐해야 되는 상황이다.
        //
        // 추가된 PassEntity 값을 확인
        // ArgumentCaptor : mock method 가 어떤 인자로 호출되었는지를 검사하고 할때 사용

        // captor 될 인자 type 이 List 임을 나타냄
        ArgumentCaptor<List> passEntitiesCaptor = ArgumentCaptor.forClass(List.class);
        // passRepository라는 mock 객체의 saveAll 메서드가 정확히 한 번 호출되었는지 검증
        // passEntitiesCaptor.capture()를 인자로 사용하면,
        // 실제로 saveAll 메서드가 호출될 때 전달된 인자를 passEntitiesCaptor가 캡쳐
        verify(passRepository, times(1)).saveAll(passEntitiesCaptor.capture());
        //  passEntitiesCaptor에서 캡쳐된 값을 저장
        final List<PassEntity> passEntities = passEntitiesCaptor.getValue();

        assertEquals(1, passEntities.size());

        final PassEntity passEntity = passEntities.get(0);

        assertEquals(packageSeq, passEntity.getPackageSeq());
        assertEquals(userId, passEntity.getUserId());
        assertEquals(PassStatus.READY, passEntity.getStatus());
        assertEquals(count, passEntity.getRemainingCount());
    }
}

