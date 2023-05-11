package com.mobisem.kasra.todoapp.service;

import com.mobisem.kasra.todoapp.model.Todo;
import com.mobisem.kasra.todoapp.model.Users;
import com.mobisem.kasra.todoapp.repository.TodoRepository;
import com.mobisem.kasra.todoapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {
    private TodoRepository todoRepository;

    @Autowired
    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> getTodosbyUserId(String userId){
        return todoRepository.findByUserId(userId);
    }
}
