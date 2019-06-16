package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.model.Employee;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class EmployeeRepositoryTest {
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	EmployeeRepo employeeRepo;
	
	@Autowired
	EntityManager em;
	
	@Test
	public void contextLoads() {
	}

	@Test
	public void shouldStoreSuccessfully() {
		Employee employee = new Employee();
		employee.setDateOfBirth(LocalDate.now());
		employee.setFirstName("FirstName");
		employee.setLastName("lastName");
		employee.setPhoneNumber("12345678");
		employee.setJunk("12");
		employee.setSubsidiaryId(12);
		
		Long employeeId = employeeRepository.save(employee);
		Assert.assertNotNull(employeeId);
		Assert.assertTrue(employeeId > 0);
	}
	
	@Test
	public void getSuccessfully() {
		Employee employee = employeeRepository.getOne(995L);
		Assert.assertEquals("Dmc", employee.getFirstName());
	}
	
	@Test
	public void getByCriteria() {
		long startTx = System.currentTimeMillis();
		List<Employee> result = employeeRepo.createQuery().withPhoneNumber("6342").getTopResultList(1);
		long endQuery = System.currentTimeMillis();
		System.out.println("TIme: " + (endQuery - startTx));
	
	}
}
