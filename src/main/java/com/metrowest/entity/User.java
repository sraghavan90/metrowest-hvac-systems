package com.metrowest.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password_hash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public User() { }

    public void setUsername(String username) { this.username = username; }
    public String getUsername() { return username; }

    public void setPassword_hash(String passwordHash) { this.password_hash = passwordHash; }
    public String getPassword_hash() { return password_hash; }

    public void setRole(Role role) { this.role = role; }
    public Role getRole() { return role; }
}