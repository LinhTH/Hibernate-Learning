//package com.example.demo.controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.example.demo.model.Employee;
//import com.example.demo.repository.EmployeeRepository;
//
//@RestController
//@RequestMapping("/api/v1/")
//public class EmployeeController {
//
//	@Autowired
//	private EmployeeRepository employeeRepository;
//
//	@GetMapping("/employees")
//	public List<Employee> getAllEmployees() {
//		return employeeRepository.findAll();
//	}
//
//	@GetMapping("/employees/{id}")
//	public ResponseEntity<Employee> getUsersById(@PathVariable(value = "id") Long employeeId) {
//		
//		Employee employee = employeeRepository.getOne(employeeId);
//		return ResponseEntity.ok().body(employee);
//	}
//}
