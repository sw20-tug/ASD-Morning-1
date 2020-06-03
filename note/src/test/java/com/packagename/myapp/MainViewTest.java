package com.packagename.myapp;

import com.packagename.myapp.entity.Note;
import com.packagename.myapp.notes.CategoryInterface;
import com.packagename.myapp.notes.NoteCategoryInterface;
import com.packagename.myapp.notes.NoteInterface;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import org.junit.Test;
import org.mockito.*;
import org.springframework.data.domain.Sort;

import java.io.IOException;

enum AppliedFilters{
    PRI_CAT_DATE, PRI_CAT, PRI_DATE, CAT_DATE, PRI, CAT, DATE, NONE, INVALID
}

public class MainViewTest
{
    /*PushNotification push_notification = Mockito.mock(PushNotification.class);
    NoteInterface note_interface = Mockito.mock(NoteInterface.class);
    CategoryInterface categoryInterface = Mockito.mock(CategoryInterface.class);
    NoteCategoryInterface note_category_interface = Mockito.mock(NoteCategoryInterface.class);*/

    @Test
    public void testMainView()
    {
        PushNotification push_notification = Mockito.mock(PushNotification.class);
        NoteInterface note_interface = Mockito.mock(NoteInterface.class);
        CategoryInterface categoryInterface = Mockito.mock(CategoryInterface.class);
        NoteCategoryInterface note_category_interface = Mockito.mock(NoteCategoryInterface.class);



        /*MainView main_view = Mockito.mock(MainView.class);
        UI.setCurrent(Mockito.spy(UI.class));
        MainView main_v = new MainView(push_notification, note_interface, categoryInterface, note_category_interface);
        Mockito.verify(main_view).initializeCat(categoryInterface);
        Mockito.verify(main_view).selectExportImport(Mockito.verify(main_view).languageSelect());*/

    }

    @Test
    public void testSelectExportImport() throws IOException {

        /*PushNotification push_notification = Mockito.mock(PushNotification.class);
        NoteInterface note_interface = Mockito.mock(NoteInterface.class);
        CategoryInterface categoryInterface = Mockito.mock(CategoryInterface.class);
        NoteCategoryInterface note_category_interface = Mockito.mock(NoteCategoryInterface.class);*/

        ExportImport export_import = Mockito.mock(ExportImport.class);
        //UI.setCurrent(Mockito.spy(UI.class));
        //MainView main_view = new MainView(push_notification, note_interface, categoryInterface, note_category_interface);
        exportButtonClick(export_import);
        importButtonClick(export_import);
        Mockito.verify(export_import).exportDatabase();
        Mockito.verify(export_import).importDatabase();

    }

    @Test
    public void testAddInput()
    {
        PushNotification push_notification = Mockito.mock(PushNotification.class);
        NoteInterface note_interface = Mockito.mock(NoteInterface.class);
        CategoryInterface categoryInterface = Mockito.mock(CategoryInterface.class);
        NoteCategoryInterface note_category_interface = Mockito.mock(NoteCategoryInterface.class);

        UI.setCurrent(Mockito.spy(UI.class));
        MainView main_view = new MainView(push_notification, note_interface, categoryInterface, note_category_interface);

        main_view.addInput(push_notification, note_interface, categoryInterface, note_category_interface);


    }

    @Test
    public void testShowunfinishedNotes()
    {
        PushNotification push_notification = Mockito.mock(PushNotification.class);
        NoteInterface note_interface = Mockito.mock(NoteInterface.class);
        CategoryInterface categoryInterface = Mockito.mock(CategoryInterface.class);
        NoteCategoryInterface note_category_interface = Mockito.mock(NoteCategoryInterface.class);

        UI.setCurrent(Mockito.spy(UI.class));
        MainView main_view = new MainView(push_notification, note_interface, categoryInterface, note_category_interface);

        main_view.showunfinishedNotes(note_interface, categoryInterface, note_category_interface);

        Mockito.verify(note_interface, Mockito.atLeast(2)).findAll(Sort.by(Sort.Direction.DESC, "pinned"));
    }

    @Test
    public void testShowfinishedNotes()
    {
        PushNotification push_notification = Mockito.mock(PushNotification.class);
        NoteInterface note_interface = Mockito.mock(NoteInterface.class);
        CategoryInterface categoryInterface = Mockito.mock(CategoryInterface.class);
        NoteCategoryInterface note_category_interface = Mockito.mock(NoteCategoryInterface.class);

        UI.setCurrent(Mockito.spy(UI.class));
        MainView main_view = new MainView(push_notification, note_interface, categoryInterface, note_category_interface);

        main_view.showfinishedNotes(note_interface, categoryInterface, note_category_interface);

        Mockito.verify(note_interface, Mockito.atLeast(2)).findAll(Sort.by(Sort.Direction.DESC, "pinned"));
    }

    enum AppliedFilters{
        PRI_CAT_DATE, PRI_CAT, PRI_DATE, CAT_DATE, PRI, CAT, DATE, NONE, INVALID
    }

    @Test
    public void testApplyFilter()
    {
        PushNotification push_notification = Mockito.mock(PushNotification.class);
        NoteInterface note_interface = Mockito.mock(NoteInterface.class);
        CategoryInterface categoryInterface = Mockito.mock(CategoryInterface.class);
        NoteCategoryInterface note_category_interface = Mockito.mock(NoteCategoryInterface.class);
        Note note = Mockito.mock(Note.class);

        UI.setCurrent(Mockito.spy(UI.class));
        MainView main_view = new MainView(push_notification, note_interface, categoryInterface, note_category_interface);

        main_view.applyFilter(note, note_interface, note_category_interface, categoryInterface, MainView.AppliedFilters.PRI_CAT);
        Mockito.verify(note, Mockito.atLeast(1)).getPriority();

    }

    public void exportButtonClick(ExportImport export_import) throws IOException {

        export_import.exportDatabase();
        /*Button button_export = new Button("Export");
        button_export.click();

        button_export.addClickListener(test ->
        {
            try {
                System.out.println("HALLO");
                export_import.exportDatabase();

            } catch (IOException e) {
                e.printStackTrace();
            }

        });*/
    }

    public void importButtonClick(ExportImport export_import) throws IOException
    {
        export_import.importDatabase();
    }
}
