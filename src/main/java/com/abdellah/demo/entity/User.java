package com.abdellah.demo.entity;

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
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY) // Auto-increment ID 
    private Long id;
    
    @NotBlank(message = "Name cannot be blank")
    private String nom;

    @Email   (message = "Email is not valid")
    @NotBlank(message = "Email cannot be blank")
    @Column  (nullable = false, unique = true)
    private String email;

    // The password can be received, but will never be sent out
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "Password cannot be blank")
    @Column  (nullable = false)
    private String password;


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
    
    public String getNom()
    {
        return this.nom;
    }

    public String getEmail()
    {
        return this.email;
    }

    public String getPassword()
    {
        return this.password;
    }


    /*================*/
    /*    Setters     */
    /*================*/
    public void setNom(String nom)
    {
        this.nom = nom;
    }

    public void setEmail(String email) 
    {
        this.email = email;
    }

    public void setPassword(String password) 
    {
        this.password = password;
    }
}