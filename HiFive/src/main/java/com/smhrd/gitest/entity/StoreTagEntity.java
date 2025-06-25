package com.smhrd.gitest.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * '가게'와 '태그' 사이의 다대다(N:M) 관계를 연결하는 중간 테이블인
 * 'store_tag' 테이블에 매핑되는 JPA 엔티티 클래스입니다.
 */
@Entity // 이 클래스가 데이터베이스 테이블과 매핑되는 JPA 엔티티임을 선언합니다.
@Table(name = "store_tag") // 실제 DB의 'store_tag' 테이블과 연결합니다.
@Getter
@Setter
@NoArgsConstructor
public class StoreTagEntity {

    @Id // 이 필드가 테이블의 기본 키(Primary Key)임을 나타냅니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB가 ID를 자동으로 생성하고 관리하도록 합니다. (Auto-increment)
    private Long id;

    // ★★★ 중요: 자바의 변수명(storeId)과 DB의 컬럼명(store_id)이 다를 경우,
    // @Column 어노테이션으로 정확히 연결해주는 것이 매우 좋은 습관입니다.
    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "tag_id")
    private Long tagId;

    // 필요에 따라 생성자 등을 추가할 수 있지만, Lombok의 @NoArgsConstructor로 충분합니다.
}