package com.academy.springwebapplication.model.repository;

import com.academy.springwebapplication.model.entity.Carriage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarriageRepository extends JpaRepository<Carriage, Integer> {
}
