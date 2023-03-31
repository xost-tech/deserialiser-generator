package com.xost.generator;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GeneratorTest {

    @Test
    void readSchemaFileNotFound() {
        assertAll(
                () -> assertThrows(IOException.class, () -> new Generator().readSchemaFile("")),
                () -> assertThrows(IOException.class, () -> new Generator().readSchemaFile("/user/public/random/random.txt")),
                () -> assertThrows(IOException.class, () -> new Generator().readSchemaFile("\\Users\\macbook\\Downloads\\Deserialiser_Generator\\schema.txt"))
        );
    }

    @Test
    void parseSchemaFileInvalidFileStructure() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Generator().parseSchemaFile(new ArrayList<>())),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Generator().parseSchemaFile(Arrays.asList("Student,This is a student record\\nfor the school"))),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Generator().parseSchemaFile(Arrays.asList("firstName,1,20")))
        );
    }

    @Test
    void parseSchemaFileInvalidLineStructure() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Generator().parseSchemaFile(Arrays.asList(
                                "Student,",
                                "firstName,1,20"
                        ))),
                () -> assertThrows(IllegalArgumentException.class,
                        () -> new Generator().parseSchemaFile(Arrays.asList(
                                "Student,This is a student record\\nfor the school",
                                "firstName,,20",
                                "lastName,21,40"
                        )))
        );
    }

    @Test
    void constructObjectEqual() {
        String content = "package com.xost.generator.output;\n\n" +
                "////This is a student record\\nfor the school\n" +
                "public class Student {\n" +
                "\tprivate String firstName;\n" +
                "\tpublic String getFirstName() {\n" +
                "\t\treturn firstName;\n" +
                "\t}\n" +
                "\tpublic void setFirstName(String firstName) {\n" +
                "\t\tthis.firstName = firstName;\n" +
                "\t}\n" +
                "\tprivate String lastName;\n" +
                "\tpublic String getLastName() {\n" +
                "\t\treturn lastName;\n" +
                "\t}\n" +
                "\tpublic void setLastName(String lastName) {\n" +
                "\t\tthis.lastName = lastName;\n" +
                "\t}\n" +
                "}";
        assertEquals(content, new Generator().constructObject(
                new GeneratorObject(
                        "Student",
                        "//This is a student record\\nfor the school",
                        Arrays.asList(
                                new GeneratorDeserialiser("firstName", 0, 20),
                                new GeneratorDeserialiser("lastName", 20, 40)
                        )
                )
        ));
    }

    @Test
    void constructDeserialiserEqual() {
        String content = "package com.xost.generator.output;\n\n" +
                "public class StudentDeserialiser {\n" +
                "\tpublic Student parse(String s) {\n" +
                "\t\tStudent student = new Student();\n" +
                "\t\tstudent.setFirstName(s.substring(0, 20).trim());\n" +
                "\t\tstudent.setLastName(s.substring(20, 40).trim());\n\n" +
                "\t\treturn student;\n" +
                "\t}\n" +
                "}";
        assertEquals(content, new Generator().constructDeserialiser(
                new GeneratorObject(
                        "Student",
                        "//This is a student record\\nfor the school",
                        Arrays.asList(
                                new GeneratorDeserialiser("firstName", 0, 20),
                                new GeneratorDeserialiser("lastName", 20, 40)
                        )
                )
        ));
    }
}