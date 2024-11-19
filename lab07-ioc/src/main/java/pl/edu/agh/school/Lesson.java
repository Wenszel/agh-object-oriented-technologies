package pl.edu.agh.school;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class Lesson implements Serializable {

    private static final long serialVersionUID = -1645504644575582072L;

    private final Subject subject;
    private final Calendar date;

    private final ArrayList<Student> presentStudents = new ArrayList<>();

    public Lesson(Subject subject) {
        this.subject = subject;
        this.date = Calendar.getInstance();
    }

    public Subject getSubject() {
        return subject;
    }

    public Calendar getDate() {
        return date;
    }

    public void registerPresence(Student student) {
        if (!presentStudents.contains(student)) {
            presentStudents.add(student);
        }
    }

    public boolean isPresent(Student student) {
        return presentStudents.contains(student);
    }

}
