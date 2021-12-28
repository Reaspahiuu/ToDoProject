package com.example.todoproject.repository;

import com.example.todoproject.model.UserSession;
import org.springframework.data.repository.CrudRepository;

public interface UserSessionRepository extends CrudRepository<UserSession, Long> {
    UserSession findBySessionHashCode(String sessionHashCode);
}
