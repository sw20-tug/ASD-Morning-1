package com.packagename.myapp;

import com.packagename.myapp.entity.Note;
import com.packagename.myapp.notes.NoteInterface;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringComponent
public class NotesLayout extends VerticalLayout {

    @Autowired
    NoteInterface notes;

    @PostConstruct
    void init() {
        setNotes(notes.findAll());
    }

    private void setNotes(List<Note> notes) {

        notes.forEach(note -> add(new NotesItemLayout(note)));
    }
}
