package com.fastcampus.pass.repository.user;

import com.fastcampus.pass.repository.BaseEntity;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Map;

@Getter
@Setter
@ToString
@Table(name = "user")
// JSON data 를 Map 으로 변환하여 field value 로 사용 가능하도록 type 변환 annotation
@TypeDef(name = "json", typeClass = JsonStringType.class)
@Entity
public class UserEntity extends BaseEntity {

    @Id
    private String userId;

    @Column(nullable = false)
    private String userName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status;

    private String phone;

    // JSON 형태로 저장되어 있는 문자열 데이터를 Map 으로 mapping
    @Type(type = "json")
    private Map<String, Object> meta;

    public String getUuid() {
        String uuid = null;
        if (meta.containsKey("uuid")) {
            uuid = String.valueOf(meta.get("uuid"));
        }
        return uuid;
    }

    protected UserEntity() {}

    private UserEntity(
            String userId,
            String userName,
            UserStatus status,
            String phone,
            Map<String, Object> meta
    ) {
        this.userId = userId;
        this.userName = userName;
        this.status = status;
        this.phone = phone;
        this.meta = meta;
    }

    public static UserEntity of(
            String userId,
            String userName,
            UserStatus status,
            String phone,
            Map<String, Object> meta
    ) {
        return new UserEntity(
                userId,
                userName,
                status,
                phone,
                meta
        );
    }
}
