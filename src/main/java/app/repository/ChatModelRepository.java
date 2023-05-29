package app.repository;

import app.model.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatModelRepository extends RepositoryInterface<Chat> {
  @Query("SELECT c FROM UserModel u JOIN u.chats c WHERE u.id = :id")
  Page<Chat> getChatList(@Param("id") Long userId, Pageable pageable);

  @Query("SELECT c, m FROM UserModel u JOIN u.chats c LEFT JOIN c.messages m " +
    "WHERE u.id = :id AND m.sent = (SELECT MAX(m2.sent) FROM c.messages m2)")
  Page<Object[]> getChatListForPreview(@Param("id") Long userId, Pageable pageable);

//  @Query("SELECT u FROM UserModel u " +
//    "WHERE (LOWER(u.fullName) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
//    "OR (LOWER(u.userTag) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
//    "OR EXISTS (SELECT m FROM Message m WHERE m.user.id = :id AND m.user = u AND LOWER(m.body) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
//    "OR EXISTS (SELECT t FROM Tweet t WHERE t.user.id = :id AND t.user = u AND LOWER(t.body) LIKE LOWER(CONCAT('%', :keyword, '%')))")
//  Page<Object[]> getSearchResults(@Param("id") Long userId, @Param("keyword") String keyword, Pageable pageable);
}
