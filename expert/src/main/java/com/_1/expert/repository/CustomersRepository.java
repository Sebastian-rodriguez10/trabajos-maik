package com._1.expert.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com._1.expert.entity.Customers;

public interface CustomersRepository extends JpaRepository<Customers, Integer>{
    
}
