package ru.sbt.jschool.session5.problem1;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 */
public class SQLGenerator {

    private final String INSERT = "INSERT INTO %s(%s) VALUES (%s)";
    private final String UPDATE = "UPDATE %s SET %s WHERE %s";
    private final String DELETE = "DELETE FROM %s WHERE %s";
    private final String SELECT = "SELECT %s FROM %s WHERE %s";

    public <T> String insert(Class<T> clazz) {
        Annotation[] ann = clazz.getAnnotations();
        String tableName = annotateName(ann[0]);

        LinkedList<Parameter> params = paramsList(clazz.getDeclaredFields());

        return String.format(INSERT, tableName, paramsStr(params, AnnotType.All, ", ", ""),
                paramsStr(params, AnnotType.Secret, ", ", ""));
    }

    public <T> String update(Class<T> clazz) {
        Annotation[] ann = clazz.getAnnotations();
        String tableName = annotateName(ann[0]);

        LinkedList<Parameter> params = paramsList(clazz.getDeclaredFields());

        return String.format(UPDATE, tableName, paramsStr(params, AnnotType.Column, ", ", " = ?"),
                paramsStr(params, AnnotType.PrimaryKey, " AND ", " = ?"));
    }

    public <T> String delete(Class<T> clazz) {
        Annotation[] ann = clazz.getAnnotations();
        String tableName = annotateName(ann[0]);

        LinkedList<Parameter> params = paramsList(clazz.getDeclaredFields());

        return String.format(DELETE, tableName, paramsStr(params, AnnotType.PrimaryKey, " AND ", " = ?"));
    }

    public <T> String select(Class<T> clazz) {
        Annotation[] ann = clazz.getAnnotations();
        String tableName = annotateName(ann[0]);

        LinkedList<Parameter> params = paramsList(clazz.getDeclaredFields());

        return String.format(SELECT, paramsStr(params, AnnotType.Column, ", ", ""), tableName,
                paramsStr(params, AnnotType.PrimaryKey, " AND ", " = ?"));
    }

    private class Parameter {
        AnnotType type;
        String name;

        public Parameter(AnnotType type, String name) {
            this.type = type;
            this.name = name;
        }
    }

    private enum AnnotType {All, PrimaryKey, Column, Secret}

    private LinkedList<Parameter> paramsList(Field[] fields) {
        LinkedList<Parameter> list = new LinkedList<>();
        for (Field f : fields) {
            String name = f.getName();
            Annotation[] annotations = f.getDeclaredAnnotations();

            if (annotations.length == 0)
                continue;
            String nameAnn = annotateName(annotations[0]);
            String type = annotateType(annotations[0]);

            if (nameAnn.equals(""))
                list.add(new Parameter(AnnotType.valueOf(type), name.toLowerCase()));
            else
                list.add(new Parameter(AnnotType.valueOf(type), nameAnn.toLowerCase()));
        }
        return list;
    }

    private String annotateName(Annotation annotation) {
        Pattern pattern = Pattern.compile("\\(name=(.+)\\)");
        Matcher matcher = pattern.matcher(annotation.toString());
        if (matcher.find())
            return matcher.group(1);
        return "";
    }

    private String annotateType(Annotation annotation) {
        Pattern pattern = Pattern.compile("\\.([^.]+)\\(");
        Matcher matcher = pattern.matcher(annotation.toString());
        if (matcher.find())
            return matcher.group(1);
        return "";
    }

    private String paramsStr(LinkedList<Parameter> list, AnnotType type, String div, String add) {
        StringBuilder stringBuilder = new StringBuilder();

        LinkedList<Parameter> lst = new LinkedList<>(list);

        ListIterator<Parameter> iterator = lst.listIterator();
        if (type == AnnotType.Column || type == AnnotType.PrimaryKey) {
            while (iterator.hasNext()) {
                if (iterator.next().type != type)
                    iterator.remove();
            }
        }

        iterator = lst.listIterator();
        while (iterator.hasNext()) {
            if (type == AnnotType.Secret) {
                iterator.next();
                stringBuilder.append("?");
            } else {
                stringBuilder.append(iterator.next().name);
            }
            stringBuilder.append(add);
            if (iterator.hasNext()) {
                stringBuilder.append(div);
            }

        }

        return stringBuilder.toString();
    }

}
