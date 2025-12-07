package ru.sentidas.country.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import ru.sentidas.country.data.entity.VisitEntity;

import java.util.Date;
import java.util.UUID;

public record Visit(
        @Null
        UUID id,

        @NotNull(message = "userID is required")
        UUID userId,

        @NotNull(message = "country is required")
        Country country,

        @NotNull(message = "Rating is required")
        Integer rating,

        boolean archived,

        @Null
        Date date
) {
    public static Visit fromEntity(VisitEntity entity) {
        return new Visit(
                entity.getId(),
                entity.getUserId(),
                entity.getCountry(),
                entity.getRating(),
                entity.isArchived(),
                entity.getDate()
        );
    }
}
