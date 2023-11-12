package ru.practicum.shareit.item.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
    @Query("select comment " +
        "from Comment as comment " +
        "where comment.item.id = :itemId " +
        "order by comment.id")
    List<Comment> findByItem(final Integer itemId);
}
