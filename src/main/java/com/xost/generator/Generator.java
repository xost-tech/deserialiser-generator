package com.xost.generator;

import com.xost.generator.constant.Status;
import com.xost.generator.util.AppUtil;
import com.xost.generator.util.FormatUtil;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static com.xost.generator.constant.Status.*;
import static com.xost.generator.constant.Template.*;

public class Generator {
    private static Logger logger = Logger.getLogger(Generator.class);
    public static void main(String[] args) {
        try {
            Generator generator = new Generator();
            String filename = Paths.get((new Generator().getClass().getClassLoader().getResource("schema/schema.txt")).toURI()).toFile().getAbsolutePath();
            List<String> content = generator.readSchemaFile(filename);
            GeneratorObject generatorObject = generator.parseSchemaFile(content);
            String objectContent = generator.constructObjectVelocity(generatorObject);
            //String objectContent = generator.constructObject(generatorObject);
            generator.generateFile(generatorObject.getObjectName(), objectContent);
            String deserialiserContent = generator.constructDeserialiser(generatorObject);
            generator.generateFile(generatorObject.getObjectName() + "Deserialiser", deserialiserContent);
        }
        catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public List<String> readSchemaFile(String filePath) throws Exception {
        List<String> content = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line = "";
            while ((line = br.readLine()) != null) {
                content.add(line);
            }
            br.close();
        }
        catch (IOException e) {
            throw new IOException(String.valueOf(FILE_NOT_FOUND));
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
        return content;
    }

    public GeneratorObject parseSchemaFile(List<String> content) throws Exception {
        GeneratorObject generatorObject = new GeneratorObject();
        if (content.size() < 2) {
            throw new IllegalArgumentException(String.valueOf(INVALID_FILE_STRUCTURE));
        }
        else {
            GeneratorValidator generatorValidator = new GeneratorValidator();
            List<GeneratorDeserialiser> generatorDeserialisers = new ArrayList<>();
            for (int i=0; i<content.size(); i++) {
                String[] fields = content.get(i).split(",");
                if (i == 0) {
                    if (fields.length != 2) {
                        throw new IllegalArgumentException(INVALID_LINE_STRUCTURE + " : " + (i+1));
                    }
                    else {
                        Status status = generatorValidator.isValidObject(fields);
                        if (status == SUCCESS) {
                            generatorObject.setObjectName(fields[0]);
                            generatorObject.setObjectDescription(fields[1]);
                        }
                        else {
                            throw new IllegalArgumentException(status + " : " + (i+1));
                        }
                    }
                }
                else {
                    if (fields.length != 3) {
                        throw new IllegalArgumentException(INVALID_LINE_STRUCTURE + " : " + (i+1));
                    }
                    else {
                        Status status = generatorValidator.isValidDeserialiser(fields);
                        if (status == SUCCESS) {
                            generatorDeserialisers.add(new GeneratorDeserialiser(
                                    fields[0],
                                    Integer.parseInt(fields[1])-1,
                                    Integer.parseInt(fields[2])));
                        }
                        else {
                            throw new IllegalArgumentException(status + " : " + (i+1));
                        }
                    }
                }
            }
            generatorObject.setGeneratorDeserialisers(generatorDeserialisers);
        }
        return generatorObject;
    }

    public String constructObjectVelocity(GeneratorObject generatorObject) throws Exception {
        FormatUtil formatUtil = new FormatUtil();
        VelocityEngine velocityEngine = new VelocityEngine();
        Properties properties = new Properties();
        properties.setProperty("resource.loader", "class");
        properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        velocityEngine.init(properties);
        VelocityContext velocityContext = null;
        org.apache.velocity.Template template = null;
        StringWriter writer = null;
        StringBuffer sb = new StringBuffer();

        for (GeneratorDeserialiser generatorDeserialiser : generatorObject.getGeneratorDeserialisers()) {
            velocityContext = new VelocityContext();
            velocityContext.put("fieldType", "String");
            velocityContext.put("fieldName", formatUtil.firstCharLower(generatorDeserialiser.getFieldName()));
            velocityContext.put("fieldNameCap", formatUtil.capitalize(generatorDeserialiser.getFieldName()));
            template = velocityEngine.getTemplate("template/objectField.vm");
            writer = new StringWriter();
            template.merge(velocityContext, writer);
            sb.append(writer);
        }

        velocityContext = new VelocityContext();
        velocityContext.put("objectPackage", new AppUtil().getProperties("output.package", "app"));
        velocityContext.put("objectDescription", generatorObject.getObjectDescription());
        velocityContext.put("objectName", generatorObject.getObjectName());
        velocityContext.put("objectField", sb.toString());
        template = velocityEngine.getTemplate("template/objectClass.vm");
        writer = new StringWriter();
        template.merge(velocityContext, writer);
        writer.close();

        return writer.toString();
    }

    public String constructObject(GeneratorObject generatorObject) {
        StringBuffer sb = new StringBuffer();
        FormatUtil formatUtil = new FormatUtil();
        for (GeneratorDeserialiser generatorDeserialiser : generatorObject.getGeneratorDeserialisers()) {
            sb.append(String.format(FIELD_TEMPLATE, "String", formatUtil.firstCharLower(generatorDeserialiser.getFieldName())));
            sb.append(String.format(GETTER_TEMPLATE,
                    "String",
                    formatUtil.capitalize(generatorDeserialiser.getFieldName()),
                    formatUtil.firstCharLower(generatorDeserialiser.getFieldName())));
            sb.append(String.format(SETTER_TEMPLATE,
                    formatUtil.capitalize(generatorDeserialiser.getFieldName()),
                    "String",
                    formatUtil.firstCharLower(generatorDeserialiser.getFieldName()),
                    formatUtil.firstCharLower(generatorDeserialiser.getFieldName()),
                    formatUtil.firstCharLower(generatorDeserialiser.getFieldName())));
        }
        return (String.format(CLASS_TEMPLATE,
                new AppUtil().getProperties("output.package", "app"),
                generatorObject.getObjectDescription(),
                formatUtil.capitalize(generatorObject.getObjectName()),
                sb));
    }

    public String constructDeserialiser(GeneratorObject generatorObject) {
        StringBuffer sb = new StringBuffer();
        FormatUtil formatUtil = new FormatUtil();
        for (GeneratorDeserialiser generatorDeserialiser : generatorObject.getGeneratorDeserialisers()) {
            sb.append(String.format(DSL_FIELD_TEMPLATE,
                    formatUtil.firstCharLower(generatorObject.getObjectName()),
                    formatUtil.capitalize(generatorDeserialiser.getFieldName()),
                    generatorDeserialiser.getStartIndex(),
                    generatorDeserialiser.getEndIndex()));
        }
        return (String.format(DSL_CLASS_TEMPLATE,
                new AppUtil().getProperties("output.package", "app"),
                formatUtil.capitalize(generatorObject.getObjectName()),
                formatUtil.capitalize(generatorObject.getObjectName()),
                formatUtil.capitalize(generatorObject.getObjectName()),
                formatUtil.firstCharLower(generatorObject.getObjectName()),
                formatUtil.capitalize(generatorObject.getObjectName()),
                sb,
                formatUtil.firstCharLower(generatorObject.getObjectName())));
    }

    public void generateFile(String name, String content) throws Exception {
        String filename = new AppUtil().getProperties("output.path", "app")
                + new FormatUtil().capitalize(name) + ".java";
        File file = new File(filename);
        if (file.exists()) {
            throw new IOException(FILE_ALREADY_EXISTS + " : " + filename);
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        bw.write(content);
        bw.close();
        logger.info("file generated : " + filename);
    }
}
