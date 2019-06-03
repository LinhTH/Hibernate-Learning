package com.example.demo.repository;

import java.time.LocalDate;
import java.util.Optional;

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
		
		Employee employee2 = employeeRepository.save(employee);
		Assert.assertNotNull(employee2);
		Assert.assertTrue(employee2.getId() > 0);
	}
	
	@Test
	public void getSuccessfully() {
		Optional<Employee> employeeOpt = employeeRepository.findById(995L);
		Assert.assertTrue(employeeOpt.isPresent());
		Assert.assertEquals("Dmc", employeeOpt.get().getFirstName());
	}
}
