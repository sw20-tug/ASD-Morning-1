package com.packagename.myapp;

import com.packagename.myapp.controller.NoteController;
import com.packagename.myapp.entity.Note;
import com.packagename.myapp.notes.NoteInterface;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.util.List;


import java.awt.*;

@Route
@PWA(name = "Notes App",
        shortName = "Notes App",
        description = "Take Notes.",
        enableInstallPrompt = true)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MainView extends VerticalLayout {

    public MainView(@Autowired PushNotification service, @Autowired NoteInterface noteInterface) {
        addInput(service, noteInterface);
        addNotes(noteInterface);
    }

    private void addInput(PushNotification service, NoteInterface noteInterface) {
        // Use TextField for standard text input
        TextArea textArea = new TextArea();
        textArea.setPlaceholder("Write here...");
        textArea.getStyle().set("minHeight,", "1000px");
        textArea.getStyle().set("minWidth", "300px");

        TextField textField_filename = new TextField("Enter name of your note:");
/*
        Button button_save = new Button("Save note",
                e -> Notification.show(service.save(textField_filename.getValue(), textArea.getValue())));
*/

        Button button_save = new Button("Save note",
                e -> saveToDatabase(textField_filename.getValue(), textArea.getValue(), noteInterface));
        button_save.addClickListener(event -> {
            UI.getCurrent().getPage().reload();
        });
        button_save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button_save.addClassName("button");

        // Use custom CSS classes to apply styling. This is defined in shared-styles.css.
        addClassName("centered-content");

        add(textField_filename, textArea, button_save);
    }

    /**
     * Loads the List with all notes.
     */
    private void addNotes(NoteInterface noteInterface) {

        List<Note> notes = noteInterface.findAll();

        notes.forEach(note -> {
            Note(note, noteInterface);
        });


    }

    private void Note(Note note, NoteInterface noteInterface) {


        Div div = new Div();
        Button button = new Button("Edit");
        Button delete_button = new Button("Delete");
        delete_button.addClassName("delete_button");
        button.addClassName("button");

        TextField textField = new TextField(note.getTitle_());
        textField.setValue(note.getText_());
        textField.getStyle().set("minHeight,", "1000px");
        textField.getStyle().set("minWidth", "300px");
        textField.setReadOnly(true);

        div.add(textField, button, delete_button);

        Dialog dialog = new Dialog();

        Button save = new Button("Save");

        TextArea textArea = new TextArea(note.getTitle_());
        textArea.setValue(note.getText_());
        textArea.setHeight("450px");
        textArea.setWidth("1000px");

        button.addClickListener(event -> {
            dialog.open();
            dialog.setWidth("1000px");
            dialog.setHeight("500px");
            dialog.add(textArea);
            dialog.add(save);

            save.addClickListener(eventSave -> {
                dialog.close();
                noteInterface.updateNotes(note.getId_(),textArea.getValue(), note.getTitle_());
                textField.setValue(textArea.getValue());

            });

        });

        delete_button.addClickListener(event -> {
            noteInterface.deleteById(note.getId_());
            UI.getCurrent().getPage().reload();
        });


        add(div);
    }

    public void saveToDatabase(String filename, String text, NoteInterface notes)
    {
        Note note = new Note();
        note.setTitle_(filename);
        note.setText_(text);

        notes.save(note);
    }
}
