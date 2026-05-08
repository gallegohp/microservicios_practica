package com.gallego.ms_users.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.gallego.ms_users.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
}
