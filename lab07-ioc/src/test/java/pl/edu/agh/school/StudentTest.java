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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class StudentTest {

	private static final String STUDENT_SURNAME = "Kowalsky";
	private static final String STUDENT_NAME = "Adam";
	Student student;
	@Mock SchoolClass class1;
	@Mock Term term1;
	
	@BeforeEach
	public void setUp() throws Exception {
		student=new Student(STUDENT_NAME, STUDENT_SURNAME);
		List<Term> schedule=new ArrayList<Term>();
		schedule.add(term1);
		lenient().when(class1.getSchedule()).thenReturn(schedule);
	}

	@Test
	public void testSetSchoolClass() {
		student.setSchoolClass(class1);
		student.getSchedule();
	}

	@Test
	public void testGetName() {
		assertEquals(STUDENT_NAME,student.getName());
		assertEquals(STUDENT_SURNAME,student.getSurname());
	}

	@Test
	public void testGetSchedule() {
		assertNull(student.getSchedule());
		student.setSchoolClass(class1);
		Collection<Term> schedule=student.getSchedule();
		assertTrue(schedule.contains(term1));
	}
}
