package com.example.models;

import lombok.AllArgsConstructor;
// import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
// @EqualsAndHashCode
@ToString
public class RegistrationRequest 
{
    private final String email;    
    private final String password;    
}