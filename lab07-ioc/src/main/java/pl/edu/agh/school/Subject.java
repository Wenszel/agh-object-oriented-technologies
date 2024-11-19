package pl.edu.agh.school;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import pl.edu.agh.logger.Logger;

public class Subject implements Serializable {

	private static final long serialVersionUID = 5342955138128716653L;

	private final String name;

	private final List<Term> terms = new ArrayList<>();
	private final List<Mark> marks = new ArrayList<>();
	private final List<Mark> semesterMarks = new ArrayList<>();
	private final List<Lesson> lessons = new ArrayList<>();

	private Teacher _teacher;

	public Subject(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void addTerm(Term newTerm) {
		if (!terms.contains(newTerm)) {
			terms.add(newTerm);
			newTerm.setSubject(this);
			Logger.getInstance().log(
					"Added " + newTerm.toString() + " to " + toString());
		}
	}

	public Collection<Term> getSchedule() {
		return terms;
	}

	public void addMark(Mark mark) {
		marks.add(mark);
	}

	public void addSemesterMark(Mark mark) {
		semesterMarks.add(mark);
	}

	public void addLesson(Lesson lesson) {
		lessons.add(lesson);
	}

	public void setTeacher(Teacher teacher) {
		_teacher = teacher;
		_teacher.addSubject(this);
	}

	public Teacher getTeacher() {
		return _teacher;
	}

	public Collection<Mark> getMarks() {
		return Collections.unmodifiableCollection(marks);
	}

	public Collection<Mark> getSemesterMarks() {
		return Collections.unmodifiableCollection(semesterMarks);
	}

	public Collection<Lesson> getLessons() {
		return Collections.unmodifiableCollection(lessons);
	}

	@Override
	public String toString() {
		return "subject " + name;
	}

}
