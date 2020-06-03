package com.packagename.myapp;

import com.packagename.myapp.entity.NoteCategory;
import org.junit.Test;
import static com.helger.commons.mock.CommonsAssert.assertEquals;

public class NoteCategoryTest {

    @Test
    public void testCategoryConstructor(){
        NoteCategory test_category = new NoteCategory(5, 3);
        int note_id = test_category.getFk_note();
        int cat_id = test_category.getFk_category();

        assertEquals(5, note_id);
        assertEquals(3, cat_id);
    }
}
