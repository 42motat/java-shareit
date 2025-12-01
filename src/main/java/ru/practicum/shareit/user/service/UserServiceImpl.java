package ru.practicum.shareit.user.service;

import jakarta.transaction.Transactional;
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
    public UserDto getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id=" + id + " не найден"));
        return UserMapper.mapToUserDto(user);
    }

    @Override
    public Collection<UserDto> getAll() {
        Collection<User> users = userRepository.findAll();
        return users.stream()
                .map(UserMapper::mapToUserDto)
                .toList();
        }

    @Override
    @Transactional
    public UserDto create(UserDto userDto) {
        User user = UserMapper.mapToUser(userDto);
        validEmailCheck(user.getEmail(), user);
        userRepository.save(user);
        return UserMapper.mapToUserDto(user);
    }

    @Override
    @Transactional
    public UserDto update(Long id, UpdateUserDto updateUserDto) {
        User updatedUser = userRepository.findById(id)
                .map(user -> UserMapper.updateUserFields(user, updateUserDto))
                .orElseThrow(() -> new NotFoundException("Такой пользователь не найден"));
        validEmailCheck(updatedUser.getEmail(), updatedUser);
        userRepository.save(updatedUser);
        return UserMapper.mapToUserDto(updatedUser);
    }

    @Override
    public void delete(long id) {
        userRepository.deleteById(id);
    }

    private void validEmailCheck(String email, User updatingUser) {
        boolean emailAlreadyExists = userRepository.findAll()
                .stream()
                .filter(user -> !user.getId().equals(updatingUser.getId()))
                .anyMatch(user -> user.getEmail().equals(email));
        if (emailAlreadyExists) {
            throw new Conflict("Такая электронная почта уже занята");
        }
    }
}
