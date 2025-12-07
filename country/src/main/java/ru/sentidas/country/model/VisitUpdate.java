package ru.sentidas.country.model;

import com.fasterxml.jackson.annotation.JsonInclude;

public record VisitUpdate(
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Integer rating,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Boolean archived
) {
}
