package ru.practicum.shareit.booking.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    @Query("select booking " +
        "from Booking as booking " +
        "where booking.booker.id = :userId " +
        "order by booking.start desc")
    List<Booking> findOwn(final Integer userId);

    @Query("select booking " +
        "from Booking as booking " +
        "where booking.booker.id = :userId and booking.status = :status " +
        "order by booking.start desc")
    List<Booking> findOwn(final Integer userId, final BookingStatus status);

    @Query("select count(booking.id) " +
        "from Booking as booking " +
        "where booking.booker.id = :userId and booking.item.id = :itemId and booking.start < :now")
    int usedCount(final Integer userId, final Integer itemId, final LocalDateTime now);

    @Query("select booking " +
        "from Booking as booking " +
        "where booking.booker.id = :userId and booking.end < :now " +
        "order by booking.start desc")
    List<Booking> findOwnInPast(final Integer userId, final LocalDateTime now);

    @Query("select booking " +
        "from Booking as booking " +
        "where booking.booker.id = :userId and booking.start >= :now and booking.end <= :now " +
        "order by booking.start desc")
    List<Booking> findOwnCurrent(final Integer userId, final LocalDateTime now);

    @Query("select booking " +
        "from Booking as booking " +
        "where booking.booker.id = :userId and booking.start > :now " +
        "order by booking.start desc")
    List<Booking> findOwnInFuture(final Integer userId, final LocalDateTime now);


    @Query("select booking " +
        "from Booking as booking " +
        "where booking.item.owner.id = :userId " +
        "order by booking.start desc")
    List<Booking> findByItemOwner(final Integer userId);

    @Query("select booking " +
        "from Booking as booking " +
        "where booking.item.owner.id = :userId and booking.status = :status " +
        "order by booking.start desc")
    List<Booking> findByItemOwner(final Integer userId, BookingStatus status);

    @Query("select booking " +
        "from Booking as booking " +
        "where booking.item.owner.id = :userId and booking.end < :now " +
        "order by booking.start desc")
    List<Booking> findByItemOwnerInPast(final Integer userId, final LocalDateTime now);

    @Query("select booking " +
        "from Booking as booking " +
        "where booking.item.owner.id = :userId and booking.start >= :now and booking.end <= :now " +
        "order by booking.start desc")
    List<Booking> findByItemOwnerCurrent(final Integer userId, final LocalDateTime now);

    @Query("select booking " +
        "from Booking as booking " +
        "where booking.item.owner.id = :userId and booking.start > :now " +
        "order by booking.start desc")
    List<Booking> findByItemOwnerInFuture(final Integer userId, final LocalDateTime now);

    @Query("select booking " +
        "from Booking as booking " +
        "where booking.item.id = :itemId " +
        "order by booking.start desc")
    List<Booking> findByItem(final Integer itemId);

}