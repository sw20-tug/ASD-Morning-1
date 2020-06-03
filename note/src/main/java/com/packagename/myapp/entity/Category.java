package com.packagename.myapp.entity;

import javax.persistence.*;

import javax.validation.constraints.NotNull;

@Entity //Note als Entity und Table definieren
@Table(name = "categories")
public class Category
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //primary key Id wird automatisch um 1 erhoeht
    private Integer id;

    @NotNull

    private String category;

    public Category()
    {
    }

    public Category(String category)
    {
        this.category = category;
    }

    public Integer getId_()
    {
        return id;
    }

    public void setId_(Integer id_)
    {
        this.id = id_;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

}