package ru.practicum.shareit.booking;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "start_date")
    private LocalDateTime start;
    @Column(name = "end_date")
    private LocalDateTime end;
    @JoinColumn(name = "ITEM_ID")
    @ManyToOne
    private Item item;
    @JoinColumn(name = "BOOKER_ID")
    @ManyToOne
    private User booker;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    public Booking(LocalDateTime start, LocalDateTime end, Item item, User booker, BookingStatus status) {
        this.start = start;
        this.end = end;
        this.item = item;
        this.booker = booker;
        this.status = status;
    }
}
