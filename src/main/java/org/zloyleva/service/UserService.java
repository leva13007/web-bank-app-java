package org.zloyleva.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserService {
  private final Map<String, String> users= new HashMap<>();
  private final Map<String, String> sessionIds= new HashMap<>();
  public String  login(String login, String password) {
    if (users.containsKey(login) || users.containsValue(password)) {
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

  public void logout () {}
}
