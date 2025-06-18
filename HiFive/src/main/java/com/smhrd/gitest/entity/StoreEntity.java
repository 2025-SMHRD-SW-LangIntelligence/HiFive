package com.smhrd.gitest.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "store") // 테이블이 이미 생성됬기 때문에 name = 테이블명 지정
public class StoreEntity {
	

	    @Id //pk 값
	    @GeneratedValue(strategy = GenerationType.IDENTITY) 
	    //숫자 자동 증가 MySQL AUTO_INCREMENT
	    @Column(name="store_id")// DB 컬럼명은 store_id
	    private Long storeId;	// 필드명은 camelCase로 변경

	    @Column(name="store_name", nullable = false) // not null 설정
	    private String storeName;

	    @Column(name="store_location" ,nullable = false)
	    private String storeLocation;

	    @Column(name="store_owner" ,nullable = false)
	    private String storeOwner;
	}

