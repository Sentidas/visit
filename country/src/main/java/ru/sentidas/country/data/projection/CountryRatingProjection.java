package ru.sentidas.country.data.projection;

import ru.sentidas.country.model.Country;

public interface CountryRatingProjection {
    Country getCountry();
    double getAverageRating();
}
