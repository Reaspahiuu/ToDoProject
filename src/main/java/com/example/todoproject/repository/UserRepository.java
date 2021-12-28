package com.example.todoproject.repository;

import com.example.todoproject.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmailAndPassword(String username, String password);
}
