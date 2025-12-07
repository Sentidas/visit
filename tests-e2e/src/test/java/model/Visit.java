package model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.UUID;

public record Visit(

        UUID id,
        UUID userId,
        Country country,
        int rating,
        boolean archived,
        String date
) {
}
