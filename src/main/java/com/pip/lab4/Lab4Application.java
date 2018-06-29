package com.pip.lab4;

import com.pip.lab4.entity.UserAccount;
import com.pip.lab4.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class Lab4Application {

	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public static void main(String[] args) {
        SpringApplication.run(Lab4Application.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepository){
	    return (e) -> {
	        UserAccount user = new UserAccount();
	        user.setId(1L);
	        user.setPasswordHash(passwordEncoder.encode("1234"));
	        userRepository.save(user);
        };
    }
}
