package com.fastcampus.pass.repository.user;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

// JPA(Java Persistence API)에서 복합 키를 사용하려면 복합 키를 나타내는 별도의 클래스를 정의해야
// 이때, 이 클래스는 Serializable 인터페이스를 구현 필요
//
// Serializable 구현의 이유:
// * JPA는 복합 키를 사용하는 엔터티의 키 객체가 직렬화될 수 있어야함.
//   이를 통해 JPA 구현체는 키 객체를 캐시, 직렬화, 역직렬화하는 등의 작업을 수행할 수 있음.

@Getter
@Setter
@ToString
public class UserGroupMappingId implements Serializable {
    private String userGroupId;
    private String userId;
}
