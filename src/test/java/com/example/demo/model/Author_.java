package com.example.demo.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Author.class)
public abstract class Author_ {

	public static volatile SingularAttribute<Author, String> firstName;
	public static volatile SingularAttribute<Author, String> lastName;
	public static volatile SingularAttribute<Author, LocalDate> birthdate;
	public static volatile SingularAttribute<Author, LocalDateTime> added;
	public static volatile SingularAttribute<Author, Long> id;
	public static volatile SetAttribute<Author, Post> posts;
	public static volatile SingularAttribute<Author, String> email;

	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";
	public static final String BIRTHDATE = "birthdate";
	public static final String ADDED = "added";
	public static final String ID = "id";
	public static final String POSTS = "posts";
	public static final String EMAIL = "email";

}

