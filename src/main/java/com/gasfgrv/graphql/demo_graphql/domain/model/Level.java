package com.gasfgrv.graphql.demo_graphql.domain.model;

import java.util.Arrays;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Level {

    INITIAL("Iniciante"),
    INTERMEDIARY("Intermediário"),
    ADVANCED("Avançado");

    private final String level;

    public static Level getByValueOf(String level) {
        return Arrays.stream(Level.values()).filter(lvl -> level.equals(lvl.level))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid level"));
    }

}
