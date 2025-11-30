package ru.practicum.shareit.item.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemBookingAndCommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.UpdatedItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repostitory.CommentRepository;
import ru.practicum.shareit.item.repostitory.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Override
    public ItemBookingAndCommentDto getById(long id, long userId) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Вещь с id=" + id + " не найдена"));

        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Такой пользователь не найден"));

        List<CommentDto> comments = commentRepository.findByItemId(item.getId())
                                                     .stream()
                                                     .map(CommentMapper::mapToCommentDto)
                                                     .toList();

        BookingItemDto lastBooking = null;
        BookingItemDto nextBooking = null;

        if (item.getOwnerId().equals(userId)) {
            List<Booking> lastBookingList = bookingRepository.findByItemIdAndEndBefore(item.getId(), LocalDateTime.now());
            if (!lastBookingList.isEmpty()) {
                lastBooking = BookingMapper.mapToBookingItemDto(lastBookingList.getFirst());
            }

            List<Booking> nextBookingList = bookingRepository.findByItemIdAndStartAfter(item.getId(), LocalDateTime.now());
            if (!nextBookingList.isEmpty()) {
                nextBooking = BookingMapper.mapToBookingItemDto(nextBookingList.getFirst());
            }
        }

        return ItemMapper.mapToItemWithBookingAndComments(item, comments, lastBooking, nextBooking);
    }

    @Override
    public Collection<ItemDto> getAll(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Такой пользователь не найден"));
        Collection<Item> items = itemRepository.findByOwnerId(userId);
        return items.stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    @Override
    @Transactional
    public ItemDto create(long userId, ItemDto itemDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Такой пользователь не найден"));
        Item item = ItemMapper.mapToItem(user.getId(), itemDto);
        itemRepository.save(item);
        return ItemMapper.mapToItemDto(item);
    }

    @Override
    @Transactional
    public ItemDto update(long id, long userId, UpdatedItemDto updatedItemDto) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Такой пользователь не найден"));

        Item updatingItem = itemRepository.findById(id)
                .map(item -> ItemMapper.updateItemFields(item, updatedItemDto))
                .orElseThrow(() -> new NotFoundException("Такая вещь не найдена"));

        long itemOwnerId = updatingItem.getOwnerId();

        if (itemOwnerId != userId) {
            throw new BadRequest("Вы не можете обновить вещь, которая вам не принадлежит");
        }

        itemRepository.save(updatingItem);
        return ItemMapper.mapToItemDto(updatingItem);
    }

    @Override
    public void delete(long id) {
        itemRepository.deleteById(id);
    }

    @Override
    public Collection<ItemDto> search(long id, String text) {
        if (text.isEmpty()) {
            return List.of();
        }
        return itemRepository.search(id, text)
                .stream()
                .map(ItemMapper::mapToItemDto)
                .toList();
    }

    @Override
    @Transactional
    public CommentDto createComment(long itemId, long userId, String text) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Такая вещь не найдена"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Такой пользователь не найден"));

        Optional<Booking> bookingForComment = bookingRepository
            .findFirstByItemIdAndBookerIdAndStatusAndEndBefore(itemId, userId, BookingStatus.APPROVED, LocalDateTime.now());

        if (bookingForComment.isEmpty()) {
            throw new BadRequest("Нет завершённого бронирования для указанной вещи");
        }

        Comment comment = CommentMapper.mapToComment(item, user, text);

        commentRepository.save(comment);

        return CommentMapper.mapToCommentDto(comment);
    }
}
