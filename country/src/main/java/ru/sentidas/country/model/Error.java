package ru.sentidas.country.model;

public record Error(
        String type,
        String title,
        int status,
        String detail,
        String instance
) {
}
