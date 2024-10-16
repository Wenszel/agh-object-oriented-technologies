package pl.edu.agh.iisg.to.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import pl.edu.agh.iisg.to.executor.QueryExecutor;
import pl.edu.agh.iisg.to.resultset.ResultSetProcessor;

public class Student {
    public static final String TABLE_NAME = "student";
    private final int id;
    private final String firstName;
    private final String lastName;
    private final int indexNumber;

    public Student(final int id, final String firstName, final String lastName, final int indexNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.indexNumber = indexNumber;
    }

    public Student(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt(Columns.ID);
        this.firstName = resultSet.getString(Columns.FIRST_NAME);
        this.lastName = resultSet.getString(Columns.LAST_NAME);
        this.indexNumber = resultSet.getInt(Columns.INDEX_NUMBER);
    }

    public static Optional<Student> create(final String firstName, final String lastName, final int indexNumber) {
        String insertSql = getInsertStudentSql();
        Object[] args = { firstName, lastName, indexNumber };
        try {
            int id = QueryExecutor.createAndObtainId(insertSql, args);
            return Student.findById(id);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    private static String getInsertStudentSql() {
        return "INSERT INTO %s(%s, %s, %s) VALUES(?, ?, ?)"
                .formatted(TABLE_NAME, Columns.FIRST_NAME, Columns.LAST_NAME, Columns.INDEX_NUMBER);
    }

    public static Optional<Student> findByIndexNumber(final int indexNumber) {
        String sql = getSelectStudentSqlBy(Columns.INDEX_NUMBER);
        return find(indexNumber, sql);
    }

    public static Optional<Student> findById(final int id) {
        String sql = getSelectStudentSqlBy(Columns.ID);
        return find(id, sql);
    }

    private static String getSelectStudentSqlBy(String columnName) {
        return "SELECT * FROM %s WHERE %s = ?"
                .formatted(TABLE_NAME, columnName);
    }

    private static Optional<Student> find(int value, String sql) {
        Object[] args = {value};
        try (ResultSet rs = QueryExecutor.read(sql, args)) {
            return rs.next() ? Optional.of(new Student(rs)) : Optional.empty();
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    private static String getReportGeneratingSql() {
        String columnsToSelect = String.join(", ",
                Course.TABLE_NAME + "." + Course.Columns.ID,
                Course.TABLE_NAME + "." + Course.Columns.NAME,
                "AVG(" + Grade.TABLE_NAME + "." + Grade.Columns.GRADE + ") as avg");

        String courseJoinClause = "JOIN " + Course.TABLE_NAME +
                " ON " + Course.TABLE_NAME + "." + Course.Columns.ID +
                " = " + Grade.TABLE_NAME + "." + Grade.Columns.COURSE_ID;

        String studentWhereClause = "WHERE " + Grade.TABLE_NAME + "." + Grade.Columns.STUDENT_ID + " = ?";

        String groupByClause = "GROUP BY " + Grade.TABLE_NAME + "." + Grade.Columns.COURSE_ID;

        return String.format(
                "SELECT %s FROM %s %s %s %s",
                columnsToSelect,
                Grade.TABLE_NAME,
                courseJoinClause,
                studentWhereClause,
                groupByClause
        );
    }

    private Map<Course, Float> fetchReportData(String reportSql, Object[] args) throws SQLException {
        Map<Course, Float> report = new HashMap<>();
        try (ResultSet rs = QueryExecutor.read(reportSql, args)) {
            ResultSetProcessor.processResultSet(rs, () -> {
                try {
                    appendAverageCourseGrade(rs, report);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return report;
    }

    private void appendAverageCourseGrade(ResultSet rs, Map<Course, Float> report) throws SQLException {
        Course course = new Course(rs);
        Float averageGrade = rs.getFloat("avg");
        report.put(course, averageGrade);
    }

    public int id() {
        return id;
    }

    public String firstName() {
        return firstName;
    }

    public String lastName() {
        return lastName;
    }

    public int indexNumber() {
        return indexNumber;
    }

    public static class Columns {

        public static final String ID = "id";

        public static final String FIRST_NAME = "first_name";

        public static final String LAST_NAME = "last_name";

        public static final String INDEX_NUMBER = "index_number";
        public static String getAllColumnsInSqlFormat() {
            return String.join(", ",
                    Student.TABLE_NAME + "." + Student.Columns.ID,
                    Student.TABLE_NAME + "." + Student.Columns.FIRST_NAME,
                    Student.TABLE_NAME + "." + Student.Columns.LAST_NAME,
                    Student.TABLE_NAME + "." + Student.Columns.INDEX_NUMBER
            );
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Student student = (Student) o;

        if (id != student.id)
            return false;
        if (indexNumber != student.indexNumber)
            return false;
        if (!firstName.equals(student.firstName))
            return false;
        return lastName.equals(student.lastName);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + indexNumber;
        return result;
    }
}
