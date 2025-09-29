package com.slash.project.myfoli.domain.interest.repository;

import com.slash.project.myfoli.domain.interest.entity.UserInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {
}
