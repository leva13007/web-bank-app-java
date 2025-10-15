package org.zloyleva.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.zloyleva.utils.ReplacementTable;
import org.zloyleva.utils.TemplateUtil;

import java.io.IOException;
import java.io.OutputStream;

public class HomeController implements HttpHandler {
  @Override
  public void handle(HttpExchange exchange) throws IOException {
    ReplacementTable table = new ReplacementTable();
    //    URI: /registration
    //    Method(HTTP): GET

    OutputStream os = exchange.getResponseBody();
    try {
      if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
        byte[] bytes = TemplateUtil.getHTMLBytes("home.html", table.getTable());
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
