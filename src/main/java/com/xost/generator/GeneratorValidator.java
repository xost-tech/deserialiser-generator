package com.xost.generator;

import com.xost.generator.constant.Status;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import static com.xost.generator.constant.Status.*;

public class GeneratorValidator {
    public Status isValidObject(String[] fields) {
        //class name alphanumeric only and cannot start with number
        if (StringUtils.isEmpty(fields[0]) || !StringUtils.isAlphanumeric(fields[0])
                || NumberUtils.isDigits(fields[0].substring(0,1))) {
            return INVALID_CLASS_NAME_FORMAT;
        }
        return SUCCESS;
    }

    public Status isValidDeserialiser(String[] fields) {
        //variable name alphanumeric only and cannot start with number
        if (StringUtils.isEmpty(fields[0]) || !StringUtils.isAlphanumeric(fields[0])
                || NumberUtils.isDigits(fields[0].substring(0,1))) {
            return INVALID_VARIABLE_NAME_FORMAT;
        }
        //start index number only
        else if (StringUtils.isEmpty(fields[1]) || !NumberUtils.isDigits(fields[1])) {
            return INVALID_START_INDEX_FORMAT;
        }
        //end index number only
        else if (StringUtils.isEmpty(fields[2]) || !NumberUtils.isDigits(fields[2])) {
            return INVALID_END_INDEX_FORMAT;
        }
        return SUCCESS;
    }
}
