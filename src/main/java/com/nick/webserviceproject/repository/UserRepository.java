package com.nick.webserviceproject.repository;

import com.nick.webserviceproject.model.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<CustomUser, Long> {
    CustomUser findByUsername(String username);
}
