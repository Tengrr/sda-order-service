package edu.cmu.orderservice.repository;

import edu.cmu.orderservice.entity.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Integer> {
    List<Order> findByUserId(Long userId);
}