package com.infyniteloop.isec.notes.repository;

import com.infyniteloop.isec.notes.models.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findByOwnerUsername(String author);
}
