package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.Collection;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private static final String CUSTOM_USER_ID_HEADER = "X-Sharer-User-Id";

    private final ItemRequestService itemRequestService;

    @GetMapping("/{id}")
    public ItemRequestWithItemsDto getById(@RequestHeader(value = CUSTOM_USER_ID_HEADER) Long userId,
                                           @PathVariable Long id) {
        return itemRequestService.getById(userId, id);
    }

    // исправить !!
    @GetMapping
    public Collection<ItemRequestWithItemsDto> getAllItemRequestsOfUser(@RequestHeader(value = CUSTOM_USER_ID_HEADER)
                                                                        long userId) {
        return itemRequestService.getAllItemRequestsOfUser(userId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> getAllRequestsOfOtherUsers(@RequestHeader(value = CUSTOM_USER_ID_HEADER)
                                                                 long userId) {
        return itemRequestService.getAllRequestsOfOtherUsers(userId);
    }

    @PostMapping
    public ItemRequestDto create(@RequestHeader(value = CUSTOM_USER_ID_HEADER) long userId,
                                 @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.create(userId, itemRequestDto);
    }
}
