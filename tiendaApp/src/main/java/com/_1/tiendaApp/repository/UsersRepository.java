package com._1.tiendaApp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com._1.tiendaApp.entity.Users;

public interface  UsersRepository extends JpaRepository<Users, Long>{
    Optional<Users> findByUserName (String userName);
    Optional<Users> findByEmail (String email);
}
