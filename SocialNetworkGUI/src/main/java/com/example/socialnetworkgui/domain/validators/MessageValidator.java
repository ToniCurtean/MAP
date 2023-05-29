package com.example.socialnetworkgui.domain.validators;

import com.example.socialnetworkgui.domain.Message;
import org.xml.sax.SAXException;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import java.io.IOException;

public class MessageValidator implements Validator<Message> {

    @Override
    public void validate(Message message) throws ValidationException{
        String e="";
        if(message.getText().equals(""))
            e+="Mesajul nu poate sa fie null!";
        if(e.length()!=0)
            throw new ValidationException(e);
    }
}
