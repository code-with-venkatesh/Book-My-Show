package com.driver.bookMyShow.Repositories;

import com.driver.bookMyShow.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByEmailId(String emailId);
}