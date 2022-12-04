package com.example.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.controllers.ConfirmationTokenController;
import com.example.models.ConfirmationToken;
import com.example.models.User;
import com.example.repositories.UserRepository;

import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService
{
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException
    {
        return userRepository.findByEmail(email).orElseThrow
        (
            ()-> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email))
        );
    }

    public String signUpUser(User user)
    {
        boolean userExists = userRepository.findByEmail(user.getEmail()).isPresent();

        if(userExists)
        {
            // TODO: Handle exception
            return "email already taken";
        }

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setId(userRepository.maxId() + 1L);

        userRepository.save(user);

        // TODO: Send confirmation token
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(15), user);
        confirmationTokenController.save(confirmationToken);

        // TODO: Send email

        return token;
    }

    private final UserRepository userRepository;
    private final ConfirmationTokenController confirmationTokenController;
    private final BCryptPasswordEncoder passwordEncoder;
    private final static String USER_NOT_FOUND_MSG = "user with email %s not found";
}
