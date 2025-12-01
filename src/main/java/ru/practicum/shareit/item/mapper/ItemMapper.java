package ru.practicum.shareit.item.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingAndCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdatedItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {

    public static Item mapToItem(long userId, ItemDto itemDto) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwnerId(userId);
        return item;
    }

    public static ItemDto mapToItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setOwnerId(item.getOwnerId());
        return itemDto;
    }

    public static ItemBookingAndCommentDto mapToItemWithBookingAndComments(Item item, List<CommentDto> comments,
                                                                           BookingItemDto lastBooking,
                                                                           BookingItemDto nextBooking) {
        ItemBookingAndCommentDto fullDto = new ItemBookingAndCommentDto();
        fullDto.setId(item.getId());
        fullDto.setName(item.getName());
        fullDto.setDescription(item.getDescription());
        fullDto.setAvailable(item.getAvailable());
        fullDto.setOwnerId(item.getOwnerId());
        fullDto.setLastBooking(lastBooking);
        fullDto.setNextBooking(nextBooking);
        fullDto.setComments(comments);
        return fullDto;
    }

    public static Item updateItemFields(Item item, UpdatedItemDto updatedItemDto) {
        if (updatedItemDto.hasName()) {
            item.setName(updatedItemDto.getName());
        }

        if (updatedItemDto.hasDescription()) {
            item.setDescription(updatedItemDto.getDescription());
        }

        if (updatedItemDto.isAvailable()) {
            item.setAvailable(updatedItemDto.getAvailable());
        }

        return item;
    }
}
