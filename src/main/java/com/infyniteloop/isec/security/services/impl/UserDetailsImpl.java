package com.infyniteloop.isec.security.services.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.infyniteloop.isec.security.models.User;
import com.infyniteloop.runningroom.runningroom.entity.Division;
import com.infyniteloop.runningroom.runningroom.entity.RunningRoom;
import com.infyniteloop.runningroom.runningroom.entity.Zone;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Getter
@Setter
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;

    private UUID id;
    private String username;
    private String email;
    private UUID tenantId;
    private UUID runningRoomId;
    private String runningRoomName;
    private String divisionName;
    private String zoneName;

    @JsonIgnore
    private String password;
    private boolean is2faEnabled;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(UUID id, String username, String email, String tenantId, String password,
                           boolean is2faEnabled, Collection<? extends GrantedAuthority> authorities,
                           String runningRoomName, String divisionName, String zoneName) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.tenantId = tenantId != null ? UUID.fromString(tenantId) : null;
        this.password = password;
        this.is2faEnabled = is2faEnabled;
        this.authorities = authorities;
        this.runningRoomName = runningRoomName;
        this.divisionName = divisionName;
        this.zoneName = zoneName;
    }

    public static UserDetailsImpl build(User user, RunningRoom runningRoom) {
        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
                .collect(Collectors.toSet());

        Division division = runningRoom.getDivision();
        Zone zone = division.getZone();

        return new UserDetailsImpl(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.getTenantId() != null ? user.getTenantId().toString() : null,
                user.getPassword(),
                user.isTwoFactorEnabled(),
                authorities,
                runningRoom.getName(),
                division.getName(),
                zone.getName()
        );
    }

}
