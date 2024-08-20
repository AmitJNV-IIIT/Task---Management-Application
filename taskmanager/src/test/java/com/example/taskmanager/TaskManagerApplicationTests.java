package com.example.taskmanager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TaskManagerApplicationTests {

	@Test
	void contextLoads() {
		// This test will pass if the application context loads successfully.
		// It verifies that the Spring context is configured correctly and all beans are loaded.
	}

	@Test
	void main() {
		// This test will pass if the main method runs without throwing any exceptions.
		// It ensures that the application can start up without issues.
		TaskManagerApplication.main(new String[]{});
	}
}
