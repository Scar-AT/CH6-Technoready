package com.techready;

import static spark.Spark.*;
import com.google.gson.Gson;

public class Main {
    public static void main(String[] args) {
        port(4567);
        Gson gson = new Gson();

        get("/hello", (req, res) -> {
            res.type("application/json");
            return gson.toJson("Server running correctly!");
        });

        System.out.println("🚀 Spark server running at http://localhost:4567/hello");
    }
}
