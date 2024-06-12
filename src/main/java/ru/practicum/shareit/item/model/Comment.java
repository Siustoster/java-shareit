package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @NotNull
    @NotBlank
    @Column(name = "text")
    String text;
    @JoinColumn(name = "author_id")
    @NotNull
    @ManyToOne
    private User user;
    @JoinColumn(name = "item_id")
    @NotNull
    @ManyToOne
    private Item item;
    @NotNull
    private LocalDateTime created;

    public Comment(String text, User user, Item item, LocalDateTime created) {
        this.text = text;
        this.item = item;
        this.user = user;
        this.created = created;
    }
}
