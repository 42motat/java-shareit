package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatedItemDto {
    private Long id;

    private String name;

    private String description;

    private Boolean available;

    private Long ownerId;

    public boolean hasName() {
        return !(name == null || name.isBlank());
    }

    public boolean hasDescription() {
        return !(description == null || description.isBlank());
    }

    public boolean isAvailable() {
        return available != null;
    }
}
