package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    UserDto getById(long id);

    Collection<UserDto> getAll();

    UserDto create(UserDto userDto);

    UserDto update(long id, UpdateUserDto updateUserDto);

    void delete(long id);
}
