package com.infyniteloop.runningroom.model.mapper;

import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class EnumMapper {

    public record EnumResponse(String key, String label) {}

    public EnumResponse[] mapEnum(Enum<?>[] values) {
        return Arrays.stream(values)
                .map(v -> new EnumResponse(v.name(), prettify(v.name())))
                .toArray(EnumResponse[]::new);
    }

    private String prettify(String name) {
        return name.replace("_", " ").toUpperCase(); // or Title Case
    }
}