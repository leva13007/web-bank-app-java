package org.zloyleva.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class HomeController implements HttpHandler {
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    //    URI: /registration
    //    Method(HTTP): GET

    OutputStream os = exchange.getResponseBody();
    try {
      if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
        Path pathHeader = Path.of("src/main/resources/templates/", "header.html");
        Path pathContent = Path.of("src/main/resources/templates/", "home.html");
        Path pathFooter = Path.of("src/main/resources/templates/", "footer.html");
        String html = Files.readString(pathHeader);
        html += Files.readString(pathContent);
        html += Files.readString(pathFooter);

        byte[] bytes = html.getBytes();
        exchange.sendResponseHeaders(200, bytes.length);
        os.write(bytes);
      } else {
        String response = "405 Method Not Allowed";
        byte[] bytes = response.getBytes();
        exchange.sendResponseHeaders(405, bytes.length);
        os.write(response.getBytes());
      }
    } catch (Exception e) {
      System.out.println(e);
      throw new RuntimeException(e);
    } finally {
      os.close();
    }
  }
}
