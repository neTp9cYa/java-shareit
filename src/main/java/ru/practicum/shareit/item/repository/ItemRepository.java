package ru.practicum.shareit.item.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    List<Item> findByOwner_Id(final Integer userId);

    List<Item> findByRequest_Id(final Integer requestId);

    @Query("select item " +
        "from Item as item " +
        "where item.available = true and (lower(item.name) like lower(concat('%', :text,'%')) or lower(description) like lower(concat('%', :text,'%')))")
    @EntityGraph(attributePaths = {"owner"})
    List<Item> search(final String text, final Pageable pageable);

    @EntityGraph(attributePaths = {"request"})
    List<Item> findByRequest_Owner_Id(final int userId);

    @EntityGraph(attributePaths = {"request"})
    List<Item> findByRequest_Owner_IdNot(final int userId);

}
