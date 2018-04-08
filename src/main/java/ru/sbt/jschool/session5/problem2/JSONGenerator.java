package ru.sbt.jschool.session5.problem2;

import org.apache.commons.lang3.ClassUtils;
import ru.sbt.jschool.session5.problem2.objects.*;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

import static ru.sbt.jschool.session5.problem2.JSONUtil.*;

/**
 */
public class JSONGenerator {

    private Map<Class, ObjectJSON> typeMap = new HashMap<>();

    {
        typeMap.put(String.class, new StringJSON());
        typeMap.put(Array.class, new ArrayJSON());
        typeMap.put(Date.class, new DateJSON());
        typeMap.put(Calendar.class, new CalendarJSON());
        typeMap.put(GregorianCalendar.class, new CalendarJSON());
        typeMap.put(Collection.class, new CollectionJSON());
    }

    public String generate(Object obj) {
        return generateObject(obj, "", 0).toString();
    }

    public void addType(Class clazz, ObjectJSON obj) {
        typeMap.put(clazz, obj);
    }

    private StringBuilder generateObject(Object obj, String name, int tabs) {
        StringBuilder sb = new StringBuilder();
        if (obj == null) {
            sb.append(String.format(BASE_OBJ, tabs(tabs), name, "null"));
        } else if (ClassUtils.isPrimitiveOrWrapper(obj.getClass())) {
            sb.append(String.format(BASE_OBJ, tabs(tabs), name, obj));
        } else if (obj.getClass().isArray()) {
            sb.append(String.format(BASE_OBJ, tabs(tabs), name, typeMap.get(Array.class).json(obj, tabs)));
        } else if (typeMap.containsKey(obj.getClass())) {
            sb.append(String.format(BASE, tabs(tabs), name, typeMap.get(obj.getClass()).json(obj, tabs)));
        } else if (ClassUtils.getAllInterfaces(obj.getClass()).contains(Collection.class)) {
            sb.append(String.format(BASE_OBJ, tabs(tabs), name, typeMap.get(Collection.class).json(obj, tabs)));
        } else {
            sb.append(String.format(BASE_OBJ, tabs(tabs), name, generateCustomObj(obj, tabs)));
        }
        if (name.equals("")) {
            sb.delete(0, 3);
        }
        return sb;
    }

    private StringBuilder generateCustomObj(Object obj, int tabs) {
        StringBuilder sb = new StringBuilder();
        sb.append(START);
        tabs++;
        Class clazz = obj.getClass();
        do {
            Field[] fields = clazz.getDeclaredFields();
            if (fields.length == 0)
                break;
            sb.append(generateFields(obj, fields, tabs));
            clazz = clazz.getSuperclass();
        } while (clazz != null);
        sb.deleteCharAt(sb.length() - 2);
        tabs--;
        sb.append(String.format(END, JSONUtil.tabs(tabs)));
        return sb;
    }


    private StringBuilder generateFields(Object obj, Field[] fields, int tabs) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String fieldName = field.getName();
            if (fieldName.equals("this$0"))
                continue;
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            Object fieldObj = null;
            try {
                fieldObj = field.get(obj);
            } catch (IllegalAccessException e) {
            }
            sb.append(generateObject(fieldObj, fieldName, tabs));
            if (i < fields.length - 1)
                sb.append(DIV);
            sb.append(RET);
        }
        return sb;
    }


}
