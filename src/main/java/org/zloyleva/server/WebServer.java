package org.zloyleva.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.zloyleva.controller.RegistrationController;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class WebServer {
  static final int port = 5678;

  public static void main(String[] args) throws IOException {
    HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

    server.createContext("/", new HomeHandler());
    server.createContext("/login", new LoginHandler());
    server.createContext("/registration", new RegistrationController());
    server.createContext("/logout", new HomeHandler());


    System.out.println("Server is running on the port: " + port);
    server.start();
  }

  public static class LoginHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {

      String query = exchange.getRequestURI().getQuery();
      System.out.println(query);

      String htmlAlert = null;
      // check if query is null
      if (query != null) {
        // split the string by &
        String[] result = query.split("&");
        // go through Array string by = In the result will be a pair of key and value
        String user = null;
        String password = null;
        for (String param : result) {
          String[] aaa = param.split("=");
          if (aaa[0].equals("userName") && aaa.length > 1) {
            user = aaa[1];
          }
          if (aaa[0].equals("password") && aaa.length > 1) {
            password = aaa[1];
          }
        }



        // TODO what if we got several error or alert?!

        // check if key "userName" and password exist!
        if (user == null || password == null) {
          // TODO validation error!
           htmlAlert = """
               <div class="alert alert-danger" role="alert">
                 Got an error during registration process, call the support team)
               </div>
               """;
        } else if (false) {
          // TODO check if user is already exist
          // htmlAlert = ""
        } else {
          // TODO if registration success display a success Alert component
          htmlAlert = """
              <div class="alert alert-success" role="alert">
                User successfully created!
              </div>
              """;
        }
        // else display an error Alert component
      }


      exchange.sendResponseHeaders(200, 0);
      OutputStream os = exchange.getResponseBody();

      StringBuilder html = new StringBuilder(
          """
              <!doctype html>
                <html lang="en">
                  <head>
                      <meta charset="UTF-8">
                      <meta name="viewport"
                            content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
                      <meta http-equiv="X-UA-Compatible" content="ie=edge">
                      <title>Bank App | Login form</title>
                      <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet"
                            integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
                  </head>
                <body>
                  <h1>Login form</h1>
              """
      );
      if (htmlAlert != null) html.append(htmlAlert);
      html.append("""
                </body>
          </html>
          """);
      os.write(html.toString().getBytes());
      os.close();
    }
  }

  public static class HomeHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
      exchange.sendResponseHeaders(200, 0);
      OutputStream os = exchange.getResponseBody();
      String html = """
          <html>
            <head></head>
            <body>
              <h1>Hello world!</h1>
            </body>
          </html>
          """;
      os.write(html.getBytes());
      os.close();
    }
  }
}
