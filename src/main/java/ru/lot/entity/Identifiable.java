package ru.lot.entity;

import java.lang.reflect.ParameterizedType;

public interface Identifiable<T> {
    void setId(T index);

    T getId();

    default Class<?> getIndexClass() {
        ParameterizedType paramType;
        paramType = (ParameterizedType) this.getClass().getGenericInterfaces()[0];
        return paramType.getActualTypeArguments()[0].getClass();
    }
}
