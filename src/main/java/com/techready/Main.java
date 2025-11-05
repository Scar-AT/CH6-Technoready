package com.techready;

import com.techready.exception.AppException;
import com.techready.user.User;
import com.techready.offer.Offer;
import com.techready.user.UserService;
import com.techready.offer.OfferService;

import static spark.Spark.*;
import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;
import com.google.gson.Gson;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;


public class Main {
    public static void main(String[] args) {
        port(4567);
        Gson gson = new Gson();
        UserService userService = new UserService();
        OfferService offerService = new OfferService();

        // test route
        get("/hello", (req, res) -> {
            res.type("application/json");
            return gson.toJson("Server running correctly!");
        });

        // MUSTACHE Routes
        get("/", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("title", "Collectibles Store");
            return new ModelAndView(model, "index.mustache");
        }, new MustacheTemplateEngine());

        get("/offers", (req, res) ->{
            return new ModelAndView(new HashMap<>(), "offers.mustache");
        }, new MustacheTemplateEngine());

        get("/offers", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("offers", offerService.getAllOffers());
            return new ModelAndView(model, "offers.mustache");
        }, new   MustacheTemplateEngine());

        post("/offers", (req, res) ->{
            Map<String, Object> model = new HashMap<>();
            String item = req.queryParams("item");
            String price = req.queryParams("price");
            String seller = req.queryParams("seller");

            if (item == null || price == null || price.isBlank() || seller == null || seller.isBlank()) {
                throw new InvalidParameterException("All fields are required!!");

            }

            Offer offer = new Offer(item, price, seller);
            offerService.addOffer(offer);

            Map<String, Object> modelMap = new HashMap<>();
            model.put("offers", offerService.getAllOffers());
            model.put("message", "Offer added: "+ item +" - $" + price +" (by "+ seller +") sucessfully!");
            return new ModelAndView(model, "offers.mustache");
        }, new MustacheTemplateEngine());


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

        // Error handling

        exception(AppException.class, (e, req, res) -> {
            res.status(400);
            res.type("text/html");

            Map<String, Object> model = new HashMap<>();
            model.put("message", e.getMessage());

            String html = new MustacheTemplateEngine().render(
                    new ModelAndView(model, "error.mustache")
            );
            res.body(html);
        });

        // handles all the unexpected exceptions
        exception(Exception.class, (e, req, res) -> {
            res.status(500);
            res.type("text/html");

            Map<String, Object> model = new HashMap<>();
            model.put("message", "An unexpected error occurred. Please try again later.");

            String html = new MustacheTemplateEngine().render(
                    new ModelAndView(model, "error.mustache")
            );
            res.body(html);
        });

    }
}
