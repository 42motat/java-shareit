package ru.practicum.shareit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.practicum.shareit.exception.Conflict;
import ru.practicum.shareit.user.dto.UpdateUserDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class ServerUserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void createUserTest() {
        UserDto userDto = new UserDto();
        userDto.setName("test-user");
        userDto.setEmail("test11@email.com");

        UserDto userToCreate = userService.create(userDto);

        assertNotNull(userToCreate.getId());
        assertEquals("test-user", userToCreate.getName());
        assertEquals("test11@email.com", userToCreate.getEmail());
    }

    @Test
    void createUserWithSameEmailTest() {
        UserDto userDto = new UserDto();
        userDto.setName("test-user");
        userDto.setEmail("test19@email.com");

        userService.create(userDto);

        UserDto anotherUserDto = new UserDto();
        anotherUserDto.setName("test-another-user");
        anotherUserDto.setEmail("test19@email.com");

        assertThrows(Conflict.class, () -> userService.create(anotherUserDto));
    }

    @Test
    void updateUserTest() {
        UserDto userDto = new UserDto();
        userDto.setName("test-user");
        userDto.setEmail("test12@email.com");

        UserDto userToCreate = userService.create(userDto);

        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setEmail("test42@email.com");

        UserDto userToUpdate = userService.update(userToCreate.getId(), updateUserDto);

        assertNotNull(userToCreate.getId());
        assertEquals("test-user", userToUpdate.getName());
        assertEquals("test42@email.com", userToUpdate.getEmail());
    }

    @Test
    void updateUserFailTest() {
        UserDto userDto = new UserDto();
        userDto.setName("test-user");
        userDto.setEmail("test44@email.com");

        userService.create(userDto);

        UserDto anotherUserDto = new UserDto();
        anotherUserDto.setName("test-user");
        anotherUserDto.setEmail("test23@email.com");

        UserDto anotherUserToCreate = userService.create(anotherUserDto);

        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setEmail("test44@email.com");

        assertThrows(Conflict.class, () -> userService.update(anotherUserToCreate.getId(), updateUserDto));
    }
}
