package com.book.BookAPI.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tbl_book")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Book implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "ISBN")
	private String isbn;
	
	@Column(name = "DATE_PUBLISHED")
	private Long datePublished;
	
	@Column(name = "CREATED_BY")
	private Integer createdBy;
	
	@Column(name = "CREATED_DATE")
	private Long createdDate;
	
	@Column(name = "UPDATED_BY")
	private Integer updatedBy;
	
	@Column(name = "UPDATED_DATE")
	private Long updatedDate;
	
	@Column(name = "ACTIVE")
	private Integer active;
	
}
