package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TableSchema {
  private DbAccess db;

  public class Column {
    private String name;
    private String type;

    Column(String name, String type) {
      this.name = name;
      this.type = type;
    }

    public String getColumnName() {
      return name;
    }

    public boolean isNumber() {
      return type.equals("number");
    }

    public String toString() {
      return name + ":" + type;
    }
  }

  List<Column> tableSchema = new ArrayList<>();

  public TableSchema(DbAccess db, String tableName) throws SQLException {
    this.db = db;
    HashMap<String, String> mapSQL_JAVATypes = new HashMap<>();
    //http://java.sun.com/j2se/1.3/docs/guide/jdbc/getstart/mapping.html
    mapSQL_JAVATypes.put("CHAR", "string");
    mapSQL_JAVATypes.put("VARCHAR", "string");
    mapSQL_JAVATypes.put("LONGVARCHAR", "string");
    mapSQL_JAVATypes.put("BIT", "string");
    mapSQL_JAVATypes.put("SHORT", "number");
    mapSQL_JAVATypes.put("INT", "number");
    mapSQL_JAVATypes.put("LONG", "number");
    mapSQL_JAVATypes.put("FLOAT", "number");
    mapSQL_JAVATypes.put("DOUBLE", "number");


    Connection conn = db.getConnection();
    Statement st = conn.createStatement();
    ResultSet rs = st.executeQuery("SELECT * FROM " + tableName + " WHERE 1=0;");
    ResultSetMetaData md = rs.getMetaData();

    for (int i=1; i<=md.getColumnCount(); i++) {
      if (mapSQL_JAVATypes.containsKey(md.getColumnTypeName(i)))
        tableSchema.add(new Column(md.getColumnName(i), mapSQL_JAVATypes.get(md.getColumnTypeName(i))));
    }
    st.close();
    rs.close();
  }


  public int getNumberOfAttributes() {
    return tableSchema.size();
  }

  public Column getColumn(int index) {
    return tableSchema.get(index);
  }


}