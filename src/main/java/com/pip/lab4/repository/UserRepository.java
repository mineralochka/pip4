package com.pip.lab4.repository;

import com.pip.lab4.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserAccount, Long>{
    UserAccount findById(Long id);
}
