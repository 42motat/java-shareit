package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private static final String CUSTOM_USER_ID_HEADER = "X-Sharer-User-Id";

    private final RequestClient requestClient;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@RequestHeader(value = CUSTOM_USER_ID_HEADER) @Positive Long userId,
                                          @PathVariable Long id) {
        return requestClient.getById(userId, id);
    }

    // исправить !!
    @GetMapping
    public ResponseEntity<Object> getAllItemRequestsOfUser(@RequestHeader(value = CUSTOM_USER_ID_HEADER)
                                                           @Positive long userId) {
        return requestClient.getAllItemRequestsOfUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequestsOfOtherUsers(@RequestHeader(value = CUSTOM_USER_ID_HEADER)
                                                             @Positive long userId) {
        return requestClient.getAllRequestsOfOtherUsers(userId);
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader(value = CUSTOM_USER_ID_HEADER) @Positive long userId,
                                         @RequestBody @Valid ItemRequestDto itemRequestDto) {
        return requestClient.create(userId, itemRequestDto);
    }
}
