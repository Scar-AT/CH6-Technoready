package com.techready;

import com.techready.exception.AppException;
import com.techready.exception.InvalidFormDataException;
import com.techready.user.User;
import com.techready.offer.Offer;
import com.techready.user.UserService;
import com.techready.offer.OfferService;

import static spark.Spark.*;

import com.techready.websocket.PriceWebSocket;
import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;
import com.google.gson.Gson;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import spark.embeddedserver.jetty.JettyServerFactory;
import spark.embeddedserver.jetty.JettyHandler;


public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);


    public static void main(String[] args) {
        port(4567);
        staticFiles.location("/public");

        Gson gson = new Gson();
        UserService userService = new UserService();
        OfferService offerService = new OfferService();

        // Register websocket endpoint
        Spark.webSocket("/ws/price", PriceWebSocket.class);
        Spark.init();

        before((req, res) -> {
            res.type(("text/html"));
        });

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

        get("/offers", (req, res) -> {
            String min = req.queryParams("min");
            String max = req.queryParams("max");
            String seller = req.queryParams("seller");
            String item = req.queryParams("item");

            Double minVal = null;
            Double maxVal = null;
            String errorMsg = null;

            try {
                if (min != null && !min.isBlank()) {
                    minVal = Double.parseDouble(min);
                }
            } catch (NumberFormatException e) {
                errorMsg = "Invalid minimum price format.";
            }

            try {
                if (max != null && !max.isBlank()) {
                    maxVal = Double.parseDouble(max);
                }
            } catch (NumberFormatException e) {
                errorMsg = "Invalid maximum price format.";
            }

            // 🔹 Use filters if any value was given, otherwise list all
            List<Offer> filteredOffers = offerService.filterOffers(minVal, maxVal, seller, item);
            Map<String, Object> model = new HashMap<>();

            model.put("offers", filteredOffers);
            model.put("message", errorMsg != null ? errorMsg : "Showing filtered offers");

            // 🔹 Persist filter values in form
            model.put("itemFilter", item);
            model.put("minFilter", min);
            model.put("maxFilter", max);
            model.put("sellerFilter", seller);

            return new ModelAndView(model, "offers.mustache");
        }, new MustacheTemplateEngine());



        post("/offers", (req, res) ->{
            Map<String, Object> model = new HashMap<>();
            String item = req.queryParams("item");
            String price = req.queryParams("price");
            String seller = req.queryParams("seller");
            String simulateError = req.queryParams("simulateError");

            logger.info("Recieved offer submission: item={}, price={}, seller={}", item, price, seller);


            // manual error trigger (for tests)
            if ("true".equalsIgnoreCase(simulateError)) {
                throw new InvalidFormDataException("Simulated backend error triggered!");
            }

            // deeper valdation
            if (!price.matches("\\d+(\\.\\d{1,2})?")){
                throw new InvalidFormDataException("Price must be a valid number!");
            }

            // dupe items
            boolean exists = offerService.getAllOffers()
                    .stream()
                    .anyMatch(o -> o.getItem().equalsIgnoreCase(item)
                            && o.getSeller().equalsIgnoreCase(seller));
            if (exists) {
                throw new InvalidFormDataException(
                        "An offer for '" + item + "' from seller '" + seller + "' already exists!"
                );
            }

            /*if (item == null || price == null || price.isBlank() || seller == null || seller.isBlank()) {
                throw new InvalidParameterException("All fields are required!!");

            }*/

            Offer offer = new Offer(item, price, seller);
            offerService.addOffer(offer);
            logger.info("Offer successfully added! {}", item);

            Map<String, Object> modelMap = new HashMap<>();
            model.put("offers", offerService.getAllOffers());
            model.put("message", "Offer added: "+ item +" - $" + price +" (by "+ seller +") successfully!");
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

        get("/users-view", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("users", userService.getAllUsers());
            return new ModelAndView(model, "users-view.mustache");
        }, new MustacheTemplateEngine());



        System.out.println("🚀 Spark server running at http://localhost:4567/hello");

        // Error handling

        /*get("/test-error", (req, res) -> {
            throw new AppException("This is a test error!");
        });
*/
        exception(AppException.class, (e, req, res) -> {
            logger.warn("App error {}", e.getMessage());
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
            logger.error("Unexpected server error", e);
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
