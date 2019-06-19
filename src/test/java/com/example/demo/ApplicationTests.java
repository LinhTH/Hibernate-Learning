package com.example.demo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.TransactionTemplate;

import com.example.demo.config.JPATransactionManagerConfiguration;
import com.example.demo.model.Author;
import com.example.demo.model.Post;
import com.example.demo.repository.AuthorRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JPATransactionManagerConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class ApplicationTests {

	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private TransactionTemplate transactionTemplate;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private AuthorRepository authorRepository;
	
	private Session session;

	@Before
	public void init() throws SQLStatementCountMismatchException {
		session = entityManager.unwrap(Session.class);
	}
	
	@After
	public void clean() {
		session.close();
	}
	
	@Test
	public void shouldGenerateTwoSQLIfOnlyJoinPost() throws SQLStatementCountMismatchException {
		List<Author> result = authorRepository.createQuery(session).withPostTitle("Eos aperiam magni porro alias tenetur.").getResultList();
		Assert.assertFalse(result.isEmpty());
		Assert.assertEquals(Long.valueOf(1L), result.get(0).getId());
		Post post = new ArrayList<Post>(result.get(0).getPosts()).get(0);
		Assert.assertEquals("Eos aperiam magni porro alias tenetur.", post.getTitle());
		SQLStatementCountValidator.assertSelectCount(2);
		SQLStatementCountValidator.reset();
	}
	
	@Test
	public void shouldGenerateOneSQLIfJoinAndFetchPost() throws SQLStatementCountMismatchException {
		List<Author> result = authorRepository.createQuery(session).withFetchPostTitle("Eos aperiam magni porro alias tenetur.").getResultList();
		Assert.assertFalse(result.isEmpty());
		Assert.assertEquals(Long.valueOf(1L), result.get(0).getId());
		Post post = new ArrayList<Post>(result.get(0).getPosts()).get(0);
		Assert.assertEquals("Eos aperiam magni porro alias tenetur.", post.getTitle());
		SQLStatementCountValidator.assertSelectCount(1);
		SQLStatementCountValidator.reset();
	}

	@Test
	public void contextLoads() {
	}

}
