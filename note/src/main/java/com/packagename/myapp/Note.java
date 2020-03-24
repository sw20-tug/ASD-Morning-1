package com.packagename.myapp;

import javax.persistence.*;

import javax.validation.constraints.NotNull;

@Entity //Note als Entity und Table definieren
@Table(name = "note")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //primary key Id wird automatisch um 1 erhoeht
    private Integer id_;
    @NotNull
    private String title_;
    @NotNull
    private String text_;

    public Note(Integer id, String title, String text){
        this.id_ = id;
        this.title_ = title;
        this.text_ = text;
    }

    public Integer getId_() {
        return id_;
    }

    public void setId_(Integer id_) {
        this.id_ = id_;
    }

    public String getTitle_() {
        return title_;
    }

    public void setTitle_(String title_) {
        this.title_ = title_;
    }

    public String getText_() {
        return text_;
    }

    public void setText_(String text_) {
        this.text_ = text_;
    }
}
