package com.example.demo.model;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Employee.class)
public abstract class Employee_ {

	public static volatile SingularAttribute<Employee, String> firstName;
	public static volatile SingularAttribute<Employee, String> lastName;
	public static volatile SingularAttribute<Employee, String> phoneNumber;
	public static volatile SetAttribute<Employee, Question> questions;
	public static volatile SingularAttribute<Employee, String> junk;
	public static volatile SingularAttribute<Employee, LocalDate> dateOfBirth;
	public static volatile SingularAttribute<Employee, Integer> subsidiaryId;
	public static volatile SingularAttribute<Employee, Long> id;

	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";
	public static final String PHONE_NUMBER = "phoneNumber";
	public static final String QUESTIONS = "questions";
	public static final String JUNK = "junk";
	public static final String DATE_OF_BIRTH = "dateOfBirth";
	public static final String SUBSIDIARY_ID = "subsidiaryId";
	public static final String ID = "id";

}

