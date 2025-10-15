package org.zloyleva.utils;

import java.util.HashMap;
import java.util.Map;

public class ReplacementTable {
  public Map<String, String> table = new HashMap<String, String >();

  public ReplacementTable() {
    this.table.put("@alert-login", "");
    this.table.put("@alert-registration", "");
  }

  public void setTableRow(String key, String value) {
    // when you are adding a new key/value - fire the alert
    table.put(key, value);
  }

  public Map<String, String> getTable() {
    return this.table;
  }
}
