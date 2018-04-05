package ru.sbt.jschool.session5.problem2;

/**
 */
public class JSONUtil {
    public static final String START = "{\n";
    public static final String END = "%s}";
    public static final String BASE = "%s\"%s\":\"%s\"";
    public static final String BASE_OBJ = "%s\"%s\":%s";
    public static final String DIV = ",";
    public static final String RET = "\n";
    public static final String TAB = "\t";

    public static String tabs(int tabs) {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < tabs; i++) {
            sb.append(TAB);
        }
        return sb.toString();
    }
}
