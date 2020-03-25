package com.packagename.myapp.controller;


import com.packagename.myapp.entity.Note;
import com.packagename.myapp.notes.NoteInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.List;


@RestController
public class NoteController {

    @Autowired
    NoteInterface noteInterface;

    @RequestMapping (
            method = RequestMethod.GET,
            path = "/notes",
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    public List<Note> getNotes() {
        return noteInterface.findAll();
    }

}
