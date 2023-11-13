package ru.practicum.shareit.item.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    List<Item> findByOwner_Id(final Integer userId);

    @Query("select item " +
        "from Item as item " +
        "where item.available = true and (lower(item.name) like lower(concat('%', :text,'%')) or lower(description) like lower(concat('%', :text,'%')))")
    List<Item> search(final String text);

}
