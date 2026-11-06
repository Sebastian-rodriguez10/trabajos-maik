package com._1.users.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com._1.users.entity.Users;

public interface UsersRepository extends JpaRepository<Users, Long>{
    Optional<Users> findByUsername(String username);

    Optional<Users> findByEmail(String email);

}
