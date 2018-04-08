package ru.sbt.jschool.session5.problem2;

import java.io.InputStream;
import java.util.*;

/**
 */
public class InputTestObjects {

    public static Object inputObject(String num) {
        InputTestObjects inputTestObjects = new InputTestObjects();
        switch (num) {
            case "0": return inputTestObjects.new ClassPrimitive();
            case "1": return inputTestObjects.new ClassCollection();
            case "2": return inputTestObjects.new ClassDate();
            case "3": return inputTestObjects.new ClassWithNested();
            case "4": return inputTestObjects.new ClassPrimitivePrivate();
            case "5": return inputTestObjects.new ClassPrimitiveWrapperWrapper();
            case "6": return new ArrayList<>(Arrays.asList(new String[] {"one", "two", "three"}));
            case "7": return 77;
            default: return null;
        }
    }

    class ClassPrimitive
    {
        public String field1;
        public double field2;
        public int field3;
        public String[] array;
        public String nullField = null;

        ClassPrimitive(){
            field1 = "value";
            field2 = 0.1d;
            field3 = 1234;
            array = new String[] {"val1", "val2", "val3"};
        }
    }

    class ClassCollection {
        public List<String> list;
        public Set<Integer> set;

        public ClassCollection() {
            list = new LinkedList<>();
            list.add("val1");
            list.add("val2");
            list.add("val3");
            set = new HashSet<>();
            set.add(new Integer(1));
            set.add(new Integer(2));
            set.add(new Integer(3));
        }
    }

    class ClassDate {
        public Date date;
        public Calendar calendar;

        public ClassDate() {
            this.date = new Date();
            date.setDate(5);
            date.setMonth(3);
            date.setYear(118);
            this.calendar = Calendar.getInstance();
            calendar.set(2018, 3, 5);
        }
    }

    class ClassWithNested
    {
        public String field1;
        public double field2;
        public int field3;
        public String[] array;
        public NestedPrimitiveClass nestedObject;

        ClassWithNested(){
            field1 = "value";
            field2 = 0.1d;
            field3 = 1234;
            array = new String[] {"val1", "val2", "val3"};
            nestedObject = new NestedPrimitiveClass();
        }
    }

    class NestedPrimitiveClass
    {
        public String nestedField1;
        public double nestedField2;
        public int nestedField3;
        public String[] nestedArray;
        public ClassPrimitive doubleNestedObject;

        NestedPrimitiveClass(){
            nestedField1 = "value";
            nestedField2 = 0.1d;
            nestedField3 = 1234;
            nestedArray = new String[] {"val1", "val2", "val3"};
            doubleNestedObject = new ClassPrimitive();
        }
    }

    class ClassPrimitivePrivate
    {
        private String field1;
        private double field2;
        private int field3;
        private String[] array;
        private String nullField = null;

        ClassPrimitivePrivate(){
            field1 = "value";
            field2 = 0.1d;
            field3 = 1234;
            array = new String[] {"val1", "val2", "val3"};
        }
    }

    class ClassPrimitiveWrapper extends ClassPrimitive
    {
        public String fieldWrapper;

        ClassPrimitiveWrapper(){
            fieldWrapper = "valueWrapper";
            field1 = "newValue";
        }
    }

    class ClassPrimitiveWrapperWrapper extends ClassPrimitiveWrapper
    {
        public String fieldWrapperWrapper;

        ClassPrimitiveWrapperWrapper(){
            fieldWrapperWrapper = "valueWrapperWrapper";
        }
    }
}
