package ru.practicum.shareit.request.dto;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.dto.ItemForItemRequestDto;

import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@Setter
public class ItemRequestWithItemsDto {
    private Long id;
    private String description;
    private Long requestorId;
    private LocalDateTime created;
    private Collection<ItemForItemRequestDto> items;
}
