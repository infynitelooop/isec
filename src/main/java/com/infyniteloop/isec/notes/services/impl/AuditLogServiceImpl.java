package com.infyniteloop.isec.notes.services.impl;


import com.infyniteloop.isec.notes.models.AuditAction;
import com.infyniteloop.isec.notes.models.AuditLog;
import com.infyniteloop.isec.notes.models.Note;
import com.infyniteloop.isec.notes.repository.AuditLogRepository;
import com.infyniteloop.isec.notes.services.AuditLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


//TODO: Add asynchronous saving (@Async) so audit logging doesn’t slow down the request.
//TODO: Add Spring AOP to automatically log create/update/delete events instead of manually calling these methods in your service.
//TODO: If you want JSON diff for updates, log both old and new note contents.
@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    // Constructor injection
    public AuditLogServiceImpl(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public void auditNote(AuditAction action, String username, Note note) {
        AuditLog log = new AuditLog();
        log.setAction(action.name());
        log.setUsername(username);
        log.setNoteId(note.getId());
        log.setNoteContent(note.getContent());
        log.setTimestamp(LocalDateTime.now());
        auditLogRepository.save(log);
    }

    @Override
    public List<AuditLog> getAllAuditLogs() {
        return auditLogRepository.findAll();
    }

    @Override
    public List<AuditLog> getAuditLogsForNoteId(Long id) {
        return auditLogRepository.findByNoteId(id);
    }
}
