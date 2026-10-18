package com.abdellah.demo.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "internships")
public class Internship
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    // Many internship records can belong to the same user
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 150)
    private String company;

    @Column(nullable = false, length = 150)
    private String role;

    @Column(length = 150)
    private String location;

    @Column(name = "work_mode", length = 30)
    private String workMode;

    @Column(name = "application_date")
    private LocalDate applicationDate;

    private LocalDate deadline;

    @Column(nullable = false, length = 50)
    private String status = "À découvrir";

    @Column(length = 150)
    private String contact;

    @Column(columnDefinition = "TEXT")
    private String notes;


    /*=============================*/
    /*  Construcutor and Methodes  */
    /*=============================*/
    public Internship() {}

    /*================*/
    /*    Getters     */
    /*================*/
    public Long getId()
    {
        return id;
    }

    public User getUser()
    {
        return user;
    }

    public String getCompany()
    {
        return company;
    }

    public String getRole()
    {
        return role;
    }

    public String getLocation()
    {
        return location;
    }

    public String getWorkMode()
    {
        return workMode;
    }

    public LocalDate getApplicationDate()
    {
        return applicationDate;
    }

    public LocalDate getDeadline()
    {
        return deadline;
    }

    public String getStatus()
    {
        return status;
    }

    public String getContact()
    {
        return contact;
    }

    public String getNotes()
    {
        return notes;
    }

    /*================*/
    /*    Setters     */
    /*================*/
    public void setUser(User user)
    {
        this.user = user;
    }

    public void setCompany(String company)
    {
        this.company = company;
    }

    public void setRole(String role)
    {
        this.role = role;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public void setWorkMode(String workMode)
    {
        this.workMode = workMode;
    }

    public void setApplicationDate(LocalDate applicationDate)
    {
        this.applicationDate = applicationDate;
    }

    public void setDeadline(LocalDate deadline)
    {
        this.deadline = deadline;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public void setContact(String contact)
    {
        this.contact = contact;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }
}
