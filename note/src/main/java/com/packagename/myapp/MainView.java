package com.packagename.myapp;

import com.packagename.myapp.controller.NoteController;
import com.packagename.myapp.entity.Category;
import com.packagename.myapp.entity.Note;
import com.packagename.myapp.entity.NoteCategory;
import com.packagename.myapp.notes.CategoryInterface;
import com.packagename.myapp.notes.NoteCategoryInterface;
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
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.component.combobox.ComboBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.gatanaso.MultiselectComboBox;




import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


import java.awt.*;
import java.util.Set;

@Route
@PWA(name = "Notes App",
        shortName = "Notes App",
        description = "Take Notes.",
        enableInstallPrompt = true)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MainView extends VerticalLayout {

    public MainView(@Autowired PushNotification service, @Autowired NoteInterface noteInterface, @Autowired CategoryInterface categoryInterface, @Autowired NoteCategoryInterface noteCategoryInterface) {
        addInput(service, noteInterface, categoryInterface, noteCategoryInterface);
        addNotes(noteInterface, categoryInterface,noteCategoryInterface);
    }

    private void addInput(PushNotification service, NoteInterface noteInterface, CategoryInterface categoryInterface, NoteCategoryInterface noteCategoryInterface) {
        // Use TextField for standard text input
        TextArea textArea = new TextArea();
        textArea.setPlaceholder("Write here...");
        textArea.getStyle().set("minHeight,", "1000px");
        textArea.getStyle().set("minWidth", "300px");

        TextField textField_filename = new TextField("Enter name of your note:");
        ComboBox<String> priority = new ComboBox<>();
        MultiselectComboBox<String> category = new MultiselectComboBox<>();
/*
        Button button_save = new Button("Save note",
                e -> Notification.show(service.save(textField_filename.getValue(), textArea.getValue())));
*/
        Button button_save = new Button("Save note",
                e -> {
                    Dialog dialog = new Dialog();
                    Div div = new Div();
                    Button button = new Button("Close");
                    TextArea textarea = new TextArea();

                    if(category.isEmpty())
                    {
                        dialog.open();
                        textarea.setValue(category.getErrorMessage());
                        div.add(textarea,button);
                        dialog.add(div);
                        button.addClickListener(close_event-> dialog.close());
                        return;
                    }
                    else if(priority.isEmpty())
                    {
                        dialog.open();
                        textarea.setValue(priority.getErrorMessage());
                        div.add(textarea,button);
                        dialog.add(div);
                        button.addClickListener(close_event-> dialog.close());
                        return;
                    }
                    Note x = saveToDatabase(textField_filename.getValue(), textArea.getValue(), Integer.parseInt(priority.getValue()) , noteInterface);
                    noteInterface.updateNotes(x.getId_(), x.getText_(),x.getTitle_(), x.getPriority());
                    addNote(x, noteInterface, categoryInterface, noteCategoryInterface);
                    mapCategoryToNote(x.getId_(), category, noteCategoryInterface, categoryInterface);
                });
        HorizontalLayout horizont = new HorizontalLayout();

        category.setRequired(true);
        category.setRequiredIndicatorVisible(true);
        category.setErrorMessage("Category must be filled in!");
        category.setLabel("Categories");
        addCategories(categoryInterface, category);

        priority.setRequired(true);
        priority.setRequiredIndicatorVisible(true);
        priority.setErrorMessage("Priority must be filled in!");
        priority.setLabel("Priority");
        priority.setItems(setPriorities());

        button_save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        addClassName("centered-content");
        horizont.add(category, priority);
        add(textField_filename, textArea, horizont, button_save);
    }



    private void addNote(Note note, NoteInterface noteInterface,CategoryInterface categoryInterface, NoteCategoryInterface noteCategoryInterface) {
        Note(note, noteInterface, categoryInterface, noteCategoryInterface);
    }


    /**
     * Loads the List with all notes.
     */
    private void addNotes(NoteInterface noteInterface, CategoryInterface categoryInterface, NoteCategoryInterface noteCategoryInterface) {

        List<Note> notes = noteInterface.findAll();
        notes.forEach(note -> {
            Note(note, noteInterface, categoryInterface, noteCategoryInterface);
        });
    }

    private void addCategories(CategoryInterface categoryInterface, MultiselectComboBox<String> toAdd)
    {

        List<Category> categories = categoryInterface.findAll();
        ArrayList<String> cat_string = new ArrayList<String>();
        categories.forEach(
                category -> {
                    cat_string.add(category.getCategory());
                }
        );
        toAdd.setItems(cat_string.stream());
    }



    private void Note(Note note, NoteInterface noteInterface, CategoryInterface categoryInterface, NoteCategoryInterface noteCategoryInterface) {
        //this will be displayed;



        /*JButton button = new JButton("edit");

        JPanel panel = new JPanel();
        panel.setOpaque(true);
        panel.add(button);


        JFrame frame = new JFrame(note.getTitle_());

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);



        JTextArea textArea = new JTextArea(note.getTitle_());
        textArea.setText(note.getText_());
        textArea.setEditable(false);
*/
        Div div = new Div();
        Button button = new Button("Edit");
        ComboBox<String> priority = new ComboBox<>();
        MultiselectComboBox<String> category = new MultiselectComboBox<>();
        addCategories(categoryInterface, category);
        category.setLabel("Category");


        priority.setLabel("Priority");
        priority.setItems(setPriorities());

        TextField textField = new TextField(note.getTitle_());
        String cats = fillCategories(note.getId_(),noteCategoryInterface, categoryInterface).toString();
        TextArea categories = new TextArea();
        categories.setValue(cats.substring(1, cats.length() - 1));
        TextArea priorities = new TextArea();
        priorities.setValue(note.getPriority().toString());

        categories.isReadOnly();
        priorities.isReadOnly();

        HorizontalLayout horizontal = new HorizontalLayout();
        horizontal.add(textField, priorities, categories);
        textField.setValue(note.getText_());
        textField.getStyle().set("minHeight,", "1000px");
        textField.getStyle().set("minWidth", "300px");
        textField.setReadOnly(true);

        div.add(horizontal, button);

        Dialog dialog = new Dialog();

        Button save = new Button("Save");

        TextArea textArea = new TextArea(note.getTitle_());
        textArea.setValue(note.getText_());
        textArea.setHeight("450px");
        textArea.setWidth("1000px");

        category.setRequired(true);
        category.setRequiredIndicatorVisible(true);
        category.setErrorMessage("Category must be filled in!");

        priority.setRequired(true);
        priority.setRequiredIndicatorVisible(true);
        priority.setErrorMessage("Priority must be filled in!");


        button.addClickListener(event -> {
            dialog.open();
            dialog.setWidth("1000px");
            dialog.setHeight("500px");
            dialog.add(textArea);
            HorizontalLayout horizont = new HorizontalLayout();

            horizont.add(save,priority, category);
            dialog.add(horizont);
            priority.setValue(Integer.toString(note.getPriority()));
            category.setValue(fillCategories(note.getId_(),noteCategoryInterface,categoryInterface));
            priority.isRequired();
            category.isRequired();
            save.addClickListener(eventSave -> {

                Dialog dialog1 = new Dialog();
                Div div1 = new Div();
                Button button1 = new Button("Close");
                TextArea textarea1 = new TextArea();

                if(category.isEmpty())
                {
                    dialog1.open();
                    textarea1.setValue(category.getErrorMessage());
                    div1.add(textarea1,button1);
                    dialog1.add(div1);
                    button1.addClickListener(close_event-> dialog1.close());
                    return;
                }
                else if(priority.isEmpty())
                {
                    dialog1.open();
                    textarea1.setValue(priority.getErrorMessage());
                    div1.add(textarea1,button1);
                    dialog1.add(div1);
                    button1.addClickListener(close_event-> dialog1.close());
                    return;
                }
                dialog.close();
                noteInterface.updateNotes(note.getId_(),textArea.getValue(), note.getTitle_(), Integer.parseInt(priority.getValue()));
                textField.setValue(textArea.getValue());
                editCategories(note,noteCategoryInterface,categoryInterface,category);

            });

        });


        add(div);
    }

    public Note saveToDatabase(String filename, String text, Integer priority, NoteInterface notes)
    {
        Note note = new Note();
        note.setTitle_(filename);
        note.setText_(text);
        note.setPriority(priority);
        notes.save(note);

        return note;


    }

   public void mapCategoryToNote(Integer note_id, MultiselectComboBox<String> category,
                                 NoteCategoryInterface notecategoryInterface, CategoryInterface categoryInterface)
    {

        List<NoteCategory> note_category_list = new ArrayList<>();
        List<Category> categories = categoryInterface.findAll();
        Set<String> set_category = category.getValue();

        for(String cat : set_category)
        {
            //System.out.println(cat);

            Integer cat_id = -1;
            for(Category cat_iterator : categories)
            {
                if(cat_iterator.getCategory().equals(cat))
                {
                    cat_id = cat_iterator.getId_();
                    System.out.println(cat_id);
                    break;
                }
            }
            if(cat_id != -1)
                note_category_list.add(new NoteCategory(note_id, cat_id));
        }

        for(NoteCategory note_cat : note_category_list)
        {
            notecategoryInterface.save(note_cat);
        }
    }


    public Set<String> fillCategories(Integer note_id, NoteCategoryInterface notecategoryInterface, CategoryInterface categoryInterface)
    {
        List<Category> cat_entries = categoryInterface.findAll();
        List<NoteCategory> notecat_entries = notecategoryInterface.findAll();
        Set<String> categories = new HashSet<String>();


        for(NoteCategory  notecat_iterator : notecat_entries)
        {
            if(notecat_iterator.getFk_note().equals(note_id))
            {
                for(Category cat : cat_entries)
                    if(cat.getId_().equals(notecat_iterator.getFk_category()))
                    {
                        categories.add(cat.getCategory());
                        break;
                    }
            }
        }

        return categories;


    }

    public void editCategories(Note note,  NoteCategoryInterface notecategoryInterface, CategoryInterface categoryInterface, MultiselectComboBox<String> category)
    {

        List<NoteCategory> notecat_entries = notecategoryInterface.findAll();

        for(NoteCategory  notecat_iterator : notecat_entries)
        {
            if(notecat_iterator.getFk_note().equals(note.getId_()))
            {
                notecategoryInterface.delete(notecat_iterator);
            }
        }

        mapCategoryToNote(note.getId_(),category,notecategoryInterface,categoryInterface);


    }

    public Set<String> setPriorities()
    {
        Set<String> ret_set = new HashSet<>();
        for(int i = 1; i <= 10; i++)
        {
            ret_set.add(Integer.toString(i));
        }
        return ret_set;
    }

}