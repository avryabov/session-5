package ru.sbt.jschool.session5.problem2.objects;

/**
 */
public class StringJSON implements ObjectJSON {
    @Override
    public StringBuilder json(Object obj, int tabs) {
        return new StringBuilder((String) obj);
    }
}
