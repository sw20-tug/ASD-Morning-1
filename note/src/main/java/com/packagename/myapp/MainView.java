package com.packagename.myapp;
import com.packagename.myapp.entity.Category;
import com.packagename.myapp.entity.Note;
import com.packagename.myapp.entity.NoteCategory;
import com.packagename.myapp.notes.CategoryInterface;
import com.packagename.myapp.notes.NoteCategoryInterface;
import com.packagename.myapp.notes.NoteInterface;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.PWA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.vaadin.gatanaso.MultiselectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;


import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.validation.constraints.Email;


import java.util.List;


@Route
@PWA(name = "Notes App",
        shortName = "Notes App",
        description = "Take Notes.",
        enableInstallPrompt = true)
@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")


public class MainView extends VerticalLayout {

    enum AppliedFilters{
        PRI_CAT_DATE, PRI_CAT, PRI_DATE, CAT_DATE, PRI, CAT, DATE, NONE, INVALID
    }

    public enum Language{
        BUTTON_CLEAR(0), BUTTON_EXPORT(1), UNFINISHED(2), FINISHED(3),
        SORT_DATE(4), SORT_TITLE(5), CATEGORIES(6), DATE_FROM(7),
        DATE_UNTIL(8), WRITE_HERE(9), SAVE_NOTE(10),
        APPLY_FILTER(11), EDIT(12), DELETE(13), SHARE(14),
        DONE(15), SAVE(16), SHARE_WITH(17), VALID_EMAIL(18),
        SEND(19), CLOSE(20), PRIORITY(21), PRIORITY_FILLING(22),
        CATEGORY(23), CATEGORY_FILLING(24), SCHOOL(25), SHOPPING(26),
        HOME(27), WORK(28), WORKOUT(29), TITLE(30), IMPORT(31), EXPORT(32);

        int index;

        Language(int newValue) {
            index = newValue;
        }

        public int getIndex()
        {
            return index;
        }
    }


    static boolean show_unfinished = true;

    static boolean date_ = false;
    static boolean title_ = false;
    static String filter_cat_ = "";

    static String filter_pri_ = "";
    static LocalDate date_from_ = null;
    static LocalDate date_until_ = null;
    static EmailService mailing_service = new EmailService();

    public MainView(@Autowired PushNotification service, @Autowired NoteInterface noteInterface, @Autowired CategoryInterface categoryInterface, @Autowired NoteCategoryInterface noteCategoryInterface) {
        initializeCat(categoryInterface);
        languageSelect();
        addInput(service, noteInterface, categoryInterface, noteCategoryInterface);
        if(show_unfinished)
        {
            showunfinishedNotes(noteInterface, categoryInterface, noteCategoryInterface);
        }
        else{
            showfinishedNotes(noteInterface, categoryInterface, noteCategoryInterface);
        }
    }
    static int language = 0;

    private void languageSelect()
    {
        Button button_export = new Button(languages[Language.EXPORT.getIndex()][language]);
        Button button_import = new Button(languages[Language.IMPORT.getIndex()][language]);
        button_import.getStyle().set("margin-left", "530px");

        ExportImport export_import_instance = new ExportImport();


    button_export.addClickListener(test ->
        {
            try {
                export_import_instance.exportDatabase();

            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        button_import.addClickListener(test ->
        {
            try {
                export_import_instance.importDatabase();
                UI.getCurrent().getPage().reload();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        HorizontalLayout languages = new HorizontalLayout();

        Button de = new Button("Deutsch");
        Button en = new Button("English");
        Button fr = new Button("French");

        de.addClassName("language");
        en.addClassName("language");
        fr.addClassName("language");

        languages.add(de, en, fr, button_import, button_export);

        de.addClickListener(event->{
            language = 0;
            UI.getCurrent().getPage().reload();
        });
        en.addClickListener(event->{
            language = 1;
            UI.getCurrent().getPage().reload();
        });
        fr.addClickListener(event->{
            language = 2;
            UI.getCurrent().getPage().reload();
        });

        add(languages);
    }


    //static String [] label_clear = {"Löschen", "Clear", "Effacer"};
    static String[][] languages = {
            {"Löschen", "Clear", "Effacer"},
            {"Exportieren", "Export", "Exportation"},
            {"Zeige Vollendet", "Show finished", "Spectacle terminé"},
            {"Zeige Unvollendete", "Show Unfinished", "Afficher inachevé"},
            {"Sortiere nach Datum", "Sort Date", "Date de tri"},
            {"Sortiere nach Titel", "Sort Title", "Trier le titre"},
            {"Kategorien", "Categories", "Les catégories"},
            {"Datum von", "Date from", "Dater de"},
            {"Datum bis", "Date until", "Date jusqu'au"},
            {"Hier schreiben...", "Write here...", "Ecrire ici..."},
            {"Notiz speichern", "Save note", "Enregistrer la note"},
            {"Filter anwenden", "Apply filter", "Appliquer le filtre"},
            {"Ändern", "Edit", "Éditer"},
            {"Löschen", "Delete", "Supprimer"},
            {"Teilen", "Share", "Partager"},
            {"Fertig", "Done", "Terminé"},
            {"Speichern", "Save", "sauver"},
            {"Teilen mit: (email)", "share with: (email)", "partager avec: (email)"},
            {"Bitte geben Sie eine gültige E-Mail-Adresse ein.", "Please enter a valid email adress.", "Veuillez saisir une adresse e-mail valide."},
            {"Senden", "Send", "Envoyer"},
            {"Schließen", "Close", "proche"},
            {"Priorität", "Priority", "Priorité"},
            {"Priorität muss ausgefüllt werden!", "Priority must be filled in!", "La priorité doit être remplie!"},
            {"Kategorie:", "Category:", "Catégorie:"},
            {"Kategorie muss ausgefüllt werden!", "Category must be filled in!", "La catégorie doit être remplie!"},
            {"Schule", "School", "École"},
            {"Einkaufen", "Shopping", "Achats"},
            {"Zuhause", "Home", "Accueil"},
            {"Arbeiten", "Work", "Travail"},
            {"Training", "Workout", "Faire des exercices"},
            {"Titel", "Title", "Titre"},
            {"Importieren", "Import", "Importation"},
            {"Exportieren", "Export", "Exportation"},
    };


    private void addInput(PushNotification service, NoteInterface noteInterface, CategoryInterface categoryInterface, NoteCategoryInterface noteCategoryInterface) {
        // Use TextField for standard text input
        Button changeview_button = new Button();
        Button clear_filters = new Button();
        clear_filters.setText(languages[Language.BUTTON_CLEAR.getIndex()][language]);
        Div input_div = new Div();
        input_div.getStyle().set("width", "100%");
        input_div.getStyle().set("display", "flex");
        input_div.getStyle().set("align-items", "center");
        input_div.getStyle().set("justify-content", "center");
        Div text_area_div = new Div();
        text_area_div.getStyle().set("width", "100%");
        text_area_div.getStyle().set("display", "flex");
        text_area_div.getStyle().set("align-items", "center");
        text_area_div.getStyle().set("justify-content", "center");
        Div save_button_div = new Div();
        save_button_div.getStyle().set("width", "100%");
        save_button_div.getStyle().set("display", "flex");
        save_button_div.getStyle().set("align-items", "center");
        save_button_div.getStyle().set("justify-content", "center");
        Div filter_div = new Div();
        filter_div.getStyle().set("width", "100%");
        Div line = new Div();
        line.getStyle().set("width","100%").set("border-top","4px solid grey");

        Button button_export = new Button();
        button_export.setText(languages[Language.BUTTON_EXPORT.getIndex()][language]);

        Checkbox unfinished_finished = new Checkbox((show_unfinished) ? (languages[Language.FINISHED.getIndex()][language]) : (languages[Language.UNFINISHED.getIndex()][language]) );
        Checkbox date = new Checkbox(languages[Language.SORT_DATE.getIndex()][language]);
        date.setValue(date_);
        Checkbox title = new Checkbox(languages[Language.SORT_TITLE.getIndex()][language]);
        title.setValue(title_);

        ComboBox<String>filter_pri = createPriorityInput(true);
        filter_pri.setValue(filter_pri_);

        ComboBox<String> filter_cat = new ComboBox<>(languages[Language.CATEGORIES.getIndex()][language]);
        filter_cat.setItems(getCategories(categoryInterface));
        filter_cat.setValue(filter_cat_);

        DatePicker filter_date_from = new  DatePicker(languages[Language.DATE_FROM.getIndex()][language]);
        filter_date_from.setValue(date_from_);

        DatePicker filter_date_until = new  DatePicker(languages[Language.DATE_UNTIL.getIndex()][language]);
        filter_date_until.setValue(date_until_);

        unfinished_finished.setValue(!show_unfinished);

        TextArea textArea = new TextArea();
        textArea.setPlaceholder(languages[Language.WRITE_HERE.getIndex()][language]);
        textArea.getStyle().set("height,", "2000px");
        textArea.getStyle().set("width", "605px");

        TextField textField_filename = new TextField(languages[Language.TITLE.getIndex()][language]);

        ComboBox<String> priority = createPriorityInput(false);
        MultiselectComboBox<String> category = createCategoryInput(categoryInterface);

        Button button_save = new Button(languages[Language.SAVE_NOTE.getIndex()][language]);

        button_save.addClickListener(event->{

            if (!emptyCheck(priority,category))
                return;

            Note note = saveToDatabase(textField_filename.getValue(), textArea.getValue(), Integer.parseInt(priority.getValue()) , noteInterface);
            mapCategoryToNote(note.getId_(), category, noteCategoryInterface, categoryInterface);
            UI.getCurrent().getPage().reload();
        });

        clear_filters.addClickListener(clear_filter_event ->{
            SortAndFilter.clearFiltering(filter_pri, filter_cat);
            UI.getCurrent().getPage().reload();
        });


        //filter Comboboxes
        HorizontalLayout horizont_filter_comboboxes = new HorizontalLayout();
        horizont_filter_comboboxes.add(filter_pri, filter_cat, filter_date_from, filter_date_until);
        horizont_filter_comboboxes.getStyle().set("width", "100%");
        horizont_filter_comboboxes.getStyle().set("display", "flex");
        horizont_filter_comboboxes.getStyle().set("align-items", "center");
        horizont_filter_comboboxes.getStyle().set("justify-content", "center");


        //Fields for category and priority input
        HorizontalLayout horizont_add_cat_pri = new HorizontalLayout();
        horizont_add_cat_pri.add(textField_filename, category, priority);

        //filter Checkboxes
        HorizontalLayout horizont_filter_checkboxes = new HorizontalLayout();
        horizont_filter_checkboxes.add(title, date,  unfinished_finished);
        horizont_filter_checkboxes.getStyle().set("width", "100%");
        horizont_filter_checkboxes.getStyle().set("display", "flex");
        horizont_filter_checkboxes.getStyle().set("align-items", "center");
        horizont_filter_checkboxes.getStyle().set("justify-content", "center");


        //filter buttons
        HorizontalLayout filter_buttons = new HorizontalLayout();
        filter_buttons.add(changeview_button, clear_filters);
        filter_buttons.getStyle().set("margin-left", "75px");


        // Use custom CSS classes to apply styling. This is defined in shared-styles.css.
        addClassName("centered-content");
        changeview_button.setText(languages[Language.APPLY_FILTER.getIndex()][language]);
        changeview_button.addClickListener(event->{
            show_unfinished = !unfinished_finished.getValue();
            SortAndFilter.keepSortAndFilterOnReload(title, date, filter_pri, filter_cat, filter_date_from, filter_date_until);
            UI.getCurrent().getPage().reload();
        });

        save_button_div.add(button_save);
        text_area_div.add(textArea);
        input_div.add(horizont_add_cat_pri);
        filter_div.add(horizont_filter_checkboxes, horizont_filter_comboboxes, filter_buttons);

        add(input_div, text_area_div, save_button_div, line, filter_div);

    }

    /**
     * Loads the List with all notes.
     */
    private void showunfinishedNotes(NoteInterface noteInterface, CategoryInterface categoryInterface, NoteCategoryInterface noteCategoryInterface) {

        List<Note> notes = noteInterface.findAll(Sort.by(Sort.Direction.DESC, "pinned"));
        ArrayList<Integer> pinned_notes = new ArrayList<>();
        notes.forEach(note-> {
            if(!note.getDone_())
            {
                if(!note.getPinned())
                    return;
                Note(note, noteInterface, categoryInterface, noteCategoryInterface);
                pinned_notes.add(note.getId_());
            }
        });

        if(title_ && date_)
            notes = noteInterface.findAll(Sort.by(Sort.Direction.ASC, "created", "title"));
        else if(title_)
            notes = noteInterface.findAll(Sort.by(Sort.Direction.ASC, "title"));
        else if(date_)
            notes = noteInterface.findAll(Sort.by(Sort.Direction.ASC, "created"));

        AppliedFilters applied_filter = SortAndFilter.checkFiltering();

        notes.forEach(note -> {
            if(!pinned_notes.contains(note.getId_()) && !note.getDone_())
                applyFilter(note, noteInterface, noteCategoryInterface, categoryInterface, applied_filter);
        });
    }

    private void showfinishedNotes(NoteInterface noteInterface, CategoryInterface categoryInterface, NoteCategoryInterface noteCategoryInterface) {

        List<Note> notes = noteInterface.findAll(Sort.by(Sort.Direction.DESC, "pinned"));
        ArrayList<Integer> pinned_notes = new ArrayList<>();
        notes.forEach(note-> {
            if(note.getDone_())
            {
                if(note.getPinned())
                    return;
                Note(note, noteInterface, categoryInterface, noteCategoryInterface);
                pinned_notes.add(note.getId_());
            }
        });

        if(title_ && date_)
            notes = noteInterface.findAll(Sort.by(Sort.Direction.ASC, "created", "title"));
        else if(title_)
            notes = noteInterface.findAll(Sort.by(Sort.Direction.ASC, "title"));
        else if(date_)
            notes = noteInterface.findAll(Sort.by(Sort.Direction.ASC, "created"));

        AppliedFilters applied_filter = SortAndFilter.checkFiltering();
        notes.forEach(note -> {
            if(!pinned_notes.contains(note.getId_()) && note.getDone_())
                    applyFilter(note, noteInterface, noteCategoryInterface, categoryInterface, applied_filter);
        });
    }


    private void applyFilter(Note note, NoteInterface noteInterface, NoteCategoryInterface noteCategoryInterface,
                               CategoryInterface categoryInterface, AppliedFilters filter)
    {
        switch(filter)
        {
            case PRI:
                if(filter_pri_.equals(note.getPriority().toString()))
                    Note(note, noteInterface, categoryInterface, noteCategoryInterface);
                break;
            case PRI_CAT:
                if(filter_pri_.equals(note.getPriority().toString()) && checkCategory(categoryInterface,
                        noteCategoryInterface, note.getId_()))
                    Note(note, noteInterface, categoryInterface, noteCategoryInterface);
                break;
            case PRI_DATE:
                if(SortAndFilter.checkDate(note) && filter_pri_.equals(note.getPriority().toString()))
                    Note(note, noteInterface, categoryInterface, noteCategoryInterface);
                break;
            case PRI_CAT_DATE:
                if(filter_pri_.equals(note.getPriority().toString()) && checkCategory(categoryInterface,
                        noteCategoryInterface, note.getId_()) && SortAndFilter.checkDate(note))
                    Note(note, noteInterface, categoryInterface, noteCategoryInterface);
                break;
            case CAT:
                if(checkCategory(categoryInterface, noteCategoryInterface, note.getId_()))
                    Note(note, noteInterface, categoryInterface, noteCategoryInterface);
                break;
            case CAT_DATE:
                if(SortAndFilter.checkDate(note) && checkCategory(categoryInterface, noteCategoryInterface, note.getId_()))
                    Note(note, noteInterface, categoryInterface, noteCategoryInterface);
                break;
            case DATE:
                if(SortAndFilter.checkDate(note))
                    Note(note, noteInterface, categoryInterface, noteCategoryInterface);
                break;
            case NONE:
                Note(note, noteInterface, categoryInterface, noteCategoryInterface);
                break;
            default:
                break;
        }
    }

    public Boolean checkCategory(CategoryInterface categoryInterface, NoteCategoryInterface noteCategoryInterface, Integer note_id)
    {
        Set<String> categories_to_id = fillCategories(note_id, noteCategoryInterface, categoryInterface);
        return SortAndFilter.checkCategory(categories_to_id);

    }

    private void Note(Note note, NoteInterface noteInterface, CategoryInterface categoryInterface, NoteCategoryInterface noteCategoryInterface) {
        Div div_text = new Div();
        Div div_buttons = new Div();

        Button button = new Button(languages[Language.EDIT.getIndex()][language]);
        Button delete_button = new Button(languages[Language.DELETE.getIndex()][language]);

        Button share_button = new Button(languages[Language.SHARE.getIndex()][language]);

        delete_button.addClassName("delete_button");
        button.addClassName("button");
        share_button.addClassName("share_button");

        ComboBox<String> priority = createPriorityInput(false);
        MultiselectComboBox<String> category = createCategoryInput(categoryInterface);

        Icon icon = new Icon(VaadinIcon.PIN);
        Button pin = new Button(icon);
        pin.getStyle().set("background-color", "white");
        pin.getStyle().set("margin-top", "40px");
        pin.getStyle().set("margin-left", "10px");

        String cats = fillCategories(note.getId_(),noteCategoryInterface, categoryInterface).toString();
        TextField categories = new TextField(languages[Language.CATEGORY.getIndex()][language]);
        categories.addClassName("priorities");
        categories.getStyle().set("maxWidth", "100px");
        categories.setValue(cats.substring(1, cats.length() - 1));
        categories.setReadOnly(true);


        TextField priorities = new TextField(languages[Language.PRIORITY.getIndex()][language]);
        priorities.addClassName("priorities");
        priorities.getStyle().set("maxWidth", "50px");
        priorities.setValue(note.getPriority().toString());
        priorities.setReadOnly(true);
        HorizontalLayout horizontal = new HorizontalLayout();

        TextField textField = new TextField(note.getTitle_());
        textField.setValue(note.getText_());
        textField.getStyle().set("minHeight,", "1000px");
        textField.getStyle().set("minWidth", "300px");
        Checkbox done = new Checkbox(languages[Language.DONE.getIndex()][language]);
        done.getStyle().set("margin-left", "5px");

        horizontal.add(textField, priorities, categories, pin);
        horizontal.getStyle().set("width", "100%");
        horizontal.getStyle().set("display", "flex");
        horizontal.getStyle().set("align-items", "center");
        horizontal.getStyle().set("justify-content", "center");

        textField.setReadOnly(true);

        div_text.add(horizontal);
        div_text.getStyle().set("width", "100%");
        div_text.getStyle().set("display", "flex");
        div_text.getStyle().set("align-items", "center");
        div_text.getStyle().set("justify-content", "center");


        Dialog dialog = new Dialog();
        Button save = new Button(languages[Language.SAVE.getIndex()][language]);


        TextArea textArea = new TextArea(note.getTitle_());
        textArea.setValue(note.getText_());
        textArea.setHeight("450px");
        textArea.setWidth("1000px");

        EmailField emailField = new EmailField(languages[Language.SHARE_WITH.getIndex()][language]);
        emailField.setClearButtonVisible(true);
        emailField.setErrorMessage(languages[Language.VALID_EMAIL.getIndex()][language]);

        Button send_mail = new Button(languages[Language.SEND.getIndex()][language]);
        send_mail.addClassName("share_button");

        div_buttons.add(button, delete_button, share_button, done);
        div_buttons.getStyle().set("margin-left", "215px");


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
            deleteCategoryMap(note, noteCategoryInterface);
            noteInterface.deleteById(note.getId_());

            UI.getCurrent().getPage().reload();
        });

        share_button.addClickListener(event -> {
            dialog.open();
            dialog.setWidth("500px");
            dialog.setHeight("250px");
            dialog.add(emailField);
            HorizontalLayout horizont = new HorizontalLayout();

            horizont.add(send_mail);
            dialog.add(horizont);

            send_mail.addClickListener(eventSave -> {
                sendEmail(emailField, note, noteCategoryInterface, categoryInterface);

                UI.getCurrent().getPage().reload();

            });

        });
        add(div_text, div_buttons);
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
        List<Category> categories = categoryInterface.findAll();
        Set<String> set_category = category.getValue();

        List<NoteCategory> note_category_list = Tags.mapCategoryAndNote(note_id, set_category, categories);

        for(NoteCategory note_cat : note_category_list)
            notecategoryInterface.save(note_cat);
    }

    public Set<String> fillCategories(Integer note_id, NoteCategoryInterface notecategoryInterface, CategoryInterface categoryInterface)
    {
        List<Category> cat_entries = categoryInterface.findAll();
        List<NoteCategory> notecat_entries = notecategoryInterface.findAll();

        return Tags.fillCategories(note_id, cat_entries, notecat_entries);
    }

    public Set<String> getCategories(CategoryInterface categoryInterface)
    {
        List<Category> cats = categoryInterface.findAll();
        return Tags.parseCategories(cats);
    }


    public void editCategories(Note note,  NoteCategoryInterface notecategoryInterface, CategoryInterface categoryInterface, MultiselectComboBox<String> category)
    {
        deleteCategoryMap(note, notecategoryInterface);
        mapCategoryToNote(note.getId_(),category,notecategoryInterface,categoryInterface);
    }

    public void deleteCategoryMap(Note note, NoteCategoryInterface noteCategoryInterface)
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


    public boolean emptyCheck(ComboBox<String> priority, MultiselectComboBox<String> category)
    {
        if(!Tags.checkEmpty(priority, category))
            return true;
        Dialog dialog = new Dialog();
        Div div = new Div();
        Button button = new Button(languages[Language.CLOSE.getIndex()][language]);
        TextArea textarea = new TextArea();
        dialog.open();
        textarea.setValue((category.isEmpty()) ? (category.getErrorMessage()) : (priority.getErrorMessage()));
        div.add(textarea,button);
        dialog.add(div);
        button.addClickListener(close_event-> dialog.close());
        return false;
    }


    public ComboBox<String> createPriorityInput(Boolean filter)
    {
        ComboBox<String> priority = new ComboBox<>();
        priority.setLabel(languages[Language.PRIORITY.getIndex()][language]);
        priority.setItems(Tags.setPriorities());
        if(!filter)
        {
            priority.setRequired(true);
            priority.setRequiredIndicatorVisible(true);
            priority.setErrorMessage(languages[Language.PRIORITY_FILLING.getIndex()][language]);
        }
        return priority;
    }

    public MultiselectComboBox<String> createCategoryInput(CategoryInterface categoryInterface)
    {
        MultiselectComboBox<String> category = new MultiselectComboBox<>();
        category.setLabel(languages[Language.CATEGORY.getIndex()][language]);
        category.setLabel(languages[Language.CATEGORIES.getIndex()][language]);
        category.setItems(getCategories(categoryInterface));
        category.setRequired(true);
        category.setRequiredIndicatorVisible(true);
        category.setErrorMessage(languages[Language.CATEGORY_FILLING.getIndex()][language]);
        return category;
    }

    private void initializeCat(CategoryInterface categoryInterface)
    {
        if(categoryInterface.findAll().isEmpty())
        {
            Set<String> cat_names = new HashSet<String>(Arrays.asList(languages[Language.SCHOOL.getIndex()][language],
                    languages[Language.SHOPPING.getIndex()][language], languages[Language.HOME.getIndex()][language],
                    languages[Language.WORK.getIndex()][language], languages[Language.WORKOUT.getIndex()][language]));
            cat_names.forEach(cat_nam -> {
                Category cat = new Category(cat_nam);
                categoryInterface.save(cat);
            });
        }
    }
    public boolean sendEmail(EmailField emailField, Note note, NoteCategoryInterface noteCategoryInterface,
                          CategoryInterface categoryInterface)
    {
        Session session = mailing_service.getSession();
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(mailing_service.getFrom_()));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(emailField.getValue()));
            setFormattedMessageContent(message, note, noteCategoryInterface, categoryInterface);
            Transport sending = session.getTransport("smtp");
            sending.connect(mailing_service.getSmtp_(), mailing_service.getFrom_(), mailing_service.getPassword_());
            sending.sendMessage(message, message.getAllRecipients());
            return true;
        }
        catch(Exception exception) { exception.printStackTrace(); }
        return false;
    }


    public void setFormattedMessageContent(MimeMessage message, Note note, NoteCategoryInterface noteCategoryInterface,
                                           CategoryInterface categoryInterface)
    {
        Set<String> cats = fillCategories(note.getId_(), noteCategoryInterface, categoryInterface);
        mailing_service.setFormattedMessageContent(cats, message, note);
    }



}

