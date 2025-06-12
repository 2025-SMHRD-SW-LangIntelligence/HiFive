package com.smhrd.gitest.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="campus_25W_LI_p2_1")
public class MemberEntity {
	
	//pk 값이 필수 
		@Id //jakarta 사용할 것 -> pk로 사용하겠다
		@GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increase
		private Long idx; // 객체타입으로 삽입 --> DB에 해당 값이 없으면 null로 값이 넘어옵니다.
		
		@Column(nullable = false, unique = true)
		private String email;
		private String pw;
		private String nickname;
		private Integer age;
		private String sex;
}
