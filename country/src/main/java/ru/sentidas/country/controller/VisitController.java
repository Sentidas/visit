package ru.sentidas.country.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.sentidas.country.model.Visit;
import ru.sentidas.country.model.VisitUpdate;
import ru.sentidas.country.service.VisitService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class VisitController {

    private static final Logger LOG = LoggerFactory.getLogger(VisitController.class);


    private final VisitService visitService;

    public VisitController(VisitService visitService) {
        this.visitService = visitService;
    }

    @GetMapping("/users/{userId}/visits")
    public List<Visit> getAllVisits(@PathVariable("userId") UUID userId,
                                    @RequestParam(required = false) Boolean archived,
                                    @RequestParam(defaultValue = "0") @Min(0) int page,
                                    @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
        LOG.info("Called getAllVisits: userId ={}, archived={}, page={}, size={}",
                userId, archived, page, size);
        return visitService.visits(userId, archived, page, size);
    }

    @GetMapping("/visits/{id}")
    @Operation(summary = "Get visit by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Visit found successfully"),
            @ApiResponse(responseCode = "404", description = "Visit not found"),
            @ApiResponse(responseCode = "400", description = "Invalid ID format")
    })
    public Visit visitById(@PathVariable("id") UUID id) {
        return visitService.visitById(id);
    }

    @PostMapping("/visits")
    @ResponseStatus(HttpStatus.CREATED)
    public Visit add(@Valid @RequestBody Visit visit) {
        LOG.info("Called post add");
        return visitService.add(visit);
    }

    @PatchMapping("/visits/{id}")
    public Visit update(@PathVariable UUID id,
                        @Valid @RequestBody VisitUpdate visit) {
        LOG.info("Called update: id ={} ", id);
        return visitService.update(id, visit);
    }

    @DeleteMapping("/visits/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        visitService.delete(id);
    }
}
