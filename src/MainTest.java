import data.Data;
import data.OutOfRangeSampleSize;
import database.DatabaseConnectionException;
import database.EmptySetException;
import database.NoValueException;
import keyboardinput.Keyboard;
import mining.KMeansMiner;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

class MainTest {

  private static boolean getInput(String msg) {
    String choice;
    while (true) {
      System.out.print(msg);
      choice = Keyboard.readString().toLowerCase();
      if (choice.charAt(0) == 'n' && choice.length() == 1) {
        return false;
      } else if (choice.charAt(0) == 'y' && choice.length() == 1) {
        return true;
      }
      System.out.println("Valore non valido, ritentare");
    }
  }

  private static Data getDBInfo() {
    boolean isNotValid = true;
    Data data = null;
    String server = null, database = null, user = null, password = null, table = null;
    int port = -1;
    while (isNotValid) {
      try {
        if (server == null) {
          System.out.print("Inserire il server: ");
          server = Keyboard.readString();
        }
        if (port == -1) {
          System.out.print("Inserire la porta: ");
          port = Keyboard.readInt();
        }
        if (database == null) {
          System.out.print("Inserire il database: ");
          database = Keyboard.readString();
        }
        if (user == null) {
          System.out.print("Inserire l'utente: ");
          user = Keyboard.readString();
        }
        if (password == null) {
          System.out.print("Inserire la password: ");
          password = Keyboard.readString();
        }
        if (table == null) {
          System.out.print("Inserire il nome della tabella: ");
          table = Keyboard.readString();
        }
        data = new Data(server, database, port, user, password, table);
        System.out.println("Connessione al database riuscita!");
        isNotValid = false;
      } catch (DatabaseConnectionException e) {
        switch (e.getCode()) {
          case 0 -> {
            if (getInput("Server e/o porta non validi. Riprovare (y) o tornare al menu (n)?")) {
              server = null;
              port = -1;
            } else {
              isNotValid = false;
            }
          }
          case 1049 -> {
            if (getInput("Database non esistente. Riprovare (y) o tornare al menu (n)?")) {
              database = null;
            } else {
              isNotValid = false;
            }
          }
          case 1045 -> {
            if (getInput("Nome utente e/o password errati. Riprovare (y) o tornare al menu (n)?")) {
              user = null;
              password = null;
            } else {
              isNotValid = false;
            }
          }
        }
      } catch (EmptySetException | NoValueException e) {
        System.err.println("Non ho trovato dati.\n ERRORE: " + e.getMessage() + "\n Uscita dal programma...");
        System.exit(0);
      } catch (SQLException e) {
        if (e.getErrorCode() == 1146) {
          if (getInput("La tabella inserita non esiste. Riprovare (y) o tornare al menu (n)?")) {
            table = null;
          } else {
            isNotValid = false;
          }
        } else {
          System.err.println("Query errata: " + e.getMessage() + "\n Uscita dal programma...");
          isNotValid = false;
        }
      }
    }
    return data;
  }

  public static void main(String[] args) {
    boolean run = true;
    while (run) {
      System.out.println("KMeans");
      System.out.println("1. Eseguire clustering");
      System.out.println("2. Esci");
      System.out.print("Inserisci scelta: ");
      int input = Keyboard.readInt();
      Data data = null;
      KMeansMiner kmeans = null;
      switch (input) {
        case 1 -> {
          data = getDBInfo();
          if (data == null) {
            break;
          }
          int numIter = 0;
          boolean isNotValid;
          do {
            isNotValid = false;
            do {
              System.out.print("Inserisci k: ");
              int k = Keyboard.readInt();
              try {
                kmeans = new KMeansMiner(k);
                numIter = kmeans.kmeans(data);
                isNotValid = false;
              } catch (OutOfRangeSampleSize e) {
                System.out.println(e.getMessage());
                isNotValid = true;
              }
            } while (isNotValid);

            System.out.println("Numero di Iterazioni:" + numIter);
            System.out.println(kmeans.getC().toString(data));
            if (getInput("Continuare sullo stesso dataset? (y/n) ")) {
              isNotValid = true;
            } else {
              System.out.println("Torno al menu");
              isNotValid = false;
            }
          } while (isNotValid);
        }
        case 2 -> System.exit(0);
        default -> System.out.println("Scelta non valida");
      }
    }
  }

}



