package pl.edu.agh.iisg.to.executor;

import pl.edu.agh.iisg.to.connection.ConnectionProvider;
import pl.edu.agh.iisg.to.query.QueryHelper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Logger;

public final class QueryExecutor {

    private static final Logger LOGGER = Logger.getGlobal();

    private QueryExecutor() {
        throw new UnsupportedOperationException();
    }

    static {
        try {
            LOGGER.info("Creating table Student");
            create("CREATE TABLE IF NOT EXISTS student (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "first_name VARCHAR(50) NOT NULL, " +
                    "last_name VARCHAR(50) NOT NULL, " +
                    "index_number int NOT NULL, " +
                    "UNIQUE (index_number) " +
                    ");");
            LOGGER.info("Creating table Course");
            create("CREATE TABLE IF NOT EXISTS course (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name VARCHAR(50) NOT NULL, " +
                    "UNIQUE (name) " +
                    ");");
            LOGGER.info("Creating table Student_Course");
            create("CREATE TABLE IF NOT EXISTS student_course (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "student_id INT NOT NULL, " +
                    "course_id INT NOT NULL, " +
                    "FOREIGN KEY(student_id) references student (id), " +
                    "FOREIGN KEY(course_id) references course (id), " +
                    "UNIQUE (student_id, course_id)" +
                    ");");
            LOGGER.info("Creating table Grade");
            create("CREATE TABLE IF NOT EXISTS grade (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "grade REAL NOT NULL, " +
                    "student_id INT NOT NULL, " +
                    "course_id INT NOT NULL, " +
                    "FOREIGN KEY(student_id) references student (id), " +
                    "FOREIGN KEY(course_id) references course (id) " +
                    ");");

        } catch (SQLException e) {
            LOGGER.info("Error during create tables: " + e.getMessage());
            throw new RuntimeException("Cannot create tables");
        }
    }

    public static int createAndObtainId(final String insertSql, Object... args) throws SQLException {
        PreparedStatement statement = ConnectionProvider.getConnection().prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
        QueryHelper.mapParams(statement, args);
        statement.execute();
        try (final ResultSet resultSet = statement.getGeneratedKeys()) {
            return readIdFromResultSet(resultSet);
        }
    }

    private static int readIdFromResultSet(final ResultSet resultSet) throws SQLException {
        return resultSet.next() ? resultSet.getInt(1) : -1;
    }

    public static void create(final String insertSql, Object... args) throws SQLException {
        PreparedStatement ps = ConnectionProvider.getConnection().prepareStatement(insertSql);
        QueryHelper.mapParams(ps, args);
        ps.execute();
    }

    public static ResultSet read(final String sql, Object... args) throws SQLException {
        PreparedStatement ps = ConnectionProvider.getConnection().prepareStatement(sql);
        QueryHelper.mapParams(ps, args);
        final ResultSet resultSet = ps.executeQuery();
        LOGGER.info(String.format("Query: %s executed.", sql));
        return resultSet;
    }

    public static void delete(final String sql, Object... args) throws SQLException {
        PreparedStatement ps = ConnectionProvider.getConnection().prepareStatement(sql);
        QueryHelper.mapParams(ps, args);
        ps.executeUpdate();
    }

    public static void executeUpdate(final List<String> sql, List<List<Object>> args) throws SQLException {
        ConnectionProvider.getConnection().setAutoCommit(false);
        for (int i = 0; i < sql.size(); i++) {
            PreparedStatement ps = ConnectionProvider.getConnection().prepareStatement(sql.get(i));
            QueryHelper.mapParams(ps, args.get(i));
            ps.executeUpdate();
            LOGGER.info(String.format("Query: %s executed.", sql.get(i)));
        }
        ConnectionProvider.getConnection().commit();
        ConnectionProvider.getConnection().setAutoCommit(true);
    }
}
