package com.pip.lab4.repository;

import com.pip.lab4.entity.Checks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckRepository extends JpaRepository<Checks, Long> {
    List<Checks> findAllByUserEquals(Long id);
}
