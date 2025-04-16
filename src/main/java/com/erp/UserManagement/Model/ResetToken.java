package com.erp.UserManagement.Model;

import jakarta.persistence.*;


import java.time.LocalDateTime;

@Entity
public class ResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;  // UUID token

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;  // Associated user for the token

    @Column(nullable = false)
    private LocalDateTime createdAt;  // Token creation time

    @Column(nullable = false)
    private LocalDateTime expirationTime;
    // Token expiration time
    public boolean isExpired() {
        return expirationTime.isBefore(LocalDateTime.now());
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(LocalDateTime expirationTime) {
        this.expirationTime = expirationTime;
    }
}

