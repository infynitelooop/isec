package com.infyniteloop.isec.security.mapper;

import com.infyniteloop.isec.security.dtos.UserResponse;
import com.infyniteloop.isec.security.models.Role;
import com.infyniteloop.isec.security.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "roles", source = "roles", qualifiedByName = "rolesToStrings")
    UserResponse toResponse(User user);

    @Named("rolesToStrings")
    default Set<String> rolesToStrings(Set<Role> roles) {
        if (roles == null) return null;
        return roles.stream()
                .map(Role::getRoleName)
                .map(Enum::name)
                .collect(Collectors.toSet());
    }
}
