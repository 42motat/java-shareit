package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository {
    Optional<User> getById(long id);

    Collection<User> getAllUsers();

    User create(User user);

    User update(User user);

    void delete(long id);
}
