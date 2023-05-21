package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbAccess {

  private static String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
  private final String DBMS = "jdbc:mysql";
  private final String SERVER;
  private final String DATABASE;
  private final int PORT;
  private final String USER_ID;
  private final String PASSWORD;

  private Connection conn;

  public DbAccess(String server, String database, int port, String user_id, String pw) {
    SERVER = server;
    DATABASE = database;
    PORT = port;
    USER_ID = user_id;
    PASSWORD = pw;
  }

  public void initConnection() throws DatabaseConnectionException {
    String connectionString = DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE
        + "?user=" + USER_ID + "&password=" + PASSWORD + "&serverTimezone=UTC";

    try {
      conn = DriverManager.getConnection(connectionString);
    } catch (SQLException e) {
      throw new DatabaseConnectionException("Errore di connessione al database", e.getErrorCode());
    }
  }

  public Connection getConnection() {
    return conn;
  }

  public void closeConnection() {
    try {
      conn.close();
    } catch (SQLException e) {
      System.out.println("Impossibile chiudere la connessione");
    }
  }
}