package pl.edu.agh.iisg.to.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

import pl.edu.agh.iisg.to.executor.QueryExecutor;
import pl.edu.agh.iisg.to.resultset.ResultSetProcessor;

public class Course {
    public static final String TABLE_NAME = "course";
    private static final Logger logger = Logger.getGlobal();
    private final int id;
    private final String name;
    private List<Student> enrolledStudents;
    private boolean isStudentsListDownloaded = false;

    public Course(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    public Course(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getInt(Columns.ID);
        this.name = resultSet.getString(Columns.NAME);
    }

    public static Optional<Course> create(final String name) {
        String insertCourseSql = getInsertCourseSql();
        Object[] args = { name };
        return executeInsertQuery(insertCourseSql, args);
    }

    private static Optional<Course> executeInsertQuery(String insertCourseSql, Object[] args) {
        try {
            int id = QueryExecutor.createAndObtainId(insertCourseSql, args);
            return Course.findById(id);
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    public static Optional<Course> findById(final int id) {
        String findByIdSql = getSelectCourseByIdSql();
        Object[] args = { id };

        return executeFindCourseByIdQuery(findByIdSql, args);
    }

    private static Optional<Course> executeFindCourseByIdQuery(String findByIdSql, Object[] args) {
        try (ResultSet rs = QueryExecutor.read(findByIdSql, args)) {
            return rs.next() ?
                    Optional.of(new Course(
                            rs.getInt(Columns.ID),
                            rs.getString(Columns.NAME)))
                    : Optional.empty();
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    public boolean enrollStudent(final Student student) {
        String enrollStudentSql = "INSERT INTO student_course(student_id, course_id) VALUES(?,?)";
        Object[] args = { student.id(), id };
        try {
            QueryExecutor.createAndObtainId(enrollStudentSql, args);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public List<Student> studentList() throws SQLException{
        String selectStudentListQuery = getSelectStudentListQuery();
        Object[] args = { id };
        ResultSet resultSet = QueryExecutor.read(selectStudentListQuery, args);
        List<Student> resultList = new ArrayList<>();
        ResultSetProcessor.processResultSet(resultSet,
                ()->        resultList.add(createStudentFromResultSet(resultSet)));
        return resultList;
    }

    public List<Student> cachedStudentsList() throws SQLException {
        if (!isStudentsListDownloaded) {
            enrolledStudents = studentList();
        }
        return enrolledStudents;
    }

    private static String getInsertCourseSql() {
        return "INSERT INTO %s(%s) VALUES (?);"
                .formatted(TABLE_NAME, Columns.NAME);
    }

    private static String getSelectCourseByIdSql() {
        return "SELECT * FROM %s WHERE %s = ?".formatted(TABLE_NAME, Columns.ID);
    }

    private static String getSelectStudentListQuery() {
        String columns = Student.Columns.getAllColumnsInSqlFormat() ;

        String joinStudentCourseClause = "JOIN student_course ON student_course.student_id = %s.%s AND student_course.course_id=?"
                .formatted(Student.TABLE_NAME, Student.Columns.ID);

        return String.format(
                "SELECT %s FROM %s %s",
                columns,
                Student.TABLE_NAME,
                joinStudentCourseClause
        );
    }
    private Student createStudentFromResultSet(ResultSet resultSet) {
        try {

            return new Student (
                    resultSet.getInt(Student.Columns.ID),
                    resultSet.getString(Student.Columns.FIRST_NAME),
                    resultSet.getString(Student.Columns.LAST_NAME),
                    resultSet.getInt(Student.Columns.INDEX_NUMBER)
            );
        } catch (SQLException e) {
            logger.warning("Error during creating student from result set");
            return null;
        }
    }


    public int id() {
        return id;
    }

    public String name() {
        return name;
    }

    public static class Columns {
        public static final String ID = "id";
        public static final String NAME = "name";

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Course course = (Course) o;

        if (id != course.id) return false;
        return name.equals(course.name);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        return result;
    }
}
