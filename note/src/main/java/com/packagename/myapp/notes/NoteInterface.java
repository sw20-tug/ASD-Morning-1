package com.packagename.myapp.notes;

import com.packagename.myapp.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

public interface NoteInterface extends JpaRepository<Note, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE Note n SET n.text = ?2, n.title =?3, n.done = ?4 where n.ID = ?1")
    Integer updateNotes(Integer id, String text, String title, Boolean done);
}
