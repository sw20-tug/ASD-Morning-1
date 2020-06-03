package com.packagename.myapp;

import com.packagename.myapp.entity.Category;
import com.packagename.myapp.entity.NoteCategory;
import com.vaadin.flow.component.combobox.ComboBox;
import org.junit.Assert;
import org.junit.Test;
import org.vaadin.gatanaso.MultiselectComboBox;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.packagename.myapp.MainView.*;
import static com.packagename.myapp.MainView.date_until_;
import static org.junit.Assert.*;
import static org.junit.Assert.assertNull;


public class TagsTest {
    @Test
    public void testSetPriorities()
    {
        Set<String> ret = Tags.setPriorities();
        Set<String> ret_set = new HashSet<>();
        for(int i = 1; i <= 10; i++)
        {
            ret_set.add(Integer.toString(i));
        }
        Assert.assertEquals(ret_set, ret);
    }

   @Test
    public void testCheckEmpty()
    {
        ComboBox<String> combo1 = new ComboBox<>();
        MultiselectComboBox<String> combo2 = new MultiselectComboBox<>();

        assertTrue(Tags.checkEmpty(null, null));

        assertTrue(Tags.checkEmpty(null, combo2));

        assertTrue(Tags.checkEmpty(combo1, combo2));
        combo1.setItems("", "Hallo");
        combo1.setValue("Hallo");

        assertTrue(Tags.checkEmpty(combo1, combo2));

        combo2.setItems("", "Hallo");
        Set<String> items = new HashSet<>();
        items.add("Hallo");
        combo2.setValue(items);

        assertFalse(Tags.checkEmpty(combo1, combo2));
    }

    @Test
    public void testFillCategories()
    {
        int note_id = 0;
        List<Category> cat_list = new ArrayList<>();
        Category cat = new Category();
        cat.setCategory("Test");
        cat.setId_(1);

        cat_list.add(cat);

        List<NoteCategory> note_cat_list = new ArrayList<>();
        NoteCategory note_cat1 = new NoteCategory();
        note_cat1.setFk_note(note_id);
        note_cat1.setFk_category(1);
        note_cat_list.add(note_cat1);

        Set<String> ret = Tags.fillCategories(note_id, cat_list, note_cat_list);
        Set<String> cmp = new HashSet<>();
        cmp.add("Test");
        assertEquals(cmp, ret);

    }
}
