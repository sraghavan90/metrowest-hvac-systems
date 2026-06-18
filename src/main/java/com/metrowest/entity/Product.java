package com.metrowest.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductType type;

    public Product() { }

    public Long getId() { return id; }

    public String getName()      { return name; }
    public BigDecimal getPrice() { return price; }
    public ProductType getType() { return type; }

    public void setName(String name)       { this.name = name; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setType(ProductType type)  { this.type = type; }
}