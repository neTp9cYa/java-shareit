package ru.practicum.shareit.request;

import java.time.Instant;
import ru.practicum.shareit.user.model.User;

public class ItemRequest {
    private int id;
    private String description;
    private User requestor;
    private Instant created;
}
