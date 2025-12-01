package ru.practicum.shareit.item.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingAndCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdatedItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private static final String CUSTOM_USER_ID_HEADER = "X-Sharer-User-Id";

    private final ItemService itemService;

    @GetMapping("/{id}")
    public ItemBookingAndCommentDto getById(@PathVariable long id, @RequestHeader(value = CUSTOM_USER_ID_HEADER) @Positive long userId) {
        return itemService.getById(id, userId);
    }

    @GetMapping
    public Collection<ItemDto> getAllItems(@RequestHeader(value = CUSTOM_USER_ID_HEADER) @Positive long userId) {
        return itemService.getAll(userId);
    }

    @PostMapping
    public ItemDto create(@RequestHeader(value = CUSTOM_USER_ID_HEADER) @Positive long userId,
                          @RequestBody @Valid ItemDto itemDto) {
        return itemService.create(userId, itemDto);
    }

    @PatchMapping("/{id}")
    public ItemDto update(@PathVariable long id, @RequestHeader(value = CUSTOM_USER_ID_HEADER) @Positive long userId,
                          @RequestBody @Valid UpdatedItemDto updatedItemDto) {
        return itemService.update(id, userId, updatedItemDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        itemService.delete(id);
    }

    @GetMapping("/search")
    public Collection<ItemDto> search(@RequestHeader(value = CUSTOM_USER_ID_HEADER) @Positive long id,
                                      @RequestParam String text) {
        return itemService.search(id, text);
    }

    @PostMapping("/{id}/comment")
    public CommentDto createComment(@PathVariable Long id,
                                    @RequestHeader(value = CUSTOM_USER_ID_HEADER) @Positive long userId,
                                    @RequestBody CommentDto commentDto) {
        return itemService.createComment(id, userId, commentDto.getText());
    }
}
