package com.infyniteloop.isec.notes.services.impl;

import com.infyniteloop.isec.notes.models.AuditAction;
import com.infyniteloop.isec.notes.models.Note;
import com.infyniteloop.isec.notes.repository.NoteRepository;
import com.infyniteloop.isec.notes.services.AuditLogService;
import com.infyniteloop.isec.notes.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final AuditLogService auditLogService;

    // Constructor injection
    public NoteServiceImpl(NoteRepository noteRepository, AuditLogService auditLogService) {
        this.noteRepository = noteRepository;
        this.auditLogService = auditLogService;
    }

    @Override
    public Note createNoteForUser(String username, String content) {
        Note note = new Note();
        note.setContent(content);
        note.setOwnerUsername(username);
        auditLogService.auditNote(AuditAction.CREATE, username, note);
        return noteRepository.save(note);
    }

    @Override
    public Note updateNoteForUser(Long noteId, String content, String username) {
        Note note = noteRepository.findById(noteId).orElseThrow(()
                -> new RuntimeException("Note not found"));
        note.setContent(content);
        auditLogService.auditNote(AuditAction.UPDATE, username, note);
        return noteRepository.save(note);
    }

    @Override
    public void deleteNoteForUser(Long noteId, String username) {

        Note note = noteRepository.findById(noteId).orElseThrow(()
                -> new RuntimeException("Note not found"));
        auditLogService.auditNote(AuditAction.DELETE, username, note);
        noteRepository.deleteById(noteId);
    }

    @Override
    public List<Note> getNotesForUser(String username) {
        return noteRepository
                .findByOwnerUsername(username);
    }
}

