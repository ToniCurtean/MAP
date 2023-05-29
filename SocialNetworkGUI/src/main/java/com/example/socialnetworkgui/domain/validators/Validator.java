package com.example.socialnetworkgui.domain.validators;

public interface Validator<T> {

    ///interfata unui Validator care valideaza entitati de tip T
    void validate(T entity) throws ValidationException;

}
