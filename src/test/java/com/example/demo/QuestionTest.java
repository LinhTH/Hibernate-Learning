package com.example.demo;


import java.util.Properties;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.model.Employee;
import com.example.demo.model.Question;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class QuestionTest {

	Long id;
	
	@Autowired
    private SessionFactory sessionFactory;
	
	private Session getCurrentSession() {
		return sessionFactory.getCurrentSession();
	}
	
	@Test
	public void shouldStoreSuccessfully() throws Exception {
		Question question = new Question();
		question.setDescription("Description");
		question.setTitle("Title");
		
		id = (Long) getCurrentSession().save(question);
		Assert.assertNotNull(id);
	}
	
	@Test
	public void shouldLoad() {
		Question question = new Question();
		question.setDescription("Description");
		question.setTitle("Title");
		id = (Long) getCurrentSession().save(question);
		Question a = getCurrentSession().get(Question.class, id);
		Assert.assertNotNull(a);
		
		Employee employee = getCurrentSession().get(Employee.class, 995l);
		Assert.assertNotNull(employee);
	}
}
