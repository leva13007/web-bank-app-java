package org.zloyleva.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserService {
  public final Map<String, String> users= new HashMap<>();
  public final Map<String, String> sessionIds= new HashMap<>();

  public UserService() {
//    users.put("Oleh", "123");
//    sessionIds.put("4db27675-87a0-4cf8-9290-9379dcad1534", "Oleh");
  }

  public String  login(String login, String password) {
    if (!users.containsKey(login) || !users.containsValue(password)) {
      return null;
    }
    String sessionID = UUID.randomUUID().toString();
    sessionIds.put(sessionID, login);
    return sessionID;
  }

  public String registration(String login, String password) {
    if (users.containsKey(login)) {
      return null;
    } else {
      users.put(login, password);
      String sessionID = UUID.randomUUID().toString();
      sessionIds.put(sessionID, login);
      return sessionID;
    }
  }

  public void logout (String sessionId) {
    sessionIds.remove(sessionId);
  }

  public String getUserBySessionId (String sessionId) {
    if (sessionIds.containsKey(sessionId)) {
      return sessionIds.get(sessionId);
    }
    return null;
  }
}
