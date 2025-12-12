package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdatedItemDto;

import java.util.ArrayList;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private static final String CUSTOM_USER_ID_HEADER = "X-Sharer-User-Id";

    private final ItemClient itemClient;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable long id,
                                          @RequestHeader(value = CUSTOM_USER_ID_HEADER) @Positive long userId) {
        return itemClient.getById(id, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItems(@RequestHeader(value = CUSTOM_USER_ID_HEADER) @Positive long userId) {
        return itemClient.getAllItems(userId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(value = CUSTOM_USER_ID_HEADER) @Positive long userId,
                                         @RequestBody @Valid ItemDto itemDto) {
        return itemClient.create(userId, itemDto);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable long id, @RequestHeader(value = CUSTOM_USER_ID_HEADER) @Positive long userId,
                                         @RequestBody @Valid UpdatedItemDto updatedItemDto) {
        return itemClient.update(id, userId, updatedItemDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        itemClient.delete(id);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> search(@RequestHeader(value = CUSTOM_USER_ID_HEADER) @Positive long id,
                                         @RequestParam String text) {
        if (text.isBlank()) {
            return ResponseEntity.ok(new ArrayList<>());
        } else {
            return itemClient.search(id, text);
        }
    }

    @PostMapping("/{id}/comment")
    public ResponseEntity<Object> createComment(@PathVariable Long id,
                                    @RequestHeader(value = CUSTOM_USER_ID_HEADER) @Positive long userId,
                                    @RequestBody CommentDto commentDto) {
        return itemClient.createComment(id, userId, commentDto);
    }
}
