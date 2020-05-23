package org.smellit.quarantinepoker.model;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "player_game_stats")
public class PlayerGameStats {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "player_id")
    private int playerId;

    @Column(name = "game_id")
    private int gameId;

    @Column(name = "buy_in")
    private BigDecimal buyIn;

    @Column(name = "total_profit")
    private BigDecimal totalProfit;
}
