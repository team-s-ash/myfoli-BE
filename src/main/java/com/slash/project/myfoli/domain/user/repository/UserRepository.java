package com.slash.project.myfoli.domain.user.repository;

import com.slash.project.myfoli.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
