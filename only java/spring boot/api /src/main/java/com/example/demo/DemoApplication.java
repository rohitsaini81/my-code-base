package com.example.demo;

import com.example.demo.sql.User;
import com.example.demo.sql.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}

@RestController
@RequestMapping("/api")
class DemoController {

	@GetMapping("/hello")
	public String sayHello() {
		return "Hello, Spring Boot!";
	}

		@Autowired
		private UserRepository userRepository;

		@GetMapping
		public List<User> getAllUsers() {
			return userRepository.findAll();
		}

		@PostMapping
		public User createUser(@RequestBody User user) {
			return userRepository.save(user);
		}
}
