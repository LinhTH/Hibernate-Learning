package com.example.demo.repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.model.Author;
import com.example.demo.model.Post;

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
		
		Author actual = authorRepository.makePersistent(author);
		Assert.assertNotNull(actual);
		Assert.assertNotNull(actual.getId());
		
	}
	
	@Test
	public void getSuccessfully() {
		Author author = authorRepository.findById(5L);
		Assert.assertNotNull(author);
		Assert.assertFalse(author.getPosts().isEmpty());
		
		List<Author> authors = authorRepository.findAll();
		Assert.assertTrue(authors.size() >= 2000);
	}
	
	@Test
	public void shouldCountSuccessfully() {
		long startTx = System.currentTimeMillis();
		Long result = authorRepository.createQuery().withEmail("morar.brittany@example.net").count();
		Assert.assertEquals(Long.valueOf(1L), result);
		
		result = authorRepository.createQuery().withLastName("Predovic").count();
		Assert.assertEquals(Long.valueOf(4L), result);
		long endQuery = System.currentTimeMillis();
		System.out.println("TIme: " + (endQuery - startTx));
	}
	
	@Test
	public void shouldGetByPostTitle() {
		List<Author> result = authorRepository.createQuery().withPostTitle("Eos aperiam magni porro alias tenetur.").getResultList();
		Assert.assertFalse(result.isEmpty());
		Assert.assertEquals(Long.valueOf(1L), result.get(0).getId());
		Post post = new ArrayList<Post>(result.get(0).getPosts()).get(0);
		Assert.assertEquals("Eos aperiam magni porro alias tenetur.", post.getTitle());
		
		
		Long count = authorRepository.createQuery().withPostTitle("Eos aperiam magni porro alias tenetur.").count();
		Assert.assertEquals(Long.valueOf(1L), count);
	}
	
	@Test
	public void getByCriteria() {
		long startTx = System.currentTimeMillis();
		List<Author> result = authorRepository.createQuery().withEmail("francis26@example.com").getResultList();
		Assert.assertFalse(result.isEmpty());
		long endQuery = System.currentTimeMillis();
		System.out.println("TIme: " + (endQuery - startTx));
	}
	
}
