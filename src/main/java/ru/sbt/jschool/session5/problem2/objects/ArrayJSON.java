package ru.sbt.jschool.session5.problem2.objects;

import ru.sbt.jschool.session5.problem2.JSONUtil;

import java.lang.reflect.Array;

/**
 */
public class ArrayJSON implements ObjectJSON {
    private static final String START = "[\n";
    private static final String END = "%s]";
    private static final String BASE = "%s\"%s\"";

    @Override
    public StringBuilder json(Object obj, int tabs) {
        StringBuilder sb = new StringBuilder();
        sb.append(START);
        tabs++;
        for (int i = 0; i < Array.getLength(obj); i++) {
            sb.append(String.format(BASE, JSONUtil.tabs(tabs), Array.get(obj, i)));
            if (i < Array.getLength(obj) - 1)
                sb.append(JSONUtil.DIV);
            sb.append(JSONUtil.RET);
        }
        tabs--;
        sb.append(String.format(END, JSONUtil.tabs(tabs)));
        return sb;
    }
}
