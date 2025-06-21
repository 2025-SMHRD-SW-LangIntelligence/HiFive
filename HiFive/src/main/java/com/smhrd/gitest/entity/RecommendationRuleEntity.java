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
@Table(name = "recommendation_rule")

public class RecommendationRuleEntity {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String category;
	
	@Column(name ="tag_name", nullable =false)
	private String tagName;
	
	@Column(nullable=false)
	private Double weight;
	
	
	
	
	
	

}
