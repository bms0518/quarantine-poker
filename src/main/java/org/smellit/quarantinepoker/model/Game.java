package org.smellit.quarantinepoker.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "game")
public class Game {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "date")
    private LocalDate date;

}


