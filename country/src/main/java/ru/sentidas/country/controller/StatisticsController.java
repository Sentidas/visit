package ru.sentidas.country.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sentidas.country.data.projection.CountryRatingProjection;
import ru.sentidas.country.model.CountyRating;
import ru.sentidas.country.service.VisitService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class StatisticsController {

    private final VisitService visitService;

    public StatisticsController(VisitService visitService) {
        this.visitService = visitService;
    }

    @GetMapping("{userId}/stat")
    public List<CountryRatingProjection> stat(@PathVariable("userId") UUID userId) {
        return visitService.stat(userId);
    }
}
