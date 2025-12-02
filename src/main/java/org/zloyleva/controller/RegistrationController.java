package org.zloyleva.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.zloyleva.service.UserService;
import org.zloyleva.utils.ReplacementTable;
import org.zloyleva.utils.ViewUtil;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class RegistrationController implements HttpHandler {
  UserService userService;
  public RegistrationController(UserService userService) {
    this.userService = userService;
  }
  @Override
  public void handle(HttpExchange exchange) throws IOException {

//    URI: /registration
//    Method(HTTP): GET
//    Method(HTTP): POST
//    System.out.println(exchange.getRequestMethod());

    OutputStream os = exchange.getResponseBody();
    try {
      if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
        ReplacementTable table = new ReplacementTable();
        table.setTableRow("@page-title", "Bank App | Registration page");
        ViewUtil.sendHTML(exchange, "registration.html", table.getTable());
      } else if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        String line = reader.readLine();

        if (line != null) {
          // Need to move it to the validator
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

          System.out.println(user);
          System.out.println(password);
          // validations!

          String sessionId = this.userService.registration(user, password);
          System.out.println(sessionId);
          if (sessionId != null) {
            ViewUtil.sendRedirect(exchange, sessionId);
          } else {
            ReplacementTable table = new ReplacementTable();
            table.setTableRow("@alert-registration", """
               <div class="alert alert-danger" role="alert" data-testid="error-registration">
                 Got an error during registration process, call the support team
               </div>
               """);
            table.setTableRow("@page-title", "Bank App | Registration page");
            ViewUtil.sendHTML(exchange, "registration.html", table.getTable());
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
