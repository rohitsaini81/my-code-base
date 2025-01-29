package com.example.demo;

import com.example.demo.sql.User;
import com.example.demo.sql.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Optional;

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
		List<User> temp = getAllUsers();
		System.out.println(temp.get(0));
		return "Hello, Spring Boot!";
	}

	@PostMapping("/add")
	public ResponseEntity<User> addUser(@RequestBody User user) {
		User savedUser = userRepository.save(user);
		return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
	}

		@Autowired
		private UserRepository userRepository;

		@GetMapping("/all")
		public List<User> getAllUsers() {
			return userRepository.findAll();
		}

	@GetMapping("/{name}")
	public ResponseEntity<User> getUserByName(@PathVariable String name) {
		Optional<User> user = userRepository.findByName(name);
		return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
		return userRepository.findById(id).map(user -> {
			user.setName(updatedUser.getName());
			user.setEmail(updatedUser.getEmail());
			userRepository.save(user);
			return ResponseEntity.ok(user);
		}).orElseGet(() -> ResponseEntity.notFound().build());
	}
	// DELETE request to remove a user by ID
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable Long id) {
		if (userRepository.existsById(id)) {
			userRepository.deleteById(id);
			return ResponseEntity.ok("User with ID " + id + " deleted successfully.");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
		}
	}


	@GetMapping("/set-cookie")
	public String setCookie(HttpServletResponse response) {
		Cookie cookie = new Cookie("rohitsaini81", "password"); // Create a cookie
		cookie.setMaxAge(60*2); // Cookie expires in 2 minutes day
		cookie.setHttpOnly(true); // Makes it accessible only via HTTP (not JavaScript)
		cookie.setPath("/"); // Cookie is accessible to all endpoints
		response.addCookie(cookie); // Add cookie to response

		return "Cookie has been set!";
	}

}
