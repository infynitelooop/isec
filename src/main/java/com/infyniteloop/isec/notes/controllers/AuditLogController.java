package com.infyniteloop.isec.notes.controllers;


import com.infyniteloop.isec.notes.models.AuditLog;
import com.infyniteloop.isec.notes.services.AuditLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@Tag(name = "Audit Log Controller", description = "APIs for retrieving audit logs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    // Constructor injection
    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    @Operation(summary = "Get all audit logs (Admin only)")
    public List<AuditLog> getAuditLogs(){
        return auditLogService.getAllAuditLogs();
    }

    @GetMapping("/note/{id}")
    @Operation(summary = "Get audit logs for a specific note by ID (Admin only)")
    public List<AuditLog> getNoteAuditLogs(@PathVariable Long id){
        return auditLogService.getAuditLogsForNoteId(id);
    }

}
