package pl.edu.agh.iisg.to.resultset;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ResultSetProcessor {
    public static void processResultSet(ResultSet resultSet, Runnable e) throws SQLException {
        while(resultSet.next()) {
            e.run();
        }
    }
}
