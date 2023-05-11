# todo-app-springboot
 Software Engineer Assessment Test
 
## Useful links
-[Docker hub](https://hub.docker.com/r/kasra96/todo-app-assessment)

-[API documentation](https://app.swaggerhub.com/apis/kasramnt96/todo-app/1.0.0)

## prerequisites
1. Java 17
2. Springboot 2.7.11
3. Maven
4. Mongo db
5. Docker Daemon
6. Postman

## Dependencies
1. spring-boot-starter-data-mongodb
2. spring-boot-starter-security
3. spring-boot-starter-web
4. jjwt
5. jaxb-api
6. spring-boot-devtools
7. spring-boot-starter-test
8. spring-security-test

## Building Instructions
Move to the root directory and issue the following at the command line:
```bash
mvn package
```
## Running test suits
To run all test suits at once you can type the following at the command line:
```bash
mvn test
```
Since test cases are developed in a decoupled manner, you can run a specific test metod only by typing the following:
```bash
mvn test -Dtest=TodoAppTest#testRegisterUser
```
for example, the mentioned line will execute testRegisterUser in the test class.

## Run with dependencies
### To run with docker:
1. Go to the provided docker hub link

2. pull the image
```bash
docker pull kasra96/todo-app-assessment:v2.0
```
3. run the container:
```bash
docker run -p 8080:8080 --name example-name kasra96/todo-app-assessment
```
### To run locally:
1. Navigate to the directory where you want to store the repository and clone the project:
```bash
git clone https://github.com/KasraMontakhabi/todo-app-springboot.git
```
2. Open the project using an IDE and type the following in the command line:
```bash
mvn clean spring-boot:run
```
## Testing with the Postman
All endpoints are documented in [swagger](https://app.swaggerhub.com/apis/kasramnt96/todo-app/1.0.0). Please note that the base URL is http://localhost:8080.
1. User registration: To register a new user, send a POST request to the registration endpoint by appending it to the base URL. The request body should be formatted like the example in Swagger. Once successful, the response body will contain a message indicating that the user has been created along with their user ID.

2. User sign-in: To sign in, send a POST request with a body formatted like the user registration example. The response body will contain a generated token. This token will be needed for authorization and testing other methods, so be sure to copy it.

3. Posting a todo: To post a new todo, first paste the copied token to the header of the request. Then provide the user ID in the URL and format the request body like the example in Swagger. Once successful, the response body will contain the ID of the new todo, the user ID, and a success message.

4. Getting a todo: To retrieve a specific todo, first add the copied token to the header of the request. Then provide both the user ID and todo ID in the URL, which can be obtained from the response body of the POST request. Send a GET request to retrieve the todo, which will be returned in the response body. For more information, please refer to the Swagger documentation.

5. Getting all todos: To get all todos for a user, include the copied token in the request header and send a GET request to the todo endpoint with the user ID in the URL. The response body will include a list of all the user's todos.

6. Edit a todo: To edit a todo, include the copied token in the request header, and provide the user ID and todo ID in the URL. Send a PUT request with the request body in the same format as the example in Swagger. The response body will show the edited todo.

7. Delete a todo: To delete a todo, include the copied token in the request header, and provide the user ID and todo ID in the URL. Send a DELETE request to delete the todo. You can check if the todo was deleted by sending a GET request for the same todo ID and user ID.
