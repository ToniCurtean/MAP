package com.example.socialnetworkgui.domain.validators;

import com.example.socialnetworkgui.domain.User;

import java.util.Objects;

public class UserValidator implements Validator<User> {

    //clasa ce implementeaza un validator ce valideaza un user


    //valideaza un utilizator
    //arunca exceptie cu mesajul corespunzator daca unul dintre atributele utilizatorului sunt neinitializate
    @Override
    public void validate(User entity) throws ValidationException {
        String message = "";
        if (entity.getId() == null)
            message += "ID-ul nu poate fi null!Introduceti un ID! ";
        if (Objects.equals(entity.getFirstName(), ""))
            message += "Prenumele nu poate fi null! Introduceti un prenume! ";
        if (Objects.equals(entity.getLastName(), ""))
            message += "Numele nu poate fi null! Introduceti un nume! ";
        if(Objects.equals(entity.getEmail(),""))
            message+="Email-ul nu poate fi null! Introduceti un email! ";
        if(Objects.equals(entity.getPassword(),""))
            message+="Parola nu poate fi null! Introduceti o parola! ";
        if (message.length() != 0)
            throw new ValidationException(message);
    }
}
