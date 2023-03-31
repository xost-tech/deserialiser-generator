package com.xost.generator.constant;

public class Template {
    public static final String CLASS_TEMPLATE = "package %s;\n\n//%s\npublic class %s {\n%s}";
    public static final String FIELD_TEMPLATE = "\tprivate %s %s;\n";
    public static final String GETTER_TEMPLATE = "\tpublic %s get%s() {\n\t\treturn %s;\n\t}\n";
    public static final String SETTER_TEMPLATE = "\tpublic void set%s(%s %s) {\n\t\tthis.%s = %s;\n\t}\n";
    public static final String DSL_CLASS_TEMPLATE = "package %s;\n\npublic class %sDeserialiser {\n\tpublic %s parse(String s) {\n\t\t%s %s = new %s();\n%s\n\t\treturn %s;\n\t}\n}";
    public static final String DSL_FIELD_TEMPLATE = "\t\t%s.set%s(s.substring(%s, %s).trim());\n";
}
