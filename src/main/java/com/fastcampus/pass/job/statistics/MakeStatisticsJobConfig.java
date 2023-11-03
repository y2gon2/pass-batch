package com.fastcampus.pass.job.statistics;

import com.fastcampus.pass.repository.booking.BookingEntity;
import com.fastcampus.pass.repository.statistics.StatisticsEntity;
import com.fastcampus.pass.repository.statistics.StatisticsRepository;
import com.fastcampus.pass.util.LocalDateTimeUtils;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaCursorItemReader;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class MakeStatisticsJobConfig {
    private final int CHUNK_SIZE = 10;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final StatisticsRepository statisticsRepository;
    private final MakeDailyStatisticsTasklet makeDailyStatisticsTasklet;
    private final MakeWeeklyStatisticsTasklet makeWeeklyStatisticsTasklet;

    public MakeStatisticsJobConfig(
            JobBuilderFactory jobBuilderFactory,
            StepBuilderFactory stepBuilderFactory,
            EntityManagerFactory entityManagerFactory,
            StatisticsRepository statisticsRepository,
            MakeDailyStatisticsTasklet makeDailyStatisticsTasklet,
            MakeWeeklyStatisticsTasklet makeWeeklyStatisticsTasklet
    ) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.entityManagerFactory = entityManagerFactory;
        this.statisticsRepository = statisticsRepository;
        this.makeDailyStatisticsTasklet = makeDailyStatisticsTasklet;
        this.makeWeeklyStatisticsTasklet = makeWeeklyStatisticsTasklet;
    }

    @Bean
    public Job MakeStatisticsJob() {
        Flow addStatisticsFlow = new FlowBuilder<Flow>("addStatisticsFlow")
                .start(addStatisticsStep())
                .build();

        Flow makeDailyStatisticsFlow = new FlowBuilder<Flow>("makeDailyStatisticsFlow")
                .start(makeDailyStatisticsStep())
                .build();

        Flow makeWeeklyStatisticsFlow = new FlowBuilder<Flow>("makeWeeklyStatisticsFlow")
                .start(makeWeeklyStatisticsStep())
                .build();

        Flow parallelMakeStatisticsFlow = new FlowBuilder<Flow>("parallelMakeStatisticsFlow")
                .split(new SimpleAsyncTaskExecutor())
                .add(makeDailyStatisticsFlow, makeWeeklyStatisticsFlow)
                .build();

        return this.jobBuilderFactory.get("makeStatisticsJob")
                .start(addStatisticsFlow)
                .next(parallelMakeStatisticsFlow)
                .build()
                .build();
    }

    // -------- step 1. booking data 를 읽여와서 StatisticsEntity 생성 --------
    @Bean
    public Step addStatisticsStep() {
        return this.stepBuilderFactory.get("addStatisticsStep")
                .<BookingEntity, BookingEntity>chunk(CHUNK_SIZE)
                .reader(addStatisticsItemReader(null, null))
                .writer(addStatisticsItemWriter())
                .build();
    }

    // endedAt  기준 from - to booking records 를 read
    @Bean
    @StepScope // Job parameter 를 사용하려면 반드시 step 내에서 bean 생성. scope 을 명시해 주어야
    public JpaCursorItemReader<BookingEntity> addStatisticsItemReader(
            @Value("#{jobParameters[from]}") String fromString, // 아직 잘 모르겟지만.. ChatGPT 와의 문답 내용. document/StepScope Bean method parameter 을 runtime 에 제공할 수 있는 Value annotation 에 대해.mhtml
            @Value("#{jobParameters[to]}") String toString
            ) {
        final LocalDateTime from = LocalDateTimeUtils.parse(fromString);
        final LocalDateTime to = LocalDateTimeUtils.parse(toString);

        return new JpaCursorItemReaderBuilder<BookingEntity>()
                .name("addStatisticsItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("select b from BookingEntity b where b.endedAt between :form and :to")
                .parameterValues(Map.of("from", from, "to", to))
                .build();
    }

    // records 를 endedAt 기준으로 날짜 정보만 남기고 시간정보를 모두 제거한 statisticsAt 로 정리하고
    // 날짜 기준으로 mapping & sorting 하여 statistics table 에 저장.
    @Bean
    public ItemWriter<BookingEntity> addStatisticsItemWriter() {
        return bookingEntities -> {
            Map<LocalDateTime, StatisticsEntity> statisticsEntityMap = new LinkedHashMap<>();

            for(BookingEntity bookingEntity: bookingEntities) {
                final LocalDateTime statisticsAt = bookingEntity.getStatisticsAt();
                StatisticsEntity statisticsEntity = statisticsEntityMap.get(statisticsAt);

                if (statisticsEntity == null) { // HashMap 에 없으면, StatisticsEntity 최초 값을 생성하여 넣음.
                    statisticsEntityMap.put(statisticsAt, StatisticsEntity.create(bookingEntity));
                } else {
                    statisticsEntity.add(bookingEntity); // 이미 포함된 HashMap value 에 가산
                }
            }
            final List<StatisticsEntity> statisticsEntities = new ArrayList<>(statisticsEntityMap.values());
            statisticsRepository.saveAll(statisticsEntities);
        };
    }

    // -------- step2. 병렬 처리 step (일일 통계) --------
    @Bean
    public Step makeDailyStatisticsStep() {
        return this.stepBuilderFactory.get("makeDailyStatisticsStep")
                .tasklet(makeDailyStatisticsTasklet)
                .build();
    }

    // -------- step3. 병렬 처리 step (주간 통계) --------
    @Bean
    public Step makeWeeklyStatisticsStep() {
        return this.stepBuilderFactory.get("makeWeeklyStatisticsStep")
                .tasklet(makeWeeklyStatisticsTasklet)
                .build();
    }
}
