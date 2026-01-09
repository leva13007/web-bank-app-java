package org.zloyleva.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.util.List;
import java.util.Objects;

import org.zloyleva.service.UserService;
import org.zloyleva.utils.ReplacementTable;
import org.zloyleva.utils.ViewUtil;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class LoginController  implements HttpHandler  {
  UserService userService;
  public LoginController(UserService userService) {
    this.userService = userService;
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    //    URI: /login
    //    Method(HTTP): GET
    //    Method(HTTP): POST
    //    System.out.println(exchange.getRequestMethod());

    OutputStream os = exchange.getResponseBody();
    try {
      if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
        ReplacementTable table = new ReplacementTable();


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
        if (user != null && sessionId != null) {
          // redirect to Home page
          ViewUtil.sendRedirect(exchange, sessionId);
        }

        table.setTableRow("@page-title", "Bank App | Login page");
        ViewUtil.sendHTML(exchange, "login.html", table.getTable());
      } else if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {

        URI uri = exchange.getRequestURI();

        if (Objects.equals(uri.getPath(), "/logout")) {
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
                  System.out.println("Logout page got the sessionId: " + sessionId);
                  break;
                }
              }

              if (sessionId != null) {
                break;
              }
            }
          }

          // remove sessionId
          userService.logout(sessionId);
          sessionId = null;
          // and redirect to login page
          ViewUtil.sendRedirect(exchange, sessionId);
        } else {
          BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
          String line = reader.readLine();

          if (line != null) {
            String[] result = line.split("&");
            String user = null;
            String password = null;
            for (String param : result) {
              String[] aaa = param.split("=");
              if (aaa[0].equals("user") && aaa.length > 1) {
                user = aaa[1];
              }
              if (aaa[0].equals("password") && aaa.length > 1) {
                password = aaa[1];
              }
            }

//          System.out.println(user);
//          System.out.println(password);
            // validations!

            String sessionId = userService.login(user, password);
            System.out.println("Login create a new sessionId: " + sessionId);
            if (sessionId != null) {
              ViewUtil.sendRedirect(exchange, sessionId);
            } else {
              ReplacementTable table = new ReplacementTable();
              table.setTableRow("@alert-login", """
               <div class="alert alert-danger" role="alert" data-testid="error-login">
                 Got an error during login process, call the support team
               </div>
               """);
              table.setTableRow("@page-title", "Bank App | Login page");
              ViewUtil.sendHTML(exchange, "login.html", table.getTable());
            }
          }
        }
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
