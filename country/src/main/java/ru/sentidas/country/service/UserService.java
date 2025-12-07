package ru.sentidas.country.service;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sentidas.country.data.entity.UserEntity;
import ru.sentidas.country.data.repository.UserRepository;
import ru.sentidas.country.ex.BadRequestException;
import ru.sentidas.country.model.User;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final static Logger LOG = LoggerFactory.getLogger(UserService.class);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User createUser(@Valid User user) {
        if (user.username() == null) throw new BadRequestException("username is required");

        UserEntity userEntity = new UserEntity();
        userEntity.setId(UUID.randomUUID());
        userEntity.setUsername(user.username());

        userRepository.save(userEntity);

        LOG.info("Created user with id: {} and username: {}",
                userEntity.getId(), userEntity.getUsername());
        return User.fromEntity(userEntity);
    }

    @Transactional(readOnly = true)
    public List<User> getUsers() {
        return userRepository.findAll().stream()
                .map(User::fromEntity)
                .toList();
    }
}
