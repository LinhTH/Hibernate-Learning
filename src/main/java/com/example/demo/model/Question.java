package com.example.demo.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.example.demo.converter.MonetaryAmountConverter;
import com.example.demo.model.advanced.MonetaryAmount;

@Deprecated
@Entity
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE) // Need to check
@Table(name = "questions")
public class Question {
	@Id
	@GeneratedValue(generator = "question_generator")
	@SequenceGenerator(name = "question_generator", sequenceName = "question_sequence")
	private Long id;

	@NotBlank
	@Size(min = 3, max = 100, message = "Title is required, maximum 255 characters.")
	private String title;

	@Column(columnDefinition = "text")
	private String description;

	@org.hibernate.annotations.Type(type = "yes_no")
	@Column(nullable = true)
	private boolean verified;

	@NotNull
	@Convert( // It is Optional as autoPlay is enabled
			converter = MonetaryAmountConverter.class, disableConversion = false)
	@Column(name = "price", length = 63)
	private MonetaryAmount buyNowPrice;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public MonetaryAmount getBuyNowPrice() {
		return buyNowPrice;
	}

	public void setBuyNowPrice(MonetaryAmount buyNowPrice) {
		this.buyNowPrice = buyNowPrice;
	}
}
