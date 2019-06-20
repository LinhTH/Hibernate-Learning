package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.hibernate.Session;
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

import com.example.demo.config.JPATransactionManagerConfiguration;
import com.example.demo.model.Author;
import com.example.demo.model.Author_;
import com.example.demo.model.Post;
import com.example.demo.repository.AuthorRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JPATransactionManagerConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class ApplicationTests {

	protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

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
	
	/**
	 * When using Join Fetch with Pagination, HIBERNATE will load all entities and do pagination in memory
	 */
	@Test
	public void joinFetchGoesWithPaginationNotWorkWell() throws SQLStatementCountMismatchException {
		List<Author> result = authorRepository.createQuery(session)
				.withFetchPostTitle("Eos aperiam magni porro alias tenetur.")
				.setFirstResult(0)
				.setMaxResults(10)
				.orderBy(Author_.ADDED)
				.getResultList();
		Assert.assertFalse(result.isEmpty());
		SQLStatementCountValidator.assertSelectCount(1);
		SQLStatementCountValidator.reset();
		
		// Select Query
		// select author0_.id as id1_0_0_, posts2_.id as id1_1_1_, author0_.added as added2_0_0_, author0_.birthdate as birthdat3_0_0_, author0_.email as email4_0_0_, author0_.first_name as first_na5_0_0_, author0_.last_name as last_nam6_0_0_, posts2_.author_id as author_i6_1_1_, posts2_.content as content2_1_1_, posts2_.date as date3_1_1_, posts2_.description as descript4_1_1_, posts2_.title as title5_1_1_, posts2_.author_id as author_i6_1_0__, posts2_.id as id1_1_0__ 
		// from authors author0_ left outer join posts posts1_ on author0_.id=posts1_.author_id inner join posts posts2_ on author0_.id=posts2_.author_id 
		// where posts1_.title=? order by author0_.added asc <-- There is NO LIMIT command
		
		
		List<Author> result2 = authorRepository.createQuery(session)
				.withPostTitle("Eos aperiam magni porro alias tenetur.")
				.setFirstResult(0)
				.setMaxResults(10)
				.orderBy(Author_.ADDED)
				.getResultList();
		Assert.assertFalse(result2.isEmpty());
		SQLStatementCountValidator.assertSelectCount(1);
		
		// Select Query
		// select author0_.id as id1_0_, author0_.added as added2_0_, author0_.birthdate as birthdat3_0_, author0_.email as email4_0_, author0_.first_name as first_na5_0_, author0_.last_name as last_nam6_0_ 
		// from authors author0_ left outer join posts posts1_ on author0_.id=posts1_.author_id 
		// where posts1_.title=? order by author0_.added asc limit ? <- Has LIMIT
	}

	@Test
	public void contextLoads() {
	}

}
