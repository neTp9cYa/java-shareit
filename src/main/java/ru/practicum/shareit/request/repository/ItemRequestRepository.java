package ru.practicum.shareit.request.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.request.model.ItemRequest;

@Repository
public interface ItemRequestRepository extends JpaRepository<ItemRequest, Integer> {
    List<ItemRequest> findByOwner_Id(final int userId);
    List<ItemRequest> findByOwner_IdNot(final int userId, final Pageable pageable);
}
