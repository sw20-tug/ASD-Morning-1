package com.packagename.myapp;

import com.packagename.myapp.controller.NoteController;
import com.packagename.myapp.entity.Note;
import com.packagename.myapp.notes.NoteInterface;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;


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
    static boolean show_unfinished = true;

    public MainView(@Autowired PushNotification service, @Autowired NoteInterface noteInterface) {

        addInput(service, noteInterface);
        if(show_unfinished)
        {
            showunfinishedNotes(noteInterface);
        }
        else{
            showfinishedNotes(noteInterface);
        }
    }

    private void addInput(PushNotification service, NoteInterface noteInterface) {
        // Use TextField for standard text input
        Button changeview_button = new Button();
        TextArea textArea = new TextArea();
        textArea.setPlaceholder("Write here...");
        textArea.getStyle().set("minHeight,", "1000px");
        textArea.getStyle().set("minWidth", "300px");

        TextField textField_filename = new TextField("Enter name of your note:");

        Button button_save = new Button("Save note");

        button_save.addClickListener(event->{
            saveToDatabase(textField_filename.getValue(), textArea.getValue(), noteInterface);
            UI.getCurrent().getPage().reload();
        });

        button_save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button_save.addClassName("button");

        // Use custom CSS classes to apply styling. This is defined in shared-styles.css.
        addClassName("centered-content");
        if(show_unfinished)
        {
            changeview_button.setText("Show finished tasks");
        }
        else{
            changeview_button.setText("Show unfinished tasks");
        }
        changeview_button.addClickListener(event->{

            if(show_unfinished)
            {
                show_unfinished = false;
            }
            else{
                show_unfinished = true;
            }
            UI.getCurrent().getPage().reload();
        });


        add(textField_filename, textArea, button_save, changeview_button);
    }

    /**
     * Loads the List with all notes.
     */
    private void showunfinishedNotes(NoteInterface noteInterface) {

        List<Note> notes = noteInterface.findAll(Sort.by(Sort.Direction.DESC, "pinned"));

        notes.forEach(note -> {
            if(note.getDone_()==false)
            {
                Note(note, noteInterface);

            }
        });
    }
    private void showfinishedNotes(NoteInterface noteInterface) {

        List<Note> notes = noteInterface.findAll();

        notes.forEach(note -> {
            if(note.getDone_())
            {
                Note(note, noteInterface);
            }
        });


    }


    private void Note(Note note, NoteInterface noteInterface) {
        Div div = new Div();
        Button button = new Button("Edit");
        Button delete_button = new Button("Delete");
        delete_button.addClassName("delete_button");
        button.addClassName("button");

        Icon icon = new Icon(VaadinIcon.PIN);
        Button pin = new Button(icon);

        TextField textField = new TextField(note.getTitle_());
        textField.setValue(note.getText_());
        textField.getStyle().set("minHeight,", "1000px");
        textField.getStyle().set("minWidth", "300px");
        Checkbox done = new Checkbox("Done");

        textField.setReadOnly(true);

        div.add(textField, button, delete_button, done, pin);
        Dialog dialog = new Dialog();

        Button save = new Button("Save");


        TextArea textArea = new TextArea(note.getTitle_());
        textArea.setValue(note.getText_());
        textArea.setHeight("450px");
        textArea.setWidth("1000px");


        Notification notification = new Notification(
                "Pinned note", 3000);

        pin.addClickListener(clicked -> {
            note.setPinned(!note.getPinned());
            noteInterface.updateNotes(note.getId_(),textArea.getValue(), note.getTitle_(), note.getPinned());
            notification.open();
            UI.getCurrent().getPage().reload();

        done.setValue(note.getDone_());
        done.addClickListener(event -> {
           note.setDone_(!note.getDone_());
           noteInterface.updateNotes(note.getId_(),textArea.getValue(), note.getTitle_(), note.getDone_());

        });

        button.addClickListener(event -> {
            dialog.open();
            dialog.setWidth("1000px");
            dialog.setHeight("500px");
            dialog.add(textArea);
            dialog.add(save);

            save.addClickListener(eventSave -> {
                dialog.close();
                noteInterface.updateNotes(note.getId_(),textArea.getValue(), note.getTitle_(), note.getPinned(), note.getDone_());
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
        Note note = new Note(filename, text);
        notes.save(note);
    }
}

