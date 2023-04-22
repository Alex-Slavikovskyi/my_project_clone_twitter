package app.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "chat_to_users")
@Data
public class ChatsToUsersModel extends BaseEntityModel{
    @Column(name = "chat_id")
    private Long chat_id;
    @Column(name = "user_id")
    private Long user_id;
    @Column(name = "body")
    private String body;
}
