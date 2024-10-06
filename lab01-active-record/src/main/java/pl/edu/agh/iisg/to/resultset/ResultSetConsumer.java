package pl.edu.agh.iisg.to.resultset;

import java.sql.SQLException;

@FunctionalInterface
public interface ResultSetConsumer {
    void process() throws SQLException;
}
