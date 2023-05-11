package com.mobisem.kasra.todoapp.repository;

import com.mobisem.kasra.todoapp.model.Todo;
import com.mobisem.kasra.todoapp.model.Users;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends MongoRepository<Todo, String> {


   List<Todo> findByUserId(String userId);
   Todo findByUserIdAndId(String userId, String id);
}
