package com.fastcampus.pass.repository.notification;

import com.fastcampus.pass.repository.BaseEntity;
import jdk.jfr.DataAmount;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Entity
@Table(name = "notification")
public class NotificationEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer notificationSeq;

    @Column(nullable = false)
    private String uuid;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationEvent event;

    @Column(nullable = false)
    private String text;

    private boolean sent;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime sentAt;

    protected NotificationEntity() {}

    public NotificationEntity(
            String uuid,
            NotificationEvent event,
            String text,
            boolean sent,
            LocalDateTime sentAt
    ) {
        this.uuid = uuid;
        this.event = event;
        this.text = text;
        this.sent = sent;
        this.sentAt = sentAt;
    }

//    public static  NotificationEntity of(
//            String uuid,
//            NotificationEvent event,
//            String text
//    ) {
//        return new NotificationEntity(
//                uuid,
//                event,
//                text,
//                null,
//                null
//        );
//    }
}
