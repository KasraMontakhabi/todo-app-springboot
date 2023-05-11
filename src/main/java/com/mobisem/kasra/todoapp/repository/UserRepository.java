package com.mobisem.kasra.todoapp.repository;

import com.mobisem.kasra.todoapp.model.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
    public interface UserRepository extends MongoRepository<Users, String> {
        Users findByUsername(String username);
    }


