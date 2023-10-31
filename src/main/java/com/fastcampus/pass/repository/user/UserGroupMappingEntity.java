package com.fastcampus.pass.repository.user;


import com.fastcampus.pass.repository.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

// @IdClass와 함께 두 개의 @Id 어노테이션을 사용하는 것은 복합 키(composite key)를 나타냄.
// 복합 키란 두 개 이상의 칼럼을 조합하여 하나의 유일한(primary) 키로 사용하는 것으로,
// UserGroupMappingEntity에서 userGroupId와 userId는 둘 다 식별자로 사용됨.
//
// 복합 키를 사용하는 이유:
// * 두 개의 칼럼을 조합하여 데이터를 유일하게 식별할 수 있다면, 별도의 인조 식별자를 추가하지 않고도
//   복합 키를 사용하여 데이터의 무결성을 보장할 수 있습니다.
// * 특정 비즈니스 로직 또는 데이터 구조에서 자연스러운 식별자로 두 개 이상의 칼럼을 사용할 필요가 있을 때 복합 키를 사용.

@Getter
@Setter
@ToString
@Entity
@Table(name = "user_group_mapping")
@IdClass(UserGroupMappingId.class)
public class UserGroupMappingEntity extends BaseEntity {
    @Id
    private String userGroupId;

    @Id
    private String userId;

    private String userGroupName;
    private String description;

}


