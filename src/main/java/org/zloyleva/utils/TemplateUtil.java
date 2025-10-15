package org.zloyleva.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class TemplateUtil {
  private static final String templatePath = "src/main/resources/templates/";
  public static String getHTML(String templateName, Map<String, String> htmlForReplace) {
    try{
      Path pathHeader = Path.of(TemplateUtil.templatePath, "header.html");
      Path pathContent = Path.of(TemplateUtil.templatePath, templateName);
      Path pathFooter = Path.of(TemplateUtil.templatePath, "footer.html");
      String html = Files.readString(pathHeader);
      html += Files.readString(pathContent);
      html += Files.readString(pathFooter);

      for(String i : htmlForReplace.keySet()) {
        html = html.replace("<!-- " + i + " -->",htmlForReplace.get(i));
      }

      return html;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static byte[] getHTMLBytes(String templateName, Map<String, String> htmlForReplace){
    return TemplateUtil.getHTML(templateName, htmlForReplace).getBytes();
  }
}
