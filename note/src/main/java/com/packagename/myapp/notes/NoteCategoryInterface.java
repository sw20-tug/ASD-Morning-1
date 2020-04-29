package com.packagename.myapp.notes;

import com.packagename.myapp.entity.Note;
import com.packagename.myapp.entity.NoteCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface NoteCategoryInterface extends JpaRepository<NoteCategory, Integer> {
/*
    @Modifying
    @Transactional
    @Query("UPDATE Note n SET n.text = ?2, n.title =?3, n.priority =?4 where n.ID = ?1")
    Integer updateNotes(Integer id, String text, String title, Integer priority);

*/
}