package com.mobisem.kasra.todoapp.tests;

import com.mobisem.kasra.todoapp.model.Todo;
import com.mobisem.kasra.todoapp.model.Users;
import com.mobisem.kasra.todoapp.repository.TodoRepository;
import com.mobisem.kasra.todoapp.repository.UserRepository;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TodoAppTest {
    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;



    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
        userRepository.deleteAll();
    }

    //Helper method LOGIN
    public String loginAndGetResponseBody(String username, String password) throws Exception {

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/users/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + username + "\",\"password\":\"" + password+"\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        return result.getResponse().getContentAsString();

    }
    //Helper method POST
    public String postTodoAndGetResponseBody(Todo todo,String userId,String token) throws Exception {
        String todoJson = "{ \"description\": \"" + todo.getDescription() + "\", \"completed\": " + todo.isCompleted() + " }";

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/todos/users/" + userId)
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(todoJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        return result.getResponse().getContentAsString();
    }
    // Helper method GET
    public String getTodoByIdResponseBody(String userId,String token, String todoId) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("http://localhost:8080/todos/users/" + userId + "/todo/" + todoId)
                        .header("Authorization", token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        return result.getResponse().getContentAsString();
    }
    // Helper method Put
    public String putTodoResponseBody(String userId, String todoId, String token, String  todoPutJson) throws Exception{
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .put("http://localhost:8080/todos/users/" + userId + "/todo/" + todoId)
                        .header("Authorization", token).contentType(MediaType.APPLICATION_JSON)
                        .content(todoPutJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        return result.getResponse().getContentAsString();
    }

    // Helper method Delete
    public void DeleteTodo(String userId,String token, String todoId) throws Exception{
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .get("http://localhost:8080/todos/users/" + userId + "/todo/" + todoId)
                        .header("Authorization", token))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    @Test
    public void testRegisterUser() throws Exception {
        String username = "testuser";
        String password = "testpassword";

        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + username + "\",\"password\":\"" + password+"\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        Users users = userRepository.findByUsername(username);


        assertNotNull(users);
        assertEquals(username, users.getUsername());
        assertNotNull(users.getPassword());
        assertNotNull(users.getId());
    }

    @Test
    public void testLoginUser() throws Exception {
        String username = "testuser";
        String password = "testpassword";
        Users user = new Users();
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);

        String responseBody = loginAndGetResponseBody(username, password);
        JSONObject responseJson = new JSONObject(responseBody);
        String token =  responseJson.getString("token");

        assertNotNull(token);
        assertEquals("logged in successfully", responseJson.getString("message"));
    }

    @Test
    public void testAddTodo() throws Exception{
        String username = "testuser";
        String password = "testpassword";
        Users user = new Users();
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
        String userId = user.getId();

        String loginResponseBody = loginAndGetResponseBody(username, password);
        JSONObject loginResponseJson = new JSONObject(loginResponseBody);
        String token =  loginResponseJson.getString("token");

        Todo todo = new Todo();
        todo.setDescription("This is a test todo");
        todo.setCompleted(true);

        String todoResponseBody = postTodoAndGetResponseBody(todo, userId, token);

        JSONObject todoResponseJson = new JSONObject(todoResponseBody);

        assertEquals("todo created", todoResponseJson.getString("message"));
    }

    @Test
    public void testGetTodoById() throws Exception{
        String username = "testuser";
        String password = "testpassword";
        Users user = new Users();
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
        String userId = user.getId();

        String loginResponseBody = loginAndGetResponseBody(username, password);
        JSONObject loginResponseJson = new JSONObject(loginResponseBody);
        String token =  loginResponseJson.getString("token");

        Todo todo = new Todo();
        todo.setDescription("This is a test todo");
        todo.setCompleted(true);

        String todoResponseBody = postTodoAndGetResponseBody(todo, userId, token);
        JSONObject todoResponseJson = new JSONObject(todoResponseBody);
        String todoId = todoResponseJson.getString("todoId");

        String getTodoByIdResponseBody = getTodoByIdResponseBody(userId, token, todoId);
        JSONObject getTodoByIdResponseJson = new JSONObject(getTodoByIdResponseBody);

        assertEquals("This is a test todo", getTodoByIdResponseJson.getString("description"));
        assertTrue(getTodoByIdResponseJson.getBoolean("completed"));
    }

    @Test
    public void testPutTodo() throws Exception{
        String username = "testuser";
        String password = "testpassword";
        Users user = new Users();
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
        String userId = user.getId();

        String loginResponseBody = loginAndGetResponseBody(username, password);
        JSONObject loginResponseJson = new JSONObject(loginResponseBody);
        String token =  loginResponseJson.getString("token");

        Todo todo = new Todo();
        todo.setDescription("This is a test todo");
        todo.setCompleted(true);

        String todoResponseBody = postTodoAndGetResponseBody(todo, userId, token);
        JSONObject todoResponseJson = new JSONObject(todoResponseBody);
        String todoId = todoResponseJson.getString("todoId");

        String todoPutJson = "{ \"description\": \"" + "This is an edited test todo" + "\", \"completed\": " + false + " }";

        String putTodoResponseBody = putTodoResponseBody(userId, todoId, token, todoPutJson);
        JSONObject putTodoResponseJson = new JSONObject(putTodoResponseBody);

        assertEquals("This is an edited test todo", putTodoResponseJson.getString("description"));
        assertFalse(putTodoResponseJson.getBoolean("completed"));


    }
    @Test
    public void testDelete() throws Exception {
        String username = "testuser";
        String password = "testpassword";
        Users user = new Users();
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        userRepository.save(user);
        String userId = user.getId();

        String loginResponseBody = loginAndGetResponseBody(username, password);
        JSONObject loginResponseJson = new JSONObject(loginResponseBody);
        String token =  loginResponseJson.getString("token");

        Todo todo = new Todo();
        todo.setDescription("This is a test todo");
        todo.setCompleted(true);

        String todoResponseBody = postTodoAndGetResponseBody(todo, userId, token);
        JSONObject todoResponseJson = new JSONObject(todoResponseBody);
        String todoId = todoResponseJson.getString("todoId");

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("http://localhost:8080/todos/users/" + userId + "/todo/" + todoId)
                        .header("Authorization", token))
                .andExpect(MockMvcResultMatchers.status().isOk());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("http://localhost:8080/todos/users/" + userId + "/todo/" + todoId)
                        .header("Authorization", token))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
