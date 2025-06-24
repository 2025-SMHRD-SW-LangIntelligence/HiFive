package com.smhrd.gitest.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // 이 클래스가 데이터베이스 테이블과 매핑되는 JPA 엔티티임을 선언합니다.
@Table(name = "dong_mapping") // 실제 DB의 'dong_mapping' 테이블과 연결합니다.
@Getter // Getter 메서드를 자동으로 생성해주는 Lombok 어노테이션입니다.
@Setter // Setter 메서드를 자동으로 생성합니다.
@NoArgsConstructor // 파라미터가 없는 기본 생성자를 자동으로 생성합니다.
@AllArgsConstructor // 모든 필드를 파라미터로 받는 생성자를 자동으로 생성합니다.
public class DongMappingEntity {

    @Id // 이 필드가 테이블의 기본 키(Primary Key)임을 나타냅니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB가 ID를 자동으로 생성하고 관리하도록 합니다. (Auto-increment)
    private Long id;

    @Column(name = "admin_dong", nullable = false) // 'admin_dong' 컬럼과 매핑, null을 허용하지 않습니다.
    private String adminDong;

    @Column(name = "legal_dong", nullable = false) // 'legal_dong' 컬럼과 매핑, null을 허용하지 않습니다.
    private String legalDong;

}