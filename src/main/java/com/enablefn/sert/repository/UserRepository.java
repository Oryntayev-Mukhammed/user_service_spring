package com.enablefn.sert.repository;

import com.enablefn.sert.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
