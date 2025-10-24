package org.zloyleva.utils;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

public class ViewUtil {
  public static void sendHTML (HttpExchange exchange, String templateName, Map<String, String> htmlForReplace) throws IOException {
    byte[] bytes = TemplateUtil.getHTMLBytes(templateName, htmlForReplace);
    exchange.sendResponseHeaders(200, bytes.length);
    try (OutputStream os = exchange.getResponseBody()) {
      os.write(bytes);
    }
  }

  public static void sendRedirect (HttpExchange exchange, String sessionId) throws IOException {
    exchange.getResponseHeaders().add("Location", "http://localhost:5678/");
    exchange.getResponseHeaders().add("Set-Cookie", "SESSION=" + sessionId);
    exchange.sendResponseHeaders(302, -1);
  }

  public static void sendMethodNotAllowed (HttpExchange exchange) throws IOException {
    String response = "405 Method Not Allowed";
    byte[] bytes = response.getBytes();
    exchange.sendResponseHeaders(405, bytes.length);
    try (OutputStream os = exchange.getResponseBody()) {
      os.write(response.getBytes());
    }
  }
}
