package com.fastcampus.pass.repository.statistics;


import com.fastcampus.pass.repository.booking.BookingEntity;
import com.fastcampus.pass.repository.booking.BookingStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name = "statistics")
public class StatisticsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer statisticsSeq;

    @Column(nullable = false)
    private LocalDateTime statisticsAt;

    @Column(nullable = false)
    private Integer allCount;

    @Column(nullable = false)
    private Integer attendedCount;

    @Column(nullable = false)
    private Integer cancelledCount;

    public static StatisticsEntity create(final BookingEntity bookingEntity) {
        StatisticsEntity statisticsEntity = new StatisticsEntity();
        statisticsEntity.setStatisticsAt(bookingEntity.getStatisticsAt());
        statisticsEntity.setAllCount(1);

        if (bookingEntity.isAttended()) {
            statisticsEntity.setAttendedCount(1);
            statisticsEntity.setCancelledCount(0);
        }

        if (BookingStatus.CANCELLED.equals(bookingEntity.getStatus())) {
            statisticsEntity.setAttendedCount(0);
            statisticsEntity.setCancelledCount(1);
        }

        return statisticsEntity;
    }

    public void add(final BookingEntity bookingEntity) {
        this.allCount++;

        if (bookingEntity.isAttended()) {
            this.attendedCount++;
        }

        if (BookingStatus.CANCELLED.equals(bookingEntity.getStatus())) {
            this.cancelledCount++;
        }
    }

}
