package ssvv.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ssvv.example.domain.Nota;
import ssvv.example.domain.Student;
import ssvv.example.domain.Tema;
import ssvv.example.repository.NotaXMLRepo;
import ssvv.example.repository.StudentXMLRepo;
import ssvv.example.repository.TemaXMLRepo;
import ssvv.example.service.Service;
import ssvv.example.validation.NotaValidator;
import ssvv.example.validation.StudentValidator;
import ssvv.example.validation.TemaValidator;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AppTest 
{
    private Service service;

    @BeforeEach
    public void setUp() {
        String filenameStudent = "fisiere_test/Studenti.xml";
        String filenameTema = "fisiere_test/Teme.xml";
        String filenameNota = "fisiere_test/Note.xml";

        StudentXMLRepo studentXMLRepository = new StudentXMLRepo(filenameStudent);
        TemaXMLRepo temaXMLRepository = new TemaXMLRepo(filenameTema);
        NotaXMLRepo notaXMLRepository = new NotaXMLRepo(filenameNota);

        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();
        NotaValidator notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);

        this.service = new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);
    }

    @AfterEach
    public void cleanup() throws IOException {
        Path file = Paths.get("fisiere_test/Studenti.xml");
        Files.write(file, Collections.singletonList("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><inbox></inbox>"), StandardCharsets.UTF_8);
        file = Paths.get("fisiere_test/Teme.xml");
        Files.write(file, Collections.singletonList("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><inbox></inbox>"), StandardCharsets.UTF_8);
        file = Paths.get("fisiere_test/Note.xml");
        Files.write(file, Collections.singletonList("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><inbox></inbox>"), StandardCharsets.UTF_8);
    }

    @Test
    public void tc0_validStudent_studentAdded() throws Exception {
        service.addStudent(new Student("1", "example", 1, "whatever@noemail.com"));
        int size = ((Collection<?>) service.getAllStudenti()).size();
        assertEquals(size, 1);
    }

    @Test
    public void tc1_addStudent_success_validId() throws Exception {
        service.addStudent(new Student("1", "example", 1, "whatever@noemail.com"));
        assertEquals(service.findStudent("1").getID(), "1");
    }

    @Test
    public void tc2_addStudent_ExceptionThrown_invalidId(){
        assertThrows(Exception.class,
                () -> service.addStudent(new Student("", "example", 1, "whatever@noemail.com")));
    }

    @Test
    public void tc3_addStudent_success_validName() throws Exception {
        service.addStudent(new Student("1", "example", 1, "whatever@noemail.com"));
        assertEquals(service.findStudent("1").getNume(), "example");
    }

    @Test
    public void tc4_addStudent_ExceptionThrown_invalidName(){
        assertThrows(Exception.class,
                () -> service.addStudent(new Student("", "", 1, "whatever@noemail.com")));
    }

    @Test
    public void tc5_addStudent_success_validMail() throws Exception {
        service.addStudent(new Student("1", "example", 1, "whatever@noemail.com"));
        assertEquals(service.findStudent("1").getEmail(), "whatever@noemail.com");
    }

    @Test
    public void tc6_addStudent_ExceptionThrown_invalidMail(){
        assertThrows(Exception.class,
                () -> service.addStudent(new Student("", "example", -1, "whatever@noemail.com")));
    }

    @Test
    public void tc7_addStudent_success_validGroup() throws Exception {
        service.addStudent(new Student("1", "example", 1, "whatever@noemail.com"));
        assertEquals(service.findStudent("1").getGrupa(), 1);
    }

    @Test
    public void tc8_addStudent_ExceptionThrown_invalidGroup(){
        assertThrows(Exception.class,
                () -> service.addStudent(new Student("", "example", 1, null)));
    }

    @Test
    public void tc9_addStudent_success_validProf() throws Exception {
        service.addStudent(new Student("1", "example", 1, "whatever@noemail.com"));
    }

    @Test
    public void tc10_addStudent_ExceptionThrown_invalidProf(){
        assertThrows(Exception.class,
                () -> service.addStudent(new Student("", "example", 1, "whatever@noemail.com")));
    }


    @Test
    public void tc11_addStudent_success_IDDoesNotExist() throws Exception {
        service.addStudent(new Student("1", "example", 1, "whatever@noemail.com"));
    }

    @Test
    public void tc12_addStudent_ExceptionThrown_IdExists() throws Exception {
        service.addStudent(new Student("1", "example", 1, "whatever@noemail.com"));
        assertThrows(Exception.class,
                () -> service.addStudent(new Student("1", "example", 1, "whatever@noemail.com")));

    }

    @Test
    public void tc13_addAssignment_validAssignment_success() throws Exception {
        service.addTema(new Tema("1", "yes", 12, 13));
        int size = ((Collection<?>) service.getAllTeme()).size();
        assertEquals(size, 1);
    }

    @Test
    public void tc14_addAssignment_invalidId_exceptionThrown() {
        assertThrows(Exception.class,
                () -> service.addTema(new Tema("", "yes", 12, 13)));
    }

    @Test
    public void tc15_addAssignment_existentAssignment_assignmentReturned() throws Exception {
        service.addTema(new Tema("1", "da", 12, 13));
        assertThrows(Exception.class,
                () -> service.addTema(new Tema("1", "da", 12, 13)));
    }

    @Test
    public void tc16_addAssignment_validDescription_success() throws Exception {
        service.addTema(new Tema("1", "da", 12, 13));
        assertEquals(service.findTema("1").getDescriere(), "da");
    }

    @Test
    public void tc17_addAssignment_invalidDescription_exceptionThrown() {
        assertThrows(Exception.class,
                () -> service.addTema(new Tema("1", "", 12, 13)));
    }

    @Test
    public void tc18_addAssignment_validDeadline_success() throws Exception {
        service.addTema(new Tema("1", "da", 12, 13));
        assertEquals(service.findTema("1").getDeadline(), 12);
    }

    @Test
    public void tc19_addAssignment_invalidDeadline_exceptionThrown() {
        assertThrows(Exception.class,
                () -> service.addTema(new Tema("1", "da", 30, 13)));
    }

    @Test
    public void tc20_addAssignment_validReceive_exceptionThrown() throws Exception {
        service.addTema(new Tema("1", "da", 12, 13));
        assertEquals(service.findTema("1").getPrimire(), 13);
    }

    @Test
    public void tc21_addAssignment_invalidReceive_exceptionThrown() {
        assertThrows(Exception.class,
                () -> service.addTema(new Tema("1", "da", 12, 30)));
    }

    //SBTM 45 minutes session

    @Test
    public void tc22_addGrade_validGrade_gradeAdded() throws Exception {
        service.addTema(new Tema("1", "integration", 12, 14));
        service.addStudent(new Student("1", "example", 1, "whatever@noemail.com"));
        service.addNota(new Nota("1", "1", "1", 5.5, LocalDate.of(2018, 11, 1)), "done");
        int size = ((Collection<?>) service.getAllNote()).size();
        assertEquals(size, 1);
    }

    @Test
    public void tc23_addGrade_gradeWithInvalidStudent_exceptionThrown() throws Exception {
        service.addTema(new Tema("1", "integration", 12, 14));
        assertThrows(Exception.class,
                () -> service.addNota(new Nota("1", "1", "1", 5.5, LocalDate.of(2018, 11, 1)), "done"));
    }

    @Test
    public void tc24_addGrade_gradeWithInvalidAssignment_exceptionThrown() throws Exception {
        service.addStudent(new Student("1", "example", 1, "whatever@noemail.com"));
        assertThrows(Exception.class,
                () -> service.addNota(new Nota("1", "1", "1", 5.5, LocalDate.of(2018, 11, 1)), "done"));
    }

    @Test
    public void tc25_addGrade_gradeWithoutFeedback_gradeAdded() throws Exception {
        service.addTema(new Tema("1", "integration", 12, 14));
        service.addStudent(new Student("1", "example", 1, "whatever@noemail.com"));
        service.addNota(new Nota("1", "1", "1", 5.5, LocalDate.of(2018, 11, 1)), "");
        int size = ((Collection<?>) service.getAllNote()).size();
        assertEquals(size, 1);
    }

    @Test
    public void tc26_addGrade_gradeBiggerThan10_exceptionThrown() throws Exception {
        service.addTema(new Tema("1", "integration", 12, 14));
        service.addStudent(new Student("1", "example", 1, "whatever@noemail.com"));
        assertThrows(Exception.class,
                () -> service.addNota(new Nota("1", "1", "1", 11, LocalDate.of(2018, 11, 1)), "done"));
    }

    @Test
    public void tc26_addGrade_gradeLowerThan0_exceptionThrown() throws Exception {
        service.addTema(new Tema("1", "integration", 12, 14));
        service.addStudent(new Student("1", "example", 1, "whatever@noemail.com"));
        assertThrows(Exception.class,
                () -> service.addNota(new Nota("1", "1", "1", -1, LocalDate.of(2018, 11, 1)), "done"));
    }

    @Test
    public void tc27_addGrade_1DayDelay_gradeDeducted() throws Exception {
        service.addTema(new Tema("1", "integration", 14, 14));
        service.addStudent(new Student("1", "example", 1, "whatever@noemail.com"));
        service.addNota(new Nota("1", "1", "1", 5.5, LocalDate.of(2018, 11, 1)), "done");
        assertEquals(service.findNota("1").getNota(), 3.0);
    }

    @Test public void tc28_addGrade_alreadyExistingGrade_gradeReturned() throws Exception {
        service.addTema(new Tema("1", "integration", 14, 14));
        service.addStudent(new Student("1", "example", 1, "whatever@noemail.com"));
        service.addNota(new Nota("1", "1", "1", 5.5, LocalDate.of(2018, 11, 1)), "done");
        assertEquals(service.addNota(new Nota("1", "1", "1", 5.5, LocalDate.of(2018, 11, 1)), "done").getID(), service.getAllNote().iterator().next().getID());
    }

    // icremental testing

    @Test
    public void tc1_incrementalIntegration_addStudent() throws Exception {
        service.addStudent(new Student("1", "example", 1, "whatever@noemail.com"));
        assertEquals(service.findStudent("1").getID(), "1");
    }

    @Test
    public void tc2_incrementalIntegration_addStudent_addAssignment() throws Exception {
        service.addStudent(new Student("1", "example", 1, "whatever@noemail.com"));
        assertEquals(service.findStudent("1").getID(), "1");
        service.addTema(new Tema("1", "da", 12, 13));
        assertEquals(service.findTema("1").getID(), "1");
    }

    @Test
    public void tc3_incrementalIntegration_addStudent_addAssignment_addGrade() throws Exception {
        service.addStudent(new Student("1", "example", 1, "whatever@noemail.com"));
        assertEquals(service.findStudent("1").getID(), "1");
        service.addTema(new Tema("1", "da", 12, 13));
        assertEquals(service.findTema("1").getID(), "1");
        service.addNota(new Nota("1", "1", "1", 5.5, LocalDate.of(2018, 11, 1)), "done");
        assertEquals(service.findNota("1").getID(), "1");
    }

}
