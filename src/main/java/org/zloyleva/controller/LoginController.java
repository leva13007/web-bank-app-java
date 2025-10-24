package org.zloyleva.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.zloyleva.utils.ReplacementTable;
import org.zloyleva.utils.ViewUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class LoginController  implements HttpHandler  {

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
        ViewUtil.sendHTML(exchange, "login.html", table.getTable());
      } else if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {

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

          if (user != null && password != null) {
            ViewUtil.sendRedirect(exchange);
          } else {
            ReplacementTable table = new ReplacementTable();
            table.setTableRow("@alert-login", """
               <div class="alert alert-danger" role="alert">
                 Got an error during login process, call the support team)// After replace
               </div>
               """);
            ViewUtil.sendHTML(exchange, "login.html", table.getTable());
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
