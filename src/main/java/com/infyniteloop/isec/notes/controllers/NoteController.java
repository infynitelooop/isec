package com.infyniteloop.isec.notes.controllers;

import com.infyniteloop.isec.notes.models.Note;
import com.infyniteloop.isec.notes.services.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@Tag(name = "Note Controller", description = "APIs for managing user notes")
public class NoteController {

    private final NoteService noteService;

    // Constructor injection
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    @Operation(summary = "Create a new note for the authenticated user")
    public Note createNote(@RequestBody String content,
                           @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return noteService.createNoteForUser(username, content);
    }

    @GetMapping
    @Operation(summary = "Get all notes for the authenticated user")
    public List<Note> getUserNotes(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return noteService.getNotesForUser(username);
    }

    @PutMapping("/{noteId}")
    @Operation(summary = "Update a note for the authenticated user")
    public Note updateNote(@PathVariable Long noteId,
                           @RequestBody String content,
                           @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        return noteService.updateNoteForUser(noteId, content, username);
    }

    @DeleteMapping("/{noteId}")
    @Operation(summary = "Delete a note for the authenticated user")
    public void deleteNote(@PathVariable Long noteId,
                           @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        noteService.deleteNoteForUser(noteId, username);
    }
}