package org.zloyleva.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;

public class ViewUtil {
  public static void sendHTML (HttpExchange exchange, String templateName, Map<String, String> htmlForReplace) throws IOException {
    byte[] bytes = TemplateUtil.getHTMLBytes(templateName, htmlForReplace);
    exchange.sendResponseHeaders(200, bytes.length);
    try (OutputStream os = exchange.getResponseBody()) {
      os.write(bytes);
    }
  }

  // Redirect to Home page
  public static void sendRedirect (HttpExchange exchange, String sessionId) throws IOException {
    exchange.getResponseHeaders().add("Location", "http://localhost:5678/");
    if (sessionId != null) {
      exchange.getResponseHeaders().add("Set-Cookie", "SESSION=" + sessionId);
    } else {
      exchange.getResponseHeaders().add("Set-Cookie", "SESSION=; Max-Age=0;");
    }

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
