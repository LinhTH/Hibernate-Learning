package com.example.demo;

import java.time.LocalDate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.example.demo.config.JPATransactionManagerConfiguration;
import com.example.demo.model.Author;
import com.example.demo.repository.AuthorRepository;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = JPATransactionManagerConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ApplicationTests {

	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private TransactionTemplate transactionTemplate;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private AuthorRepository authorRepository;
	
    @Before
    public void init() {
        try {
            transactionTemplate.execute((TransactionCallback<Void>) transactionStatus -> {
            	Author author = new Author();
        		author.setFirstName("FirstName");
        		author.setLastName("lastName");
        		author.setBirthdate(LocalDate.now());
        		author.setEmail("email@email.com");
        		
        		entityManager.persist(author);
                return null;
            });
        } catch (TransactionException e) {
            LOGGER.error("Failure", e);
        }

}
	
	@Test
	public void contextLoads() {
	}

}
