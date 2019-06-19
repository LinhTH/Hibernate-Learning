package com.example.demo.model;

import com.example.demo.model.advanced.MonetaryAmount;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Question.class)
public abstract class Question_ {

	public static volatile SingularAttribute<Question, Boolean> verified;
	public static volatile SingularAttribute<Question, String> description;
	public static volatile SingularAttribute<Question, Long> id;
	public static volatile SingularAttribute<Question, MonetaryAmount> buyNowPrice;
	public static volatile SingularAttribute<Question, String> title;

	public static final String VERIFIED = "verified";
	public static final String DESCRIPTION = "description";
	public static final String ID = "id";
	public static final String BUY_NOW_PRICE = "buyNowPrice";
	public static final String TITLE = "title";

}

