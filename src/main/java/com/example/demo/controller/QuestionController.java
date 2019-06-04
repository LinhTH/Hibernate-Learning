package com.example.demo.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Question;
import com.example.demo.repository.QuestionRepository;

@RestController
@RequestMapping("/api/v1")
public class QuestionController {
	
	@Autowired
	private QuestionRepository questionRepository;
	
	@GetMapping("/questions")
	public List<Question> getAllQuestions() {
		return questionRepository.findAll();
	}
	
	@PostMapping("/questions")
	public Question createQuestion(@Valid @RequestBody Question question) {
		return questionRepository.save(question);
	}

}
