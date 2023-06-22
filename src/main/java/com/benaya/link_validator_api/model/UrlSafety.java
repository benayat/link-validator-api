package com.benaya.link_validator_api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Getter
public enum UrlSafety {
    VALID("VALID"), INVALID("INVALID");
    private final String value;
    private static final Map<String, UrlSafety> lookup = Map.of("VALID", VALID, "INVALID", INVALID);
}
