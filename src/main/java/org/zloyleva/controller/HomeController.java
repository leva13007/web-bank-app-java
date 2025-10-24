package org.zloyleva.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.zloyleva.utils.ReplacementTable;
import org.zloyleva.utils.ViewUtil;

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
        ViewUtil.sendHTML(exchange, "home.html", table.getTable());
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
