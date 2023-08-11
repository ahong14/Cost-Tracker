package com.cost_tracker.cost_tracker.models;

import jakarta.persistence.*;

import java.time.LocalDate;

// @Entity, indicates JPA entity
// @Table, specifies primary table for given entity
@Entity
@Table
public class Cost {
    // @Id - specifies primary key of entity
    @Id
    // @GeneratedValue, used to generate primary keys for @Id
    // strategy - GenerationType.IDENTITY, auto incremented database value for primary key
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Integer id;

    private Double amount;
    private LocalDate date;
    private long date_unix;
    private String title;
    private Integer quantity;
    private String category;
    
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Cost(Integer id, Double amount, LocalDate date, long date_unix, String title, Integer quantity, String category, Integer userId, User user) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.date_unix = date_unix;
        this.title = title;
        this.quantity = quantity;
        this.category = category;
        this.userId = userId;
        this.user = user;
    }

    // @Column, map property to column name in table
    @Column(name = "user_id")
    private Integer userId;


    @ManyToOne
    @JoinColumn(name = "user_cost_id")
    private User user;


    public Cost(Double amount, LocalDate date, long date_unix, String title, Integer quantity, Integer userId) {
        this.amount = amount;
        this.date = date;
        this.date_unix = date_unix;
        this.title = title;
        this.quantity = quantity;
        this.userId = userId;
    }

    public Cost() {

    }

    public Cost(Integer id, Double amount, LocalDate date, long date_unix, String title, Integer quantity, Integer userId) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.date_unix = date_unix;
        this.title = title;
        this.quantity = quantity;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public long getDate_unix() {
        return date_unix;
    }

    public void setDate_unix(long date_unix) {
        this.date_unix = date_unix;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getUser_id() {
        return userId;
    }

    public void setUser_id(Integer userId) {
        this.userId = userId;
    }
}
