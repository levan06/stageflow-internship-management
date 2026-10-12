package com.abdellah.demo.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User 
{
    /*===============*/
    /*   Attributs   */
    /*===============*/
    /* ID */
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID 
    private Long id;
    
    /* NAME */
    @NotBlank(message = "Name cannot be blank")
    private String name;

    /* EMAIL */
    @Email   (message = "Email is not valid")
    @NotBlank(message = "Email cannot be blank")
    @Column  (nullable = false, unique = true)
    private String email;

    /* PASSWORD */
    //The password can be received, but will never be sent out
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Password cannot be blank")
    @Column(nullable = false)
    private String password;

    /* EMAIL VERIFIED */
    @Column(name = "emailverified", nullable = false)
    private boolean emailVerified = false;

    /* EMAIL VERIFICATION TOKEN */
    @JsonIgnore
    @Column(name = "email_verification_token", unique = true)
    private String emailVerificationToken;

    @Column(name = "email_verification_token_expiry")
    private LocalDateTime emailVerificationTokenExpiry;


    /*=============================*/
    /*  Construcutor and Methodes  */
    /*=============================*/
    public User() {}

    /*================*/
    /*    Getters     */
    /*================*/
    public Long getId()
    {
        return this.id;
    }
    
    public String getName()
    {
        return this.name;
    }

    public String getEmail()
    {
        return this.email;
    }

    public String getPassword()
    {
        return this.password;
    }

    public boolean isEmailVerified()
    {
        return this.emailVerified;
    }

    public String getEmailVerificationToken() 
    {
        return this.emailVerificationToken;
    }

    public LocalDateTime getEmailVerificationTokenExpiry() 
    {
        return this.emailVerificationTokenExpiry;
    }


    /*================*/
    /*    Setters     */
    /*================*/
    public void setName(String name)
    {
        this.name = name;
    }

    public void setEmail(String email) 
    {
        this.email = email;
    }

    public void setPassword(String password) 
    {
        this.password = password;
    }

    public void setEmailVerified(boolean emailVerified)
    {
        this.emailVerified = emailVerified;
    }

    public void setEmailVerificationToken(String token) 
    {
        this.emailVerificationToken = token;
    }

    public void setEmailVerificationTokenExpiry(LocalDateTime expiry) 
    {
        this.emailVerificationTokenExpiry = expiry;
    }
}