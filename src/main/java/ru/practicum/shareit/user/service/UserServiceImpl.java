package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.Conflict;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserDto getById(long id) {
        User user = userRepository.getById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + id + " не найден"));
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public Collection<UserDto> getAll() {
        Collection<User> users = userRepository.getAllUsers();
        return users.stream()
                .map(UserMapper::mapToUserDto)
                .toList();
        }

    @Override
    public UserDto create(UserDto userDto) {
        User user = UserMapper.mapToUser(userDto);
        validEmailCheck(user.getEmail(), user);
        userRepository.create(user);
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public UserDto update(long id, UpdateUserDto updateUserDto) {
        User updatedUser = userRepository.getById(id)
                .map(user -> UserMapper.updateUserFields(user, updateUserDto))
                .orElseThrow(() -> new NotFoundException("Такой пользователь не найден"));
        validEmailCheck(updatedUser.getEmail(), updatedUser);
        userRepository.update(updatedUser);
        return UserMapper.mapToUserDto(updatedUser);
    }

    @Override
    public void delete(long id) {
        userRepository.delete(id);
    }

    private void validEmailCheck(String email, User updatingUser) {
        boolean emailAlreadyExists = userRepository.getAllUsers()
                .stream()
                .filter(user -> !user.getId().equals(updatingUser.getId()))
                .anyMatch(user -> user.getEmail().equals(email));
        if (emailAlreadyExists) {
            throw new Conflict("Такая электронная почта уже занята");
        }
    }

}
