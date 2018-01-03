package com.pip.lab4;

import com.pip.lab4.entity.UserAccount;
import com.pip.lab4.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Lab4Application {
	public static void main(String[] args) {
        SpringApplication.run(Lab4Application.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepository){
	    return (e) -> {
	        UserAccount user = new UserAccount();
	        user.setId(1L);
	        user.setPasswordHash("1234".hashCode());
	        userRepository.save(user);
        };
    }


}
