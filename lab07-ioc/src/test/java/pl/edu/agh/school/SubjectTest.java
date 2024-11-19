package pl.edu.agh.school;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SubjectTest {

	private static final String SUBJECT_NAME = "Computer Science";
	@Mock Term term1;
	@Mock Term term2;
	@Mock Teacher teacher; 
	@Mock Lesson lesson1;
	@Mock Lesson lesson2;
	@Mock Mark mark1;
	@Mock Mark mark2;
	
	
	Subject subject;
	
	@BeforeEach
	public void setUp() throws Exception {
		subject=new Subject(SUBJECT_NAME);
	}

	@Test
	public void testGetName() {
		assertEquals(SUBJECT_NAME,subject.getName());
	}

	@Test
	public void testTeacher() {
		assertNull(subject.getTeacher());
		subject.setTeacher(teacher);
		assertEquals(teacher,subject.getTeacher());
	}
	
	@Test
	public void testAddTerm() {
		Collection<Term> terms=subject.getSchedule();
		subject.addTerm(term1);
		verify(term1).setSubject(subject);
		subject.addTerm(term2);
		verify(term2).setSubject(subject);
		assertTrue(terms.contains(term1));
		assertTrue(terms.contains(term2));
	}
	
	@Test
	public void testAddMark() {	
		Collection<Mark> marks=subject.getMarks();
		assertEquals(0,marks.size());
		subject.addMark(mark1);
		subject.addMark(mark2);
		marks=subject.getMarks();
		assertTrue(marks.contains(mark1));
		assertTrue(marks.contains(mark2));
	}
	
	@Test
	public void testAddSemesterMark() {	
		Collection<Mark> marks=subject.getSemesterMarks();
		assertEquals(0,marks.size());
		subject.addSemesterMark(mark1);
		subject.addSemesterMark(mark2);
		marks=subject.getSemesterMarks();
		assertTrue(marks.contains(mark1));
		assertTrue(marks.contains(mark2));
	}
	
	@Test
	public void testAddLesson() {
		Collection<Lesson> lessons=subject.getLessons();
		assertEquals(0,lessons.size());
		subject.addLesson(lesson1);
		subject.addLesson(lesson2);
		lessons=subject.getLessons();
		assertTrue(lessons.contains(lesson1));
		assertTrue(lessons.contains(lesson2));
	}
}
