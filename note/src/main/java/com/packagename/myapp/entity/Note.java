package com.packagename.myapp.entity;

import javax.persistence.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;

@Entity //Note als Entity und Table definieren
@Table(name = "notes")
public class Note
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //primary key Id wird automatisch um 1 erhoeht
    private Integer ID;

    @NotNull
    private String title;

    private String text;

    @NotNull
    private Boolean pinned;

    private Boolean done;

    private Integer priority;

    private java.util.Date created;

    public Note()
    {
    }

    public Note(String title, String text)
    {
        this.title = title;
        this.text = text;
        this.done = false;
        this.pinned = false;
        created = new Date();
    }

    public Integer getId_()
    {
        return ID;
    }

    public void setId_(Integer id_)
    {
        this.ID = id_;
    }

    public String getTitle_()
    {
        return title;
    }

    public void setTitle_(String title_)
    {
        this.title = title_;
    }

    public String getText_()
    {
        return text;
    }

    public void setText_(String text_)
    {
        this.text = text_;
    }


    public Boolean getPinned() {
        return pinned;
    }

    public void setPinned(Boolean pinned) {
        this.pinned = pinned;
    }
    public Boolean getDone_() {
        return done;
    }

    public void setDone_(Boolean done) {
        this.done = done;

    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
