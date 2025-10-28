package org.zloyleva.server;

import com.sun.net.httpserver.HttpServer;
import org.zloyleva.controller.HomeController;
import org.zloyleva.controller.LoginController;
import org.zloyleva.controller.RegistrationController;
import org.zloyleva.service.UserService;

import java.io.IOException;
import java.net.InetSocketAddress;

public class WebServer {
  static final int port = 5678;

  public static void main(String[] args) throws IOException {
    HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
    UserService userService = new UserService();

    server.createContext("/", new HomeController(userService));
    server.createContext("/login", new LoginController(userService)); // only for guests
    server.createContext("/registration", new RegistrationController(userService)); // only for guests
    server.createContext("/logout", new LoginController(userService));


    System.out.println("Server is running on the port: " + port);
    server.start();
  }
}
