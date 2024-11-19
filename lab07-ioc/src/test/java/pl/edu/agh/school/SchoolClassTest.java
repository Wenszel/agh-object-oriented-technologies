package pl.edu.agh.school;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class SchoolClassTest {

	private static final String CLASS_PROFILE = "French";
	private static final String CLASS_NAME = "3D";
	@Mock
	Student student1;
	@Mock
	Student student2;
	@Mock
	Subject subject1;
	@Mock
	Subject subject2;
	@Mock
	Term term1;
	@Mock
	Term term2;

	SchoolClass sclass;

	@BeforeEach
	public void setUp() throws Exception {
		sclass = new SchoolClass(CLASS_NAME, CLASS_PROFILE);
		List<Term> terms1 = new ArrayList<Term>();
		terms1.add(term1);
		List<Term> terms2 = new ArrayList<Term>();
		terms2.add(term2);
		lenient().when(subject1.getSchedule()).thenReturn(terms1);
		lenient().when(subject2.getSchedule()).thenReturn(terms2);
	}

	@Test
	public void testNameAndProfile() {
		assertEquals(CLASS_NAME, sclass.getName());
		assertEquals(CLASS_PROFILE, sclass.getProfile());
	}

	@Test
	public void testMeetSearchCriteria() {
		assertTrue(sclass.meetSearchCriteria(CLASS_NAME, CLASS_PROFILE));
	}

	@Test
	public void testMeetSearchCriteriaDoesNotMeet() {
		assertFalse(sclass.meetSearchCriteria("some not existing class",
				"some profile"));
	}

	@Test
	public void testAddSubject() {
		Collection<Subject> subjects = sclass.getSubjects();
		assertEquals(0, subjects.size());
		sclass.addSubject(subject1);
		sclass.addSubject(subject2);
		subjects = sclass.getSubjects();
		assertTrue(subjects.contains(subject1));
		assertTrue(subjects.contains(subject2));
	}

	@Test
	public void testAddStudent() {
		Collection<Student> students = sclass.getStudents();
		assertEquals(0, students.size());
		sclass.addStudent(student1);
		sclass.addStudent(student2);
		students = sclass.getStudents();
		assertTrue(students.contains(student1));
		assertTrue(students.contains(student2));
	}

	@Test
	public void testSchedule() {
		sclass.addSubject(subject1);
		sclass.addSubject(subject2);
		Collection<Term> schedule = sclass.getSchedule();
		assertEquals(2, schedule.size());
		assertTrue(schedule.contains(term1));
		assertTrue(schedule.contains(term2));
	}
}
