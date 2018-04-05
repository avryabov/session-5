package ru.sbt.jschool.session5.problem2.objects;

import java.util.Collection;

/**
 */
public class CollectionJSON extends ArrayJSON {

    @Override
    public StringBuilder json(Object obj, int tabs) {
        return super.json(((Collection) obj).toArray(), tabs);
    }
}
