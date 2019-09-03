package com.example.fieldforce.repositories;

import com.example.fieldforce.entity.FfaUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<FfaUser, Integer> {
    FfaUser findByName(String userName);
}
