package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private int id;
    @NotNull
    private String itemName;
    private String description;
    private Boolean available;
    @NotNull
    private int ownerId;

    public Item(String itemName, String description, boolean available, int ownerId) {
        this.itemName = itemName;
        this.description = description;
        this.available = available;
        this.ownerId = ownerId;
    }
}
