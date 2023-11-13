package ru.practicum.shareit.item.repository;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByItem_Id(final Integer itemId, Sort sort);
}
