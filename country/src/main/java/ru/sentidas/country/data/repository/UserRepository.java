package ru.sentidas.country.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sentidas.country.data.entity.UserEntity;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {


    List<UserEntity> findAll();


}
