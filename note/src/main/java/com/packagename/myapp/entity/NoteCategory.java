package com.packagename.myapp.entity;

import javax.persistence.*;

import javax.validation.constraints.NotNull;

@Entity //Note als Entity und Table definieren
@Table(name = "notecategory")
public class NoteCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //primary key Id wird automatisch um 1 erhoeht
    private Integer ID;

    @NotNull
    private Integer fk_category;

    private Integer fk_note;


    public NoteCategory() {
    }

    public NoteCategory(Integer note_id, Integer cat_id) {

        fk_note = note_id;
        fk_category = cat_id;
    }


    public Integer getFk_category() {
        return fk_category;
    }

    public void setFk_category(Integer fk_category) {
        this.fk_category = fk_category;
    }

    public Integer getFk_note() {
        return fk_note;
    }

    public void setFk_note(Integer fk_note) {
        this.fk_note = fk_note;
    }

    public Integer getID() {
        return ID;
    }
}


