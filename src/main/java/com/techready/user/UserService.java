package com.techready.user;

import java.util.*;

public class UserService {
    private static final Map<Integer, User> users = new HashMap<>();

    // Constructor adds sample data
    public UserService() {
        users.put(1, new User(1, "Scarlett", "scarlett@tts.com"));
        users.put(2, new User(2, "Ana", "ana@tts.com"));
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public User getUserById(int id) {
        return users.get(id);
    }

    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    public void updateUser(int id, User updatedUser) {
        users.put(id, updatedUser);
    }

    public void deleteUser(int id) {
        users.remove(id);
    }

    public boolean userExists(int id) {
        return users.containsKey(id);
    }
}
