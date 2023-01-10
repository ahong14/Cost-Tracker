package com.cost_tracker.cost_tracker.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table
public class Cost {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Integer id;

    private Double amount;
    private LocalDate date;
    private long date_unix;
    private String title;
    private Integer quantity;
    @Column(name = "user_id")
    private Integer userId;



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
