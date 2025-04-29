package ru.lot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.lang.reflect.ParameterizedType;

public interface Identifiable<T> {
    void setId(T index);

    T getId();

    @JsonIgnore
    default Class<?> getIndexClass() {
        ParameterizedType paramType;
        paramType = (ParameterizedType) this.getClass().getGenericInterfaces()[0];
        return paramType.getActualTypeArguments()[0].getClass();
    }
}
