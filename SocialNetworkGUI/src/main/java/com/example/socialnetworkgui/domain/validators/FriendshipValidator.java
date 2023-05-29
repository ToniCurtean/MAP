package com.example.socialnetworkgui.domain.validators;


import com.example.socialnetworkgui.domain.Friendship;

public class FriendshipValidator implements Validator<Friendship> {
    //clasa ce implementeaza un validator ce valideaza o prietenie
    //valideaza o prietenie
    //arunca exceptie cu mesajul corespunzator daca se incearca crearea unei prietenii intre aceeasi utilizatori
    @Override
    public void validate(Friendship entity) throws ValidationException {
        String message = "";
        if (entity.getIdUser1().equals(entity.getIdUser2()))
            message += "Nu se poate stabili o relatie de prietenie intre acelas utilizator! ";
        if (message.length() != 0)
            throw new ValidationException(message);
    }
}
