package ru.sentidas.country.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.sentidas.country.data.entity.VisitEntity;
import ru.sentidas.country.data.projection.CountryRatingProjection;
import ru.sentidas.country.model.CountyRating;

import java.util.List;
import java.util.UUID;

public interface VisitRepository extends JpaRepository<VisitEntity, UUID> {

    Page<VisitEntity> findByUserIdAndArchived(UUID userId, Boolean archived, Pageable pageable);

    Page<VisitEntity> findByUserId(UUID userId, Pageable pageable);

    @Query("""
    SELECT v.country as country, 
    ROUND (AVG(v.rating), 2) as averageRating
    FROM VisitEntity v 
    WHERE v.userId = :userId
    GROUP BY v.country
    ORDER BY AVG(v.rating) DESC
""")
    List<CountryRatingProjection> findAverageRatingByUserIdGroupByCountry(@Param("userId") UUID userId);
}
