package com.slash.project.myfoli.domain.interest.repository;

import com.slash.project.myfoli.domain.interest.entity.UserInterest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {
}
