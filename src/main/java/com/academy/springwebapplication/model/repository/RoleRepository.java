package com.academy.springwebapplication.model.repository;

import com.academy.springwebapplication.model.entity.Role;
import com.academy.springwebapplication.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}
