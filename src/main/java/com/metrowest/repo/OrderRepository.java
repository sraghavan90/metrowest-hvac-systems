package com.metrowest.repo;

import com.metrowest.entity.Order;
import com.metrowest.entity.UserEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>
{
    List<Order> findByCustomer(UserEntry customer);
}
