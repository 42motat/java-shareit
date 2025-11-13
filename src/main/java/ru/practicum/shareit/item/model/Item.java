package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

@Data
public class Item {
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private Boolean Available;

    private Long ownerId;

    private ItemRequest request;
}
