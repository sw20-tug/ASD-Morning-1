package com.packagename.myapp;

import javax.mail.PasswordAuthentication;
import java.util.Properties;

public class JAuthenticator extends javax.mail.Authenticator{

   public JAuthenticator(Properties prop, PasswordAuthentication pwd)
    {
        properties_ = prop;
        pwd_ = pwd;
    }

    PasswordAuthentication pwd_;
    Properties properties_;

    protected PasswordAuthentication getPasswordAuthentication() {
        return pwd_;
    }
}