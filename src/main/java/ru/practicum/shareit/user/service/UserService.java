package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

public interface UserService {
    UserDto getById(Long id);

    Collection<UserDto> getAll();

    UserDto create(UserDto userDto);

    UserDto update(Long id, UpdateUserDto updateUserDto);

    void delete(long id);
}
