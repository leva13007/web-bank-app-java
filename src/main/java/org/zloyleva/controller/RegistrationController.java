package org.zloyleva.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class RegistrationController implements HttpHandler {

  @Override
  public void handle(HttpExchange exchange) throws IOException {

//    URI: /registration
//    Method(HTTP): GET
//    Method(HTTP): POST
//    System.out.println(exchange.getRequestMethod());

    OutputStream os = exchange.getResponseBody();
    try {
      if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
        Path pathHeader = Path.of("src/main/resources/templates/", "header.html");
        Path pathContent = Path.of("src/main/resources/templates/", "registration.html");
        Path pathFooter = Path.of("src/main/resources/templates/", "footer.html");
        String html = Files.readString(pathHeader);
        html += Files.readString(pathContent);
        html += Files.readString(pathFooter);

        byte[] bytes = html.getBytes();
        exchange.sendResponseHeaders(200, bytes.length);
        os.write(bytes);
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

          System.out.println(user);
          System.out.println(password);
          // validations!

          if (user != null && password != null) {
            // redirect to home page
            exchange.getResponseHeaders().add("Location", "http://localhost:5678/");
            exchange.sendResponseHeaders(302, 0);
            //os.close();
          } else {
            Path pathHeader = Path.of("src/main/resources/templates/", "header.html");
            Path pathContent = Path.of("src/main/resources/templates/", "registration.html");
            Path pathFooter = Path.of("src/main/resources/templates/", "footer.html");
            String html = Files.readString(pathHeader);
            html += Files.readString(pathContent);
            html += Files.readString(pathFooter);

            String htmlAlert = """
               <div class="alert alert-danger" role="alert">
                 Got an error during registration process, call the support team)
               </div>
               """;
            html = html.replace("<!-- alert -->", htmlAlert);

            byte[] bytes = html.getBytes();
            exchange.sendResponseHeaders(200, bytes.length);
            os.write(bytes);
          }
        }
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
