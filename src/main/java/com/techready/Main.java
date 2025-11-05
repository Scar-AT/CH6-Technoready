package com.techready;

import com.techready.user.User;
import com.techready.user.UserService;

import static spark.Spark.*;
import com.google.gson.Gson;

public class Main {
    public static void main(String[] args) {
        port(4567);
        Gson gson = new Gson();
        UserService userService = new UserService();

        // test route
        get("/hello", (req, res) -> {
            res.type("application/json");
            return gson.toJson("Server running correctly!");
        });


//      Routes
//          Retrieve all Users
        get("/users", (req, res) ->{
            res.type("application/json");
            return gson.toJson(userService.getAllUsers());
        });

        // Retrieve user by ID
        get("/users/:id", (req, res)->{
            res.type("application/json");
            int id = Integer.parseInt(req.params(":id"));
            User user = userService.getUserById(id);

            if(user == null){
                res.status(404);
                return gson.toJson("USER NOT FOUND");
            }
            return gson.toJson(user);
        });

        // POST -> add a new user
        post("/users/:id", (req, res)->{
            res.type( "application/json");
            int id = Integer.parseInt(req.params(":id"));
            User newUser = gson.fromJson(req.body(), User.class);

            newUser.setId(id);
            userService.addUser(newUser);

            res.status(201);


            return gson.toJson("USER ADDED!");
        });


//      PUT -> Edit an user
        put("/users/:id", (req, res)->{
            res.type( "application/json");
            int id = Integer.parseInt(req.params(":id"));

            User updateUser = gson.fromJson(req.body(), User.class);

            updateUser.setId(id);
            userService.updateUser(id, updateUser);

            return gson.toJson("USER UPDATED!");

        });

        // OPTIONS -> Check user existence
        options("/users/:id", (req, res)->{
            res.type( "application/json");

            int id = Integer.parseInt(req.params(":id"));
            boolean exists = userService.userExists(id);

            return  gson.toJson("USER EXISTS "+ exists);
        });

        // DELETE -> Delete an user
        delete("/users/:id", (req, res)->{
            res.type( "application/json");
            int id = Integer.parseInt(req.params(":id"));

            userService.deleteUser(id);

            return gson.toJson("USER DELETED!");
        });

        System.out.println("🚀 Spark server running at http://localhost:4567/hello");
    }
}
