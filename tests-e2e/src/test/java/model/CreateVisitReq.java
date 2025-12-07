package model;

import java.time.LocalDate;
import java.util.UUID;

public record CreateVisitReq(

        UUID userId,
        Country country,
        int rating
) {
}
