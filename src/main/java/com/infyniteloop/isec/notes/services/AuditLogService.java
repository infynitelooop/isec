package com.infyniteloop.isec.notes.services;

import com.infyniteloop.isec.notes.models.AuditAction;
import com.infyniteloop.isec.notes.models.AuditLog;
import com.infyniteloop.isec.notes.models.Note;

import java.util.List;

public interface AuditLogService {
    void auditNote(AuditAction action, String username, Note note);

    List<AuditLog> getAllAuditLogs();

    List<AuditLog> getAuditLogsForNoteId(Long id);
}
