package ru.sentidas.country.service;

import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sentidas.country.data.entity.VisitEntity;
import ru.sentidas.country.data.projection.CountryRatingProjection;
import ru.sentidas.country.data.repository.UserRepository;
import ru.sentidas.country.data.repository.VisitRepository;
import ru.sentidas.country.ex.BadRequestException;
import ru.sentidas.country.ex.UserNotFoundException;
import ru.sentidas.country.ex.VisitNotFoundException;
import ru.sentidas.country.model.Country;
import ru.sentidas.country.model.CountyRating;
import ru.sentidas.country.model.Visit;
import ru.sentidas.country.model.VisitUpdate;

import java.util.Date;
import java.util.List;
import java.util.UUID;


@Service
public class VisitService {

    private static final Logger LOG = LoggerFactory.getLogger(VisitService.class);


    private final VisitRepository visitRepository;
    private final UserRepository userRepository;

    public VisitService(VisitRepository visitRepository,
                        UserRepository userRepository) {
        this.visitRepository = visitRepository;
        this.userRepository = userRepository;
    }

    public List<Visit> visits(UUID userId, @Nullable Boolean archived, int page, int size) {
        // Сортировка задается здесь единоразово так как обеспечивает гибкость изменения сортировки без модификации репозитория
        if (userId == null) {
            throw new BadRequestException("userId is required");
        }

        userRepository.findById(userId).orElseThrow(
                () -> new UserNotFoundException("Cannot find user by id: '" + userId + "'"));


        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "rating"));

        Page<VisitEntity> entityPage;

        if (archived != null) {
            entityPage = visitRepository.findByUserIdAndArchived(userId, archived, pageable);
        } else {
            entityPage = visitRepository.findByUserId(userId, pageable);
        }

        return entityPage.getContent().stream()
                .map(Visit::fromEntity)
                .toList();
    }

    public Visit visitById(UUID id) {
        if (id == null) throw new BadRequestException("id is required");
        VisitEntity visitEntity = visitRepository.findById(id)
                .orElseThrow(() -> new VisitNotFoundException("Cannot visit by id: " + id));
        return Visit.fromEntity(visitEntity);
    }

    @Transactional
    public Visit add(Visit visitJson) {
        if (visitJson.rating() < 1 || visitJson.rating() > 10) {
            throw new BadRequestException("Rating must be between 1 and 10");
        }

        if (!userRepository.existsById(visitJson.userId())) {
            throw new UserNotFoundException("User not found: " + visitJson.userId());
        }

        VisitEntity visitEntity = new VisitEntity();
        visitEntity.setId(UUID.randomUUID());
        visitEntity.setUserId(visitJson.userId());
        visitEntity.setCountry(visitJson.country());
        visitEntity.setRating(visitJson.rating());
        visitEntity.setArchived(false);
        visitEntity.setDate(new Date());
        VisitEntity saved = visitRepository.save(visitEntity);
        LOG.info("Created visit {} for user {}", saved.getId(), saved.getUserId());
        return Visit.fromEntity(saved);
    }

    @Transactional
    public Visit update(UUID id, VisitUpdate visit) {
        if (id == null) {
            throw new BadRequestException("id is required");
        }
        VisitEntity entity = visitRepository.findById(id)
                .orElseThrow(() -> new VisitNotFoundException("Cannot find visit by id: '" + id + "'"));

        if (visit.archived() != null) {
            entity.setArchived(visit.archived());
        }
        if (visit.rating() != null) {
            if (visit.rating() < 1 || visit.rating() > 10) {
                throw new BadRequestException("Rating must be between 1 and 10");
            }
            entity.setRating(visit.rating());
        }
        VisitEntity saved = visitRepository.save(entity);
        LOG.info("Update visit {}", id);
        return Visit.fromEntity(saved);
    }

    @Transactional
    public void delete(UUID id) {
        if (id == null) throw new BadRequestException("id is required");

        boolean exists = visitRepository.existsById(id);
        if (!exists) throw new VisitNotFoundException("Cannot visit by id:" + id);


        visitRepository.deleteById(id);
        LOG.info("Deleted visit {}", id);
    }

    public List<CountryRatingProjection> stat(UUID userId) {
        if (userId == null) throw new BadRequestException("userId is required");

        LOG.debug("Fetching country ratings for user {}", userId);

        return visitRepository.findAverageRatingByUserIdGroupByCountry(userId);
    }
}
