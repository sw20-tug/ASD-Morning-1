package com.packagename.myapp;

import org.junit.Test;

import static org.junit.Assert.*;

public class ExportImportTest {
    @Test
    public void testConstructor(){
        ExportImport test_object = new ExportImport();
        assertEquals("notedb", test_object.db_name_);
        assertEquals("root", test_object.db_user_);
        assertEquals("password", test_object.db_pass_);
    }
}
