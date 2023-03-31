package com.xost.generator;

import org.junit.jupiter.api.Test;

import static com.xost.generator.constant.Status.*;
import static org.junit.jupiter.api.Assertions.*;

class GeneratorValidatorTest {

    @Test
    void isValidObject() {
        assertEquals(SUCCESS, new GeneratorValidator().isValidObject("Student,This is a student record\\nfor the school".split(",")));
    }

    @Test
    void isInvalidObjectSpecialChar() {
        var generatorValidator = new GeneratorValidator();
        assertAll(
                () -> assertEquals(INVALID_CLASS_NAME_FORMAT, generatorValidator.isValidObject("Student_,This is a student record\\nfor the school".split(","))),
                () -> assertEquals(INVALID_CLASS_NAME_FORMAT, generatorValidator.isValidObject("$tudent,This is a student record\\nfor the school".split(",")))
        );
    }

    @Test
    void isInvalidObjectFirstCharNum() {
        assertEquals(INVALID_CLASS_NAME_FORMAT, new GeneratorValidator().isValidObject("1Student,This is a student record\\nfor the school".split(",")));
    }

    @Test
    void isValidDeserialiser() {
        assertEquals(SUCCESS, new GeneratorValidator().isValidDeserialiser("firstName,1,20".split(",")));
    }

    @Test
    void isInvalidDeserialiserFieldNameSpecialChar() {
        var generatorValidator = new GeneratorValidator();
        assertAll(
                () -> assertEquals(INVALID_VARIABLE_NAME_FORMAT, generatorValidator.isValidDeserialiser("$irstName,1,20".split(","))),
                () -> assertEquals(INVALID_VARIABLE_NAME_FORMAT, generatorValidator.isValidDeserialiser("firstName_".split(",")))
        );
    }

    @Test
    void isInvalidDeserialiserFieldNameFirstCharNum() {
        assertEquals(INVALID_VARIABLE_NAME_FORMAT, new GeneratorValidator().isValidDeserialiser("1stName,1,20".split(",")));
    }

    @Test
    void isInvalidDeserialiserStartIndexInt()  {
        assertEquals(INVALID_START_INDEX_FORMAT, new GeneratorValidator().isValidDeserialiser("firstName,a,20".split(",")));
    }

    @Test
    void isInvalidDeserialiserEndIndexInt()  {
        assertEquals(INVALID_END_INDEX_FORMAT, new GeneratorValidator().isValidDeserialiser("firstName,1,b".split(",")));
    }
}