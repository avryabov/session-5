package ru.sbt.jschool.session5.problem1;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 */
public class SQLGenerator {

    private final String INSERT = "INSERT INTO %s(%s) VALUES (%s)";
    private final String UPDATE = "UPDATE %s SET %s WHERE %s";
    private final String DELETE = "DELETE FROM %s WHERE %s";
    private final String SELECT = "SELECT %s FROM %s WHERE %s";

    public <T> String insert(Class<T> clazz) {
        String tableName = clazz.getAnnotation(Table.class).name();
        Field[] fields = clazz.getDeclaredFields();
        return String.format(INSERT, tableName, paramsStr(fields, new Class[]{Column.class, PrimaryKey.class}, true, ", ", ""),
                paramsStr(fields, new Class[]{Column.class, PrimaryKey.class}, false, ", ", ""));
    }

    public <T> String update(Class<T> clazz) {
        String tableName = clazz.getAnnotation(Table.class).name();
        Field[] fields = clazz.getDeclaredFields();
        return String.format(UPDATE, tableName, paramsStr(fields, new Class[]{Column.class}, true, ", ", " = ?"),
                paramsStr(fields, new Class[]{PrimaryKey.class}, true, " AND ", " = ?"));
    }

    public <T> String delete(Class<T> clazz) {
        String tableName = clazz.getAnnotation(Table.class).name();
        Field[] fields = clazz.getDeclaredFields();
        return String.format(DELETE, tableName, paramsStr(fields, new Class[]{PrimaryKey.class}, true, " AND ", " = ?"));
    }

    public <T> String select(Class<T> clazz) {
        String tableName = clazz.getAnnotation(Table.class).name();
        Field[] fields = clazz.getDeclaredFields();
        return String.format(SELECT, paramsStr(fields, new Class[]{Column.class}, true, ", ", ""), tableName,
                paramsStr(fields, new Class[]{PrimaryKey.class}, true, " AND ", " = ?"));
    }

    private String paramsStr(Field[] fields, Class[] clazzes, boolean isName, String div, String add) {
        List<String> list = new ArrayList<>();
        for (Field field : fields) {
            String name = field.getName().toLowerCase();
            for (Class clazz : clazzes) {
                if (field.isAnnotationPresent(clazz)) {
                    if (isName) {
                        String nameAnn = "";
                        if (clazz == Column.class)
                            nameAnn = field.getAnnotation(Column.class).name().toLowerCase();
                        else if (clazz == PrimaryKey.class)
                            nameAnn = field.getAnnotation(PrimaryKey.class).name().toLowerCase();
                        if (nameAnn.equals(""))
                            list.add(name);
                        else
                            list.add(nameAnn);
                    } else
                        list.add("?");
                }
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i != 0)
                stringBuilder.append(div);
            stringBuilder.append(list.get(i));
            stringBuilder.append(add);
        }
        return stringBuilder.toString();
    }

}
