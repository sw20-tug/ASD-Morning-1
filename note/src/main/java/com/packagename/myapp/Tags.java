package com.packagename.myapp;

import com.packagename.myapp.entity.Category;
import com.packagename.myapp.entity.NoteCategory;
import org.vaadin.gatanaso.MultiselectComboBox;
import com.vaadin.flow.component.combobox.ComboBox;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Tags {

    static public Set<String> parseCategories(List<Category> category_list)
    {
        Set<String> categories = new HashSet<>();
        category_list.forEach(
                category -> {
                    categories.add(category.getCategory());
                }
        );
        return categories;
    }

    static public Set<String> fillCategories(Integer note_id, List<Category> cat_entries, List<NoteCategory> notecat_entries)
    {
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

    static public Set<String> setPriorities()
    {
        Set<String> ret_set = new HashSet<>();
        for(int i = 1; i <= 10; i++)
        {
            ret_set.add(Integer.toString(i));
        }
        return ret_set;
    }

    static public boolean checkEmpty(ComboBox<String> pri, MultiselectComboBox<String> cat)
    {
        return pri == null || cat == null || pri.isEmpty() || cat.isEmpty();
    }

    static public List<NoteCategory> mapCategoryAndNote(Integer note_id, Set<String> set_category, List<Category> categories)
    {
        List<NoteCategory> note_category_list = new ArrayList<>();
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
        return note_category_list;
    }



}
