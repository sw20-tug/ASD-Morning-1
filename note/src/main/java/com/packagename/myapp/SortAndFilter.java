package com.packagename.myapp;


import com.packagename.myapp.entity.Note;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;

import java.time.ZoneId;
import java.util.Date;
import java.util.Set;

import static com.packagename.myapp.MainView.*;

public class SortAndFilter {

    static public MainView.AppliedFilters checkFiltering() {
        if (!filter_pri_.isEmpty() && !filter_cat_.isEmpty() && date_from_ != null && date_until_ != null)
            return MainView.AppliedFilters.PRI_CAT_DATE;
        else if (!filter_pri_.isEmpty() && !filter_cat_.isEmpty() && date_from_ == null && date_until_ == null)
            return MainView.AppliedFilters.PRI_CAT;
        else if (!filter_pri_.isEmpty() && filter_cat_.isEmpty() && date_from_ != null && date_until_ != null)
            return MainView.AppliedFilters.PRI_DATE;
        else if (!filter_pri_.isEmpty() && filter_cat_.isEmpty() && date_from_ == null && date_until_ == null)
            return MainView.AppliedFilters.PRI;
        else if (filter_pri_.isEmpty() && !filter_cat_.isEmpty() && date_from_ != null && date_until_ != null)
            return MainView.AppliedFilters.CAT_DATE;
        else if (filter_pri_.isEmpty() && !filter_cat_.isEmpty() && date_from_ == null && date_until_ == null)
            return MainView.AppliedFilters.CAT;
        else if (filter_pri_.isEmpty() && filter_cat_.isEmpty() && date_from_ != null && date_until_ != null)
            return MainView.AppliedFilters.DATE;
        else if (filter_pri_.isEmpty() && filter_cat_.isEmpty() && date_from_ == null && date_until_ == null)
            return MainView.AppliedFilters.NONE;
        return MainView.AppliedFilters.INVALID;
    }

    static public void clearFiltering(ComboBox<String> filter_pri, ComboBox<String> filter_cat){
        if(filter_pri == null) filter_pri_ = "";
        else  filter_pri.setValue(filter_pri_ = "");

        if(filter_cat == null) filter_cat_ = "";
        else filter_cat.setValue(filter_cat_ = "");

        show_unfinished = true;
        date_ = title_= false;
        date_until_ = date_from_ = null;
    }

    static public void keepSortAndFilterOnReload(Checkbox title, Checkbox date, ComboBox<String> filter_pri,
                                                 ComboBox<String> filter_cat, DatePicker filter_date_from,
                                                 DatePicker filter_date_until)
    {
        title_ = title.getValue();
        date_ = date.getValue();
        filter_pri_ = filter_pri.getValue();
        filter_cat_ = filter_cat.getValue();
        date_from_ = filter_date_from.getValue();
        date_until_ = filter_date_until.getValue();
    }

    static public Boolean checkDate(Note note)
    {
        if(date_from_ == null || date_until_ == null)
            return false;
        return (note.getCreated().after( Date.from(date_from_.atStartOfDay(ZoneId.systemDefault()).toInstant()) ) &&
                note.getCreated().before(Date.from(date_until_.atStartOfDay(ZoneId.systemDefault()).toInstant())));
    }

    static public Boolean checkCategory(Set<String> categories_to_id)
    {
        if(categories_to_id == null)
            return false;

        if(filter_cat_.isEmpty())
            return false;

        for(String to_test : categories_to_id)
        {
            if( to_test.equals(filter_cat_))
                return true;
        }
        return false;
    }
}