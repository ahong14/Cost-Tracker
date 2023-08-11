package com.cost_tracker.cost_tracker.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table
public class User {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Integer id;

    @Column(name = "first_name")
    // @Size, min = 2, element size must be a minimum of 2, validation
    @Size(min = 2)
    private String firstName;

    @Column(name = "last_name")
    // @Size, min = 2, element size must be a minimum of 2, validation
    @Size(min = 2)
    private String lastName;

    // @Email, string must be a well formatted email string
    @Email
    @NotNull
    private String email;

    // @NotNull, value must not be null
    @NotNull
    // @JsonProperty, access = JsonProperty.Access.WRITE_ONLY
    // JsonProperty.Access.WRITE_ONLY, property only accessible during deserialization
    // value will not be part of the response
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(name = "date_created")
    // @JsonProperty value = , serialize key into value provided
    @JsonProperty(value = "date_created")
    private LocalDate dateCreated;

    @OneToMany(mappedBy = "user")
    private Set<Cost> costs;

    public User() {

    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public User(Integer id, String firstName, String lastName, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirst_name() {
        return firstName;
    }

    public void setFirst_name(String firstName) {
        this.firstName = firstName;
    }

    public String getLast_name() {
        return lastName;
    }

    public void setLast_name(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
