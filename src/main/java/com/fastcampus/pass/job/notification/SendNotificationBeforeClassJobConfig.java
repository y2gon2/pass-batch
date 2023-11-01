package com.fastcampus.pass.job.notification;

import com.fastcampus.pass.repository.booking.BookingEntity;
import com.fastcampus.pass.repository.booking.BookingStatus;
import com.fastcampus.pass.repository.notification.NotificationEntity;
import com.fastcampus.pass.repository.notification.NotificationEvent;
import com.fastcampus.pass.repository.notification.NotificationModelMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.batch.item.support.builder.SynchronizedItemStreamReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.Map;

// Step 2 개로 이루어진 Chunk 를 multi threading 처리하는 Batch 구현
public class SendNotificationBeforeClassJobConfig {
    private final int CHUNK_SIZE = 10;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final SendNotificationItemWriter sendNotificationItemWriter;

    public SendNotificationBeforeClassJobConfig(
            JobBuilderFactory jobBuilderFactory,
            StepBuilderFactory stepBuilderFactory,
            EntityManagerFactory entityManagerFactory,
            SendNotificationItemWriter sendNotificationItemWriter
    ) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.entityManagerFactory = entityManagerFactory;
        this.sendNotificationItemWriter = sendNotificationItemWriter;
    }

    @Bean
    public Job sendNotificationBeforeClassJob() {
        return this.jobBuilderFactory.get("sendNotificationBeforeClassJob")
                .start(addNotificationStep())
                .next(sendNotificationStep())
                .build();
    }

    //  ----------------- 1. addNotificationStep : Notification 생성 -----------------
    @Bean
    public Step addNotificationStep() {
        return this.stepBuilderFactory.get("addNotificationStep")
                .<BookingEntity, NotificationEntity>chunk(CHUNK_SIZE)
                .reader(addNotificationItemReader())
                .processor(addNotifcationItemProcessor())
                .writer(addNotificationITemWriter())
                .build();
    }

    /**
     * JpaPagingItemReader : JPA 에서 사용하는 페이징 기법
     * query 당 pageSize 만큼 가져오며 다른 PagingItemReader 와 마찬가지로 Thread-safe 함.
     */
    @Bean
    public JpaPagingItemReader<BookingEntity> addNotificationItemReader() {
        // status 가 READY & startedAt 이 10분 후 일 때, 예약이 알림 대상이 됨.
        return new JpaPagingItemReaderBuilder<BookingEntity>()
                .name("addNotificationItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(CHUNK_SIZE) // 한 번에 조회할 row 수
                .queryString("select b from BookingEntity b join fetch b.userEntity where b.status = :status and b.startedAt <= :startedAt order by b.bookingSeq")
                .parameterValues(Map.of("status", BookingStatus.READY, "startedAt", LocalDateTime.now().plusMinutes(10)))
                .build();
    }

    @Bean
    public ItemProcessor<BookingEntity, NotificationEntity> addNotifcationItemProcessor() {
        // 정의된 mapper 를 이용하여 Booking -> Notification 변환
        return bookingEntity -> NotificationModelMapper.INSTANCE.toNotificationEntity(bookingEntity, NotificationEvent.BEFORE_CLASS);
    }

    @Bean
    public JpaItemWriter<NotificationEntity> addNotificationITemWriter() {
        return new JpaItemWriterBuilder<NotificationEntity>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

    //  ----------------- 2. sendNotificationStep : 실제 alarm 을 보냄 -----------------
    @Bean
    public Step sendNotificationStep() {
        return this.stepBuilderFactory.get("sendNotificationStep")
                .<NotificationEntity, NotificationEntity>chunk(CHUNK_SIZE)
                .reader(sendNotificationItemReader())
                .writer(sendNotificationItemWriter)
                .taskExecutor(new SimpleAsyncTaskExecutor()) // 가장 간단한 multi thread TaskExecutor 선언
                .build();
    }

    /**
     * SynchronizedItemStreamReader: Multi-Thread 환경에서 reader 와 writer 는 thread-safe 해야함.
     * Cursor 기법의 ItemReader는 thread-safe 하지 않아 Paging 기법을 사용하거나 synchronized 를 선언해야 순차적으로 수행함.
     * 결과적으로 해당 reader 작업은 병렬처리되지 않으므로 multi threading 의 병렬 처리를 통한 작업 속도 향상은 반영되지 않는다.
     * 그러나, 앞에서 말한 바와 같이 reading 단계에서 thread safe 하지 않아 발생하는 문제를 방지하고,
     * 실제로 reader 보다 writer 가 훨씬 더 많은 작업 시간을 요구하므로, reader 만 순차적 처리하므로써 성능상 이점은 유지 시킬 수 있다.
     */
    @Bean
    public SynchronizedItemStreamReader<NotificationEntity> sendNotificationItemReader() {
        JpaCursorItemReader<NotificationEntity> itemReader = new JpaCursorItemReaderBuilder<NotificationEntity>()
                .name("sendNotificationItemReader")
                .entityManagerFactory(entityManagerFactory)
                // event 가 수업 전이며, 발송 여부(sent)가 미발송인 알람이 조회 대상
                .queryString("select n form NotificationEntity n where n.event = :event and n.sent = :sent")
                .parameterValues(Map.of("event", NotificationEvent.BEFORE_CLASS, "sent", false))
                .build();

        return new SynchronizedItemStreamReaderBuilder<NotificationEntity>()
                .delegate(itemReader)
                .build();
    }


}
