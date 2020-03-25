package com.packagename.myapp.notes;

import com.packagename.myapp.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

public interface NoteInterface extends JpaRepository<Note, Integer>
{
}
