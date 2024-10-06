package pl.edu.agh.iisg.to.model;

import pl.edu.agh.iisg.to.executor.QueryExecutor;

import java.sql.SQLException;

public class Grade {

    public static final String TABLE_NAME = "grade";

    private final int id;

    private final float grade;

    public Grade(final int id, final float grade) {
        this.id = id;
        this.grade = grade;
    }

    public static boolean gradeStudent(final Student student, final Course course, final float grade) {
        String insertGradeSql = getInsertGradeSql();
        Object[] args = { student.id(), course.id(), grade };
        return executeInsertQuery(insertGradeSql, args);
    }

    private static String getInsertGradeSql() {
        return "INSERT INTO %s(%s, %s, %s) VALUES(?, ?, ?)"
                .formatted(TABLE_NAME, Columns.STUDENT_ID, Columns.COURSE_ID, Columns.GRADE);
    }

    private static boolean executeInsertQuery(String insertQuery, Object[] args) {
        try {
            QueryExecutor.createAndObtainId(insertQuery, args);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public int id() {
        return id;
    }

    public float grade() {
        return grade;
    }

    public static class Columns {
        public static final String ID = "id";
        public static final String GRADE = "grade";
        public static final String STUDENT_ID = "student_id";
        public static final String COURSE_ID = "course_id";
    }

}
