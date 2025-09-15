package org.zloyleve.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class WebServer {
  static final int port = 5678;

  public static void main(String[] args) throws IOException {
    HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

    server.createContext("/", new HomeHandler());

    System.out.println("Server is running on the port: " + port);
    server.start();
  }

  public static class HomeHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
      exchange.sendResponseHeaders(200, 0);
      OutputStream os = exchange.getResponseBody();
      os.write("<html><head></head><body><h1>Hello world!</h1></body></html>".getBytes());
      os.close();
    }
  }
}
