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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
public class TeacherTest {

    private static final String SURNAME = "Smith";
    private static final String NAME = "John";
    Teacher teacher;
    @Mock
    Subject subject1;
    @Mock
    Subject subject2;
    @Mock
    Term term1;
    @Mock
    Term term2;


    @BeforeEach
    public void setUp() throws Exception {
        teacher = new Teacher(NAME, SURNAME);
        List<Term> schedule1 = new ArrayList<Term>();
        schedule1.add(term1);
        lenient().when(subject1.getSchedule()).thenReturn(schedule1);
        List<Term> schedule2 = new ArrayList<Term>();
        schedule2.add(term2);
        lenient().when(subject2.getSchedule()).thenReturn(schedule2);
    }

    @Test
    public void testPersonalData() {
        assertEquals(NAME, teacher.getName());
        assertEquals(SURNAME, teacher.getSurname());
    }

    @Test
    public void testAddSubject() {
        teacher.addSubject(subject1);
    }

    @Test
    public void testGetSchedule() {
        Collection<Term> schedule = teacher.getSchedule();
        assertNotNull(schedule);
        assertEquals(0, schedule.size());
        teacher.addSubject(subject1);
        teacher.addSubject(subject2);
        schedule = teacher.getSchedule();
        assertTrue(schedule.contains(term1));
        assertTrue(schedule.contains(term2));
    }

    @Test
    public void testMeetsSearchCriteria() {
        assertTrue(teacher.meetsSearchCriteria(NAME, SURNAME));
    }

    @Test
    public void testMeetsSearchCriteriaNotFound() {
        assertFalse(teacher.meetsSearchCriteria(SURNAME, NAME));
    }

}
