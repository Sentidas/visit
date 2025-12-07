package ru.sentidas.country.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import ru.sentidas.country.data.entity.UserEntity;

import java.util.UUID;

public record User(
        @Null
        UUID id,

        @NotNull
        String username
) {
        public static User fromEntity(UserEntity userEntity) {
                return new User(
                        userEntity.getId(),
                        userEntity.getUsername()
                );
        }
}
