package pl.edu.agh.school;

import java.io.Serializable;

public class Mark implements Serializable {

    private static final long serialVersionUID = -8070625969786768937L;

    private final MarkValue value;
    private final Person student;

    public Mark(MarkValue value, Person student) {
        this.value = value;
        this.student = student;
    }

    public MarkValue getValue() {
        return value;
    }

    public Person getStudent() {
        return student;
    }
}
