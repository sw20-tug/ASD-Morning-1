package com.packagename.myapp;

import org.junit.Test;
import org.mockito.*;

import javax.validation.constraints.Email;
import com.packagename.myapp.EmailService;

public class EmailServiceTest {

    @Test
    public void testSetFormattedMessageContent()
    {
        EmailService mocked_service = Mockito.mock(EmailService.class);
        Mockito.doCallRealMethod().when(mocked_service).setFormattedMessageContent(Mockito.anyObject(), Mockito.anyObject(), Mockito.anyObject());
        mocked_service.setFormattedMessageContent(Mockito.anyObject(), Mockito.anyObject(), Mockito.anyObject());
        Mockito.verify(mocked_service).composeMessage(Mockito.anyObject(), Mockito.anyObject());
    }


}
