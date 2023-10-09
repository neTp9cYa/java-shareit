package ru.practicum.shareit.booking;

import java.time.LocalDateTime;
import ru.practicum.shareit.item.model.Item;

public class Booking {
    private int id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Item item;
    private int booker;
    private BookingStatus status;
}
