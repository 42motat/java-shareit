package ru.practicum.shareit.request.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemForItemRequestDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repostitory.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    public ItemRequestWithItemsDto getById(Long userId, Long itemRequestId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        ItemRequest itemRequest = itemRequestRepository.findById(itemRequestId)
                .orElseThrow(() -> new NotFoundException("Запрос не найден"));

        List<Item> itemList = itemRepository.findByRequestId(itemRequest.getId());
        List<ItemForItemRequestDto> itemsForItemRequestDto = itemList
                .stream()
                .map(ItemMapper::mapToItemForRequestDto)
                .toList();

        return ItemRequestMapper.mapToItemRequestWithItems(itemRequest, itemsForItemRequestDto);
    }

    @Override
    public Collection<ItemRequestWithItemsDto> getAllItemRequestsOfUser(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        Collection<ItemRequest> requests = itemRequestRepository.findAllByRequestorId(userId);

        return requests
                .stream()
                .map(itemRequest -> {
                    List<Item> items = itemRepository.findByRequestId(itemRequest.getId());
                    return ItemRequestMapper.mapToItemRequestWithItems(itemRequest,
                                                    items.stream().map(ItemMapper::mapToItemForRequestDto).toList());
                })
                .toList();
   }

    @Override
    public Collection<ItemRequestWithItemsDto> getAllRequestsOfOtherUsers(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        Collection<ItemRequest> requests = itemRequestRepository.findAllByRequestorIdNot(userId);

        return requests
                .stream()
                .map(itemRequest -> {
                    List<Item> items = itemRepository.findByRequestId(itemRequest.getId());
                    return ItemRequestMapper.mapToItemRequestWithItems(itemRequest,
                            items.stream().map(ItemMapper::mapToItemForRequestDto).toList());
                })
                .toList();
    }

    @Override
    @Transactional
    public ItemRequestDto create(Long userId, ItemRequestDto itemRequestDto) {
        User requestor = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        ItemRequest itemRequest = ItemRequestMapper.mapToItemRequest(requestor, itemRequestDto);
        itemRequest.setCreated(LocalDateTime.now());

        return ItemRequestMapper.mapToItemRequestDto(itemRequestRepository.save(itemRequest));
    }
}
