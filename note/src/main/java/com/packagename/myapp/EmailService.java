package com.packagename.myapp;

import com.packagename.myapp.entity.Note;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class EmailService {

    public EmailService()
    {
        from_ = "asdmorning1.2020@gmail.com";
        password_ = "ASDmorning1!";
        smtp_ = "smtp.gmail.com";
        properties_ = new Properties();
        properties_.put("mail.smtp.host", smtp_);
        properties_.put("mail.smtp.auth", "true");
        properties_.put("mail.smtp.starttls.enable", "true");
        properties_.put("mail.smtp.port", "587");
        instance = this;
    }

    public void setFormattedMessageContent(Set<String> cats, MimeMessage message, Note note)
    {
        Set<String> cats_;
        if(cats == null)
        {
            cats_ = new HashSet<>();
            cats_.add("");
        }
        else
            cats_ = cats;

        MimeMessage message_;
        if(message == null)
            message_ = new MimeMessage(getSession());
        else
            message_ = message;

        Note note_;
        if(note == null)
            note_ = new Note("" , "");
        else
            note_ = note;

        String msg = composeMessage(cats_, note_);

        try {
            message_.setContent(msg, "text/html");
            message_.setSubject(note_.getTitle_());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String composeMessage(Set<String> cats,Note note)
    {
        return "        <html>\n" +
            "<head>\n" +
            "\t<title></title>\n" +
            "</head>\n" +
            "<body>A user shared his note with you:\n\n" +
            "<h1><span style=\"font-size:14px\"><strong></h1>Note Title: </strong></span><span style=\"font-size:14\">"
            + note.getTitle_() + "</strong></span>\n" +
            "<h1><span style=\"font-size:14px\">Note: </span></h1>\n" +
            "<blockquote>\n" +
            "<p><span style=\"font-size:14px\">" + note.getText_() + "</span><br />\n&nbsp;</p>\n" +
            "</blockquote>\n" +
            "<p><span style=\"font-size:14px\"><strong>Priority: </strong>" + note.getPriority() + "<br />\n" +
            "<br />\n" +
            "<br />\n" +
            "<strong>Categories: </strong>" + cats.toString().substring(1, (cats.toString()).length() - 1) +
            "</span><br />\n" +
            "<br />\n" +
            "            Kind regards<br />\n" +
            "        Your ASD-Morning-1 develop team</p>\n" +
            "\n" +
            "<blockquote>\n" +
            "<h1>&nbsp;</h1>\n" +
            "</blockquote>\n" +
            "</body>\n" +
            "</html>";
    }

    public Session getSession()
    {
        return  Session.getInstance(properties_,  new JAuthenticator(properties_, new PasswordAuthentication(from_, password_)));
    }

    public String getFrom_() {
        return from_;
    }

    public String getPassword_() {
        return password_;
    }

    public String getSmtp_() {
        return smtp_;
    }

    String from_ ;
    String password_;
    String smtp_;
    Properties properties_;

    @PersistenceContext
    private EmailService instance;

}
