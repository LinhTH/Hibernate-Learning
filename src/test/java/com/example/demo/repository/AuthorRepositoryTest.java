package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.model.Author;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class AuthorRepositoryTest {
	@Autowired
	private AuthorRepository authorRepository;
	
	@Test
	public void contextLoads() {
	}
	
	@Test
	public void shouldStoreSuccessfully() {
		Author author = new Author();
		author.setFirstName("FirstName");
		author.setLastName("lastName");
		author.setBirthdate(LocalDate.now());
		author.setEmail("email@email.com");
		
		Long authorId = authorRepository.save(author);
		Assert.assertTrue(authorId > 0);
		
	}
	
	@Test
	public void getSuccessfully() {
		Author author = authorRepository.getOne(5L);
		Assert.assertNotNull(author);
		Assert.assertFalse(author.getPosts().isEmpty());
		
		List<Author> authors = authorRepository.findAll();
		Assert.assertTrue(authors.size() >= 2000);
	}
	

	@Test
	public void getByCriteria() {
		long startTx = System.currentTimeMillis();
		List<Author> result = authorRepository.createQuery().withEmail("morar.brittany@example.net").getResultList();
		Assert.assertFalse(result.isEmpty());
		long endQuery = System.currentTimeMillis();
		System.out.println("TIme: " + (endQuery - startTx));
	
	}
}
