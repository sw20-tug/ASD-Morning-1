package com.packagename.myapp.notes;

import com.packagename.myapp.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

public interface CategoryInterface extends JpaRepository<Category, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Category c SET c.category = ?2, c.id = ?1")
    Integer updateNotes(Integer id, String category);


}