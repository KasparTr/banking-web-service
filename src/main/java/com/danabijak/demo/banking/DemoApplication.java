package com.danabijak.demo.banking;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.danabijak.demo.banking.entity.Role;
import com.danabijak.demo.banking.entity.User;
import com.danabijak.demo.banking.repositories.UserRepository;
import com.danabijak.demo.banking.services.UserService;

@SpringBootApplication
public class DemoApplication {
    
//
//    @Bean
//    public CommandLineRunner setupDefaultUser(UserService service) {
//        return args -> {
//            service.save(new User(
//                    "user", //username
//                    "user", //password
//Arrays.asList(new Role("USER"), new Role("ACTUATOR")),//roles 
//                    true//Active
//            ));
//        };
//    }
	
    
    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
    

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}