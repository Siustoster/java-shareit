package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "item_name")
    @NotNull
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "available")
    private Boolean available;
    @JoinColumn(name = "OWNER_ID")
    @NotNull
    @ManyToOne
    private User user;

    public Item(String itemName, String description, boolean available, User user) {
        this.name = itemName;
        this.description = description;
        this.available = available;
        this.user = user;
    }
}
