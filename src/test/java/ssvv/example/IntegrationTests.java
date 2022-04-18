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

public class IntegrationTests
{
    private Service service;
    private Service service2;

    @BeforeEach
    public void setUp() {
        String filenameStudent = "fisiere_test/Studenti.xml";
        String filenameTema = "fisiere_test/Teme.xml";
        String filenameNota = "fisiere_test/Note.xml";

        StudentXMLRepo studentXMLRepository = new StudentXMLRepo(filenameStudent);
        StudentXMLRepo studentXmlRepository2 = new StudentXMLRepo("fisiere_test/Studenti1.xml");
        TemaXMLRepo temaXMLRepository = new TemaXMLRepo(filenameTema);
        TemaXMLRepo temaXMLRepository2 = new TemaXMLRepo("fisiere_test/Teme1.xml");
        NotaXMLRepo notaXMLRepository = new NotaXMLRepo(filenameNota);

        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();
        NotaValidator notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);

        this.service = new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);
        this.service2 = new Service(studentXmlRepository2, studentValidator, temaXMLRepository2, temaValidator, notaXMLRepository, notaValidator);
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
    public void tc_1_bigBangIntegration_addAssignment() throws Exception {
        service.addTema(new Tema("1", "integration", 12, 13));
        int size = ((Collection<?>) service.getAllTeme()).size();
        assertEquals(size, 1);
    }

    @Test
    public void tc_2_bigBangIntegration_addStudent() throws Exception {
        service.addStudent(new Student("1", "example", 1, "whatever@noemail.com"));
        int size = ((Collection<?>) service.getAllStudenti()).size();
        assertEquals(size, 1);
    }

    @Test
    public void tc_3_bigBangIntegration_addGrade() throws Exception {
        service.addTema(new Tema("1", "integration", 12, 14));
        service.addStudent(new Student("1", "example", 1, "whatever@noemail.com"));
        service2.addNota(new Nota("1", "1", "1", 5.5, LocalDate.of(2020, 12, 12)), "done");
        int size = ((Collection<?>) service2.getAllNote()).size();
        assertEquals(size, 1);
    }

    @Test
    public void tc_4_bigBangIntegration_addAssignment_Student_and_Grade() throws Exception {
        service.addTema(new Tema("1", "integration", 12, 14));
        service.addStudent(new Student("1", "example", 1, "whatever@noemail.com"));
        service.addNota(new Nota("1", "1", "1", 5.5, LocalDate.of(2018, 11, 1)), "done");
        int size1 = ((Collection<?>) service.getAllTeme()).size();
        int size2 = ((Collection<?>) service.getAllStudenti()).size();
        int size3 = ((Collection<?>) service.getAllNote()).size();

        assertEquals(size1, 1);
        assertEquals(size2, 1);
        assertEquals(size3, 1);
    }
}
