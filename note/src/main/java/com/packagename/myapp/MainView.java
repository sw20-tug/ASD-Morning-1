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
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.vaadin.gatanaso.MultiselectComboBox;


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
    static boolean show_unfinished = true;

    public MainView(@Autowired PushNotification service, @Autowired NoteInterface noteInterface, @Autowired CategoryInterface categoryInterface, @Autowired NoteCategoryInterface noteCategoryInterface) {

        addInput(service, noteInterface, categoryInterface, noteCategoryInterface);
        if(show_unfinished)
        {
            showunfinishedNotes(noteInterface, categoryInterface, noteCategoryInterface);
        }
        else{
            showfinishedNotes(noteInterface, categoryInterface, noteCategoryInterface);
        }
    }

    private void addInput(PushNotification service, NoteInterface noteInterface, CategoryInterface categoryInterface, NoteCategoryInterface noteCategoryInterface) {
        // Use TextField for standard text input
        Button changeview_button = new Button();
        TextArea textArea = new TextArea();
        textArea.setPlaceholder("Write here...");
        textArea.getStyle().set("minHeight,", "1000px");
        textArea.getStyle().set("minWidth", "300px");

        TextField textField_filename = new TextField("Enter name of your note:");

        ComboBox<String> priority = createPriorityInput();
        MultiselectComboBox<String> category = createCategoryInput(categoryInterface);

        Button button_save = new Button("Save note");

        button_save.addClickListener(event->{

            if (!emptyCheck(priority,category))
                return;

            Note x = saveToDatabase(textField_filename.getValue(), textArea.getValue(), Integer.parseInt(priority.getValue()) , noteInterface);
            noteInterface.updateNotes(x.getId_(),textArea.getValue(), x.getTitle_(), x.getPinned(), x.getDone_(), Integer.parseInt(priority.getValue()));
            mapCategoryToNote(x.getId_(), category, noteCategoryInterface, categoryInterface);
            UI.getCurrent().getPage().reload();
        });

        HorizontalLayout horizont = new HorizontalLayout();
        horizont.add(category, priority);




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


        add(textField_filename, textArea, horizont, button_save, changeview_button);
    }

    /**
     * Loads the List with all notes.
     */
    private void showunfinishedNotes(NoteInterface noteInterface, CategoryInterface categoryInterface, NoteCategoryInterface noteCategoryInterface) {

        List<Note> notes = noteInterface.findAll(Sort.by(Sort.Direction.DESC, "pinned"));

        notes.forEach(note -> {
            if(note.getDone_()==false)
            {
                Note(note, noteInterface, categoryInterface, noteCategoryInterface);

            }
        });
    }
    private void showfinishedNotes(NoteInterface noteInterface, CategoryInterface categoryInterface, NoteCategoryInterface noteCategoryInterface) {

        List<Note> notes = noteInterface.findAll(Sort.by(Sort.Direction.DESC, "pinned"));

        notes.forEach(note -> {
            if(note.getDone_())
            {
                Note(note, noteInterface, categoryInterface, noteCategoryInterface);
            }
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
        Div div = new Div();
        Button button = new Button("Edit");
        Button delete_button = new Button("Delete");
        delete_button.addClassName("delete_button");
        button.addClassName("button");

        ComboBox<String> priority = createPriorityInput();
        MultiselectComboBox<String> category = createCategoryInput(categoryInterface);

        Icon icon = new Icon(VaadinIcon.PIN);
        Button pin = new Button(icon);

        String cats = fillCategories(note.getId_(),noteCategoryInterface, categoryInterface).toString();
        TextArea categories = new TextArea();
        categories.setValue(cats.substring(1, cats.length() - 1));
        categories.setReadOnly(true);

        TextArea priorities = new TextArea();
        priorities.setValue(note.getPriority().toString());
        priorities.setReadOnly(true);
        HorizontalLayout horizontal = new HorizontalLayout();

        TextField textField = new TextField(note.getTitle_());
        textField.setValue(note.getText_());
        textField.getStyle().set("minHeight,", "1000px");
        textField.getStyle().set("minWidth", "300px");
        Checkbox done = new Checkbox("Done");

        horizontal.add(textField, priorities, categories);
        textField.setReadOnly(true);

        div.add(horizontal, button, delete_button, done, pin);
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
            noteInterface.updateNotes(note.getId_(), textArea.getValue(), note.getTitle_(), note.getPinned(), note.getDone_(), note.getPriority());
            notification.open();
            UI.getCurrent().getPage().reload();
        });

        done.setValue(note.getDone_());
        done.addClickListener(event -> {
            note.setDone_(!note.getDone_());
            noteInterface.updateNotes(note.getId_(),textArea.getValue(), note.getTitle_(),note.getPinned(), note.getDone_(), note.getPriority());

        });

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

            save.addClickListener(eventSave -> {

                if (!emptyCheck(priority,category))
                    return;

                noteInterface.updateNotes(note.getId_(),textArea.getValue(), note.getTitle_(), note.getPinned(), note.getDone_(), Integer.parseInt(priority.getValue()));
                textField.setValue(textArea.getValue());
                editCategories(note,noteCategoryInterface,categoryInterface,category);
                UI.getCurrent().getPage().reload();

            });

        });


        delete_button.addClickListener(event -> {
            deleteCategoryMap(note, noteCategoryInterface, categoryInterface);
            noteInterface.deleteById(note.getId_());

            UI.getCurrent().getPage().reload();
        });

        add(div);


    }


    public Note saveToDatabase(String filename, String text, Integer priority, NoteInterface notes)
    {
        Note note = new Note(filename, text);
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
        Set<String> categories = new HashSet<>();


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
        deleteCategoryMap(note, notecategoryInterface, categoryInterface);

        mapCategoryToNote(note.getId_(),category,notecategoryInterface,categoryInterface);
    }

    public void deleteCategoryMap(Note note, NoteCategoryInterface noteCategoryInterface, CategoryInterface categoryInterface)
    {
        List<NoteCategory> notecat_entries = noteCategoryInterface.findAll();

        for(NoteCategory  notecat_iterator : notecat_entries)
        {
            if(notecat_iterator.getFk_note().equals(note.getId_()))
            {
                noteCategoryInterface.delete(notecat_iterator);
            }
        }
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



    public boolean emptyCheck(ComboBox<String> priority, MultiselectComboBox<String> category)
    {
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
            return false;
        }
        else if(priority.isEmpty())
        {
            dialog.open();
            textarea.setValue(priority.getErrorMessage());
            div.add(textarea,button);
            dialog.add(div);
            button.addClickListener(close_event-> dialog.close());
            return false;
        }
        dialog.close();
        return true;

    }


    public ComboBox<String> createPriorityInput()
    {
        ComboBox<String> priority = new ComboBox<>();
        priority.setLabel("Priority");
        priority.setItems(setPriorities());
        priority.setRequired(true);
        priority.setRequiredIndicatorVisible(true);
        priority.setErrorMessage("Priority must be filled in!");

        return priority;
    }

    public MultiselectComboBox<String> createCategoryInput(CategoryInterface categoryInterface)
    {
        MultiselectComboBox<String> category = new MultiselectComboBox<>();
        category.setLabel("Category");
        category.setLabel("Categories");
        addCategories(categoryInterface, category);
        category.setRequired(true);
        category.setRequiredIndicatorVisible(true);
        category.setErrorMessage("Category must be filled in!");

        return category;
    }



}

