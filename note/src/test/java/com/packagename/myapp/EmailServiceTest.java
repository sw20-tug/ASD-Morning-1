package com.packagename.myapp;

import com.packagename.myapp.entity.Note;
import org.junit.Test;
import org.mockito.Mockito;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.validation.constraints.Email;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class EmailServiceTest {
    @Test
    public void testEmailServiceConstructor()
    {
       EmailService testing_object = new EmailService();
       assertEquals("asdmorning1.2020@gmail.com", testing_object.from_);
       assertEquals("ASDmorning1!", testing_object.password_);
       assertEquals("smtp.gmail.com", testing_object.smtp_);
       assertEquals(testing_object.smtp_, testing_object.properties_.get("mail.smtp.host"));
       assertEquals("true", testing_object.properties_.get("mail.smtp.auth"));
       assertEquals("true", testing_object.properties_.get("mail.smtp.starttls.enable"));
       assertEquals("587", testing_object.properties_.get("mail.smtp.port"));
    }

    @Test
    public void testSetFormattedMessageContent() throws MessagingException {
        Note mocked_note = mock(Note.class);
        MimeMessage mocked_message = mock(MimeMessage.class);

        EmailService mailing_service = new EmailService();
        mailing_service.setFormattedMessageContent(null, mocked_message, mocked_note);

        //Verifying Note calls
        Mockito.verify(mocked_note, Mockito.times(2)).getTitle_();
        Mockito.verify(mocked_note, Mockito.times(1)).getText_();

        //Verifying calls of message functions
        Mockito.verify(mocked_message, Mockito.times(1)).setContent(Mockito.anyObject(), Mockito.anyObject());
        Mockito.verify(mocked_message, Mockito.times(1)).setSubject(Mockito.anyObject());
    }
}
