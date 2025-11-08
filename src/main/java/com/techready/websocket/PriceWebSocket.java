package com.techready.websocket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
public class PriceWebSocket {
    private static final Set<Session> sessions = ConcurrentHashMap.newKeySet();

    @OnWebSocketConnect
    public void onConnect(Session session) {
        sessions.add(session);
        System.out.println("🟢 New WebSocket connection: " + session);
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        sessions.remove(session);
        System.out.println("🔴 Connection closed: " + session);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        // Broadcast updated price to all connected clients
        for (Session s : sessions) {
            if (s.isOpen()) {
                s.getRemote().sendString(message);
            }
        }
    }
}
