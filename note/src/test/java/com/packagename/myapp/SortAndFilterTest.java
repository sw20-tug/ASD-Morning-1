package com.packagename.myapp;

import com.packagename.myapp.entity.Note;
import com.packagename.myapp.entity.NoteCategory;
import com.packagename.myapp.notes.CategoryInterface;
import com.packagename.myapp.notes.NoteCategoryInterface;
import com.packagename.myapp.notes.NoteInterface;
import com.packagename.myapp.MainView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.page.Push;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

import static com.packagename.myapp.MainView.*;
import static com.packagename.myapp.SortAndFilter.*;

public class SortAndFilterTest {

    @Test
    public void testCheckCategory()
    {
        //Test if filter_cat is empty
        Set<String> categories = new HashSet<>();
        categories.add("Test");
        assertFalse(checkCategory(categories));

        filter_cat_ = "Test";

        //test if passed null
        assertFalse(checkCategory(null));

        //Check if arguments match
        assertTrue(checkCategory(categories));
        categories.clear();
        categories.add("hallo");
        assertFalse(checkCategory(categories));
    }

    @Test
    public void testCheckDate()
    {
        Note mocked_note = Mockito.spy(Note.class);

        assertFalse(SortAndFilter.checkDate(mocked_note));

        date_until_ = LocalDate.now();
        date_from_ = LocalDate.now();
        mocked_note.setCreated(new Date());
        checkDate(mocked_note);
        Mockito.verify(mocked_note, Mockito.times(2)).getCreated();
    }

    @Test
    public void testCheckFiltering()
    {
        assertEquals(AppliedFilters.NONE, checkFiltering());

        filter_pri_ = "E";
        filter_cat_ = "E";
        date_from_ = LocalDate.now();
        date_until_ = LocalDate.now();

        assertEquals(AppliedFilters.PRI_CAT_DATE, checkFiltering());

        date_from_ = date_until_ = null;

        assertEquals(AppliedFilters.PRI_CAT, checkFiltering());

        filter_cat_ = "";

        assertEquals(AppliedFilters.PRI, checkFiltering());

        filter_pri_ = "";
        filter_cat_ = "E";
        date_from_ = date_until_ = LocalDate.now();

        assertEquals(AppliedFilters.CAT_DATE, checkFiltering());
        date_from_ = date_until_ = null;

        assertEquals(AppliedFilters.CAT, checkFiltering());

        filter_cat_ = "";
        date_from_ = date_until_ = LocalDate.now();
        assertEquals(AppliedFilters.DATE, checkFiltering());

        date_from_ = null;

        assertEquals(AppliedFilters.INVALID, checkFiltering());

        filter_cat_ = "E";
        assertEquals(AppliedFilters.INVALID, checkFiltering());
    }

     @Test
    public void clearFiltering()
     {
         ComboBox<String> combo1 = new ComboBox<>();
         ComboBox<String> combo2 = new ComboBox<>();
         combo1.setItems("", "H");
         combo2.setItems("", "H");

         filter_pri_ = "E";
         SortAndFilter.clearFiltering(null, null);
         assertEquals(filter_pri_, "");
         assertEquals(filter_cat_ , "");

         filter_pri_ = "U";

         SortAndFilter.clearFiltering(combo1, combo2);
         assertEquals(filter_pri_, "");
         assertEquals(filter_cat_, "");

         combo1.setValue(filter_pri_ = "H");
         combo1.setValue(filter_cat_ = "H");

         SortAndFilter.clearFiltering(combo1, combo2);
         assertEquals(filter_pri_, "");
         assertEquals(filter_cat_, "");

        show_unfinished = false;
        date_ = title_ = true;
        date_from_ = date_until_ = LocalDate.now();

        SortAndFilter.clearFiltering(combo1, combo2);
        assertTrue( show_unfinished);
        assertFalse(date_ && title_);
        assertNull(date_from_);
        assertNull(date_until_);

     }
}
