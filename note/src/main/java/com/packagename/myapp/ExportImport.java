package com.packagename.myapp;

import java.io.IOException;

// Imports and exports the current database to a local file

public class ExportImport {

    public ExportImport() {
        db_name_ = "notedb";
        db_user_ = "root";
        db_pass_ = "password";
    }

    public void exportDatabase() throws IOException {
        String export = "";
        export = "mysqldump -u "+db_user_+" -p"+db_pass_+" "+db_name_+" -r export.sql";
        Runtime.getRuntime().exec(export);
    }

    public void importDatabase() throws IOException {
        String[] importdb = {"/bin/sh" , "-c", "mysql -u" + db_user_+ " -p"+db_pass_+" " + db_name_ + " < export.sql"};
        Runtime.getRuntime().exec(importdb);
}

    String db_name_;
    String db_user_;
    String db_pass_;
}
