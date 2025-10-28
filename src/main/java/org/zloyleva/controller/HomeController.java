package org.zloyleva.controller;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.zloyleva.service.UserService;
import org.zloyleva.utils.ReplacementTable;
import org.zloyleva.utils.ViewUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeController implements HttpHandler {
  UserService userService;
  public HomeController(UserService userService) {
    this.userService = userService;
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    ReplacementTable table = new ReplacementTable();
    //    URI: /registration
    //    Method(HTTP): GET

    OutputStream os = exchange.getResponseBody();
    try {
      if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
        String sessionId = null;

        Headers headers = exchange.getRequestHeaders();
        List<String> cookies = headers.get("Cookie");

        if (cookies != null) {
          for (String cookie : cookies) {
//            System.out.println("Element: " + cookie);
            String[] results = cookie.split(";");
            for(String item: results){
              String[] keyValue = item.split("=");
              if (Objects.equals(keyValue[0].trim(), "SESSION")) {
                sessionId = keyValue[1];
                System.out.println("Home page got the sessionId: " + sessionId);
                break;
              }
            }

            if (sessionId != null) {
              break;
            }
          }
        }

        String user = userService.getUserBySessionId(sessionId);
        if (user != null) {
          table.setTableRow("@user-greeting", String.format("""
              <div class="text-end d-flex align-items-center" style="gap: 8px;">
                  <h2 class="text-light">Hello, %s</h2>
                  <form method="POST" action="/logout"><button type="submit" class="btn btn-warning">Logout</button></form>
              </div>
              """, user));
        }
        ViewUtil.sendHTML(exchange, "home.html", table.getTable());
      } else {
        ViewUtil.sendMethodNotAllowed(exchange);
      }
    } catch (Exception e) {
      System.out.println(e);
      throw new RuntimeException(e);
    } finally {
      os.close();
    }
  }
}
