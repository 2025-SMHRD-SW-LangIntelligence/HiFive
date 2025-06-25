package com.smhrd.gitest.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name="mood_tag")
@NoArgsConstructor
public class MoodTagEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long tagId;
	
	@Column(name = "tag_name")
	private String tagName;
}
