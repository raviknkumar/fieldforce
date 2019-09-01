package com.example.fieldforce.repositories;

import com.example.fieldforce.entity.FfaUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FfaUserRepo extends JpaRepository<FfaUser, Integer> {
}
