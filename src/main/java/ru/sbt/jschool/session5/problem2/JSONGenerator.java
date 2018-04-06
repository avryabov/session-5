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
        return generate(obj, 0).toString();
    }

    public void addSupportedType(Class clazz, ObjectJSON obj) {
        typeMap.put(clazz, obj);
    }

    private StringBuilder generate(Object obj, int tabs) {
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
        } while(clazz != null);
        sb.deleteCharAt(sb.length()-2);
        tabs--;
        sb.append(String.format(END, JSONUtil.tabs(tabs)));
        return sb;
    }


    private StringBuilder generateFields(Object obj, Field[] fields, int tabs) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String fieldName = field.getName();
            if(fieldName.equals("this$0"))
                continue;
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            Object fieldObj = null;
            try {
                fieldObj = field.get(obj);
            } catch (IllegalAccessException e) {}
            if (fieldObj == null)
                sb.append(String.format(BASE_OBJ, tabs(tabs), fieldName, "null"));
            else if (ClassUtils.isPrimitiveOrWrapper(field.getType())) {
                sb.append(String.format(BASE_OBJ, tabs(tabs), fieldName, fieldObj));
            } else if (field.getType().isArray()) {
                sb.append(String.format(BASE_OBJ, tabs(tabs), fieldName, typeMap.get(Array.class).json(fieldObj, tabs)));
            } else if (typeMap.containsKey(fieldObj.getClass())) {
                sb.append(String.format(BASE, tabs(tabs), fieldName, typeMap.get(fieldObj.getClass()).json(fieldObj, tabs)));
            } else if (ClassUtils.getAllInterfaces(fieldObj.getClass()).contains(Collection.class)) {
                sb.append(String.format(BASE_OBJ, tabs(tabs), fieldName, typeMap.get(Collection.class).json(fieldObj, tabs)));
            } else {
                sb.append(String.format(BASE_OBJ, tabs(tabs), fieldName, generate(fieldObj, tabs)));
            }
            if (i < fields.length - 1)
                sb.append(DIV);
            sb.append(RET);
        }
        return sb;
    }
}
