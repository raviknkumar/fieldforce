package com.example.fieldforce.converter;

import com.example.fieldforce.model.AuthUser;

import java.util.List;

public interface Converter<E,M> {

    E convertModelToEntity(M model, AuthUser user);

    E convertModelToEntity(M model);

    M convertEntityToModel(E entity);

    List<E> convertModelToEntity(List<M> modelList, AuthUser user);

    List<E> convertModelToEntity(List<M> modelList);

    List<M> convertEntityToModel(List<E> entityList);

    void applyChanges(E entity, M model, AuthUser user);

}
