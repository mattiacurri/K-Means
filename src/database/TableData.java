package database;

import com.sun.source.tree.Tree;
import database.TableSchema.Column;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;


public class TableData {

  DbAccess db;


  public TableData(DbAccess db) {
    this.db = db;
  }

  public List<Example> getDistinctTransazioni(String table) throws SQLException, EmptySetException {
		TableSchema tableSchema = new TableSchema(db, table);
    Statement statement = db.getConnection().createStatement();
    ResultSet resultSet = statement.executeQuery("SELECT DISTINCT * FROM " + table + ";");
    List<Example> distinctTransactions = new ArrayList<Example>();
    while (resultSet.next()) {
      Example ex = new Example();
      for (int i = 0; i < tableSchema.getNumberOfAttributes(); i++) {
        if (tableSchema.getColumn(i).isNumber()) {
          ex.add(resultSet.getDouble(i + 1));
        } else {
          ex.add(resultSet.getString(i + 1));
        }
      }
      distinctTransactions.add(ex);
    }
    statement.close();
    resultSet.close();
    if (distinctTransactions.isEmpty()) {
      throw new EmptySetException("Non ci sono transazioni distinte");
    }
    return distinctTransactions;
  }


  public Set<Object> getDistinctColumnValues(String table, Column column) throws SQLException {
    Statement statement = db.getConnection().createStatement();
    ResultSet resultSet = statement.executeQuery("SELECT DISTINCT " + column.getColumnName() + " FROM " + table + ";");
    Set<Object> distinctValues = new TreeSet<>();
    while (resultSet.next()) {
        if (column.isNumber()) {
          distinctValues.add(resultSet.getDouble(column.getColumnName()));
        } else {
          distinctValues.add(resultSet.getString(column.getColumnName()));
        }
    }
    resultSet.close();
    return distinctValues;
  }

  public Object getAggregateColumnValue(String table, Column column, QUERY_TYPE aggregate) throws SQLException, NoValueException {
    Statement statement = db.getConnection().createStatement();
    String query = "SELECT " + aggregate + "(" + column.getColumnName() + ")" + " FROM " + table + ";";
    ResultSet resultSet = statement.executeQuery(query);
    int tupleNO = 0;
    Object aggregateValue = null;
    while (resultSet.next()) {
      tupleNO++;
      aggregateValue = resultSet.getDouble(aggregate + "(" + column.getColumnName() + ")");
    }
    if (tupleNO == 0) {
      throw new NoValueException("Non ci sono valori aggregati");
    }
    return aggregateValue;
  }

}
