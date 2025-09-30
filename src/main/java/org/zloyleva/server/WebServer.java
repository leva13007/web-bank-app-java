package org.zloyleva.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.zloyleva.controller.LoginController;
import org.zloyleva.controller.RegistrationController;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class WebServer {
  static final int port = 5678;

  public static void main(String[] args) throws IOException {
    HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

    server.createContext("/", new HomeHandler());
    server.createContext("/login", new LoginController());
    server.createContext("/registration", new RegistrationController());
    server.createContext("/logout", new HomeHandler());


    System.out.println("Server is running on the port: " + port);
    server.start();
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
