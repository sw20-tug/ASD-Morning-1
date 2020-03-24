package com.packagename.myapp;

import com.packagename.myapp.entity.Note;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;

public class NotesItemLayout extends HorizontalLayout {

    public NotesItemLayout(Note note) {

        TextArea textArea = new TextArea("Note");
        textArea.getStyle().set("minHeight,", "1000px");
        textArea.getStyle().set("minWidth", "300px");
        textArea.setPlaceholder("Write here...");
    }
}
