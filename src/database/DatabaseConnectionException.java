package database;

public class DatabaseConnectionException extends Exception {
  private final int code;
  public DatabaseConnectionException(String msg, int dbcode) {
    super(msg);
    code = dbcode;
  }

  public int getCode() {
    return code;
  }
}
