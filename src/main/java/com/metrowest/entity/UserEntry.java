package com.metrowest.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserEntry
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password_hash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public UserEntry() { }

    public Long getId() { return id; }

    public void setEmail(String email)                { this.email = email; }
    public void setUsername(String username)          { this.username = username; }
    public void setPassword_hash(String passwordHash) { this.password_hash = passwordHash; }
    public void setRole(Role role)                    { this.role = role; }

    public String getEmail()         { return email; }
    public String getUsername()      { return username; }
    public String getPassword_hash() { return password_hash; }
    public Role getRole()            { return role; }
}