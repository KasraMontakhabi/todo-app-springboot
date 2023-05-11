package com.mobisem.kasra.todoapp.controller;

import com.mobisem.kasra.todoapp.model.Todo;
import com.mobisem.kasra.todoapp.repository.TodoRepository;
import com.mobisem.kasra.todoapp.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/todos")
public class TodoController  {

    private TodoRepository todoRepository;

    private TodoService todoService;

    public TodoController(TodoRepository todoRepository, TodoService todoService){
        this.todoRepository = todoRepository;
        this.todoService = todoService;
    }
@PostMapping("/users/{userId}")
public ResponseEntity<Map<String, String>> addTodo(@PathVariable String userId, @RequestBody Todo todo){
    todo.setUserId(userId);
    Todo savedTodo = todoRepository.save(todo);
    Map<String, String> response = new HashMap<>();
    response.put("message", "todo created");
    response.put("userId", userId);
    response.put("todoId", savedTodo.getId());
    return ResponseEntity.ok(response);
}

    @GetMapping("/users/{userId}/todo/{todoId}") // try /user/{userId}/{id}
    public Todo getTodo(@PathVariable String userId, @PathVariable String todoId){
        Todo todo = todoRepository.findByUserIdAndId(userId, todoId);
        if (todo == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found");
        }
        return todo;
    }
    @GetMapping("/users/{userId}/todo")
    public List<Todo> getTodosByUserId(@PathVariable String userId){
        return todoService.getTodosbyUserId(userId);

    }
    @PutMapping("/users/{userId}/todo/{todoId}")
    public Todo editTodo(@PathVariable String userId, @PathVariable String todoId, @RequestBody Todo updatedTodo){

        Todo todo = todoRepository.findByUserIdAndId(userId, todoId);
        todo.setDescription(updatedTodo.getDescription());
        todo.setCompleted(updatedTodo.isCompleted());
        return todoRepository.save(todo);
    }
    @DeleteMapping("/users/{userId}/todo/{todoId}")
    public void deleteTodo(@PathVariable String userId, @PathVariable String todoId){
        Todo todo = todoRepository.findByUserIdAndId(userId, todoId);
        todoRepository.delete(todo);

    }
}
