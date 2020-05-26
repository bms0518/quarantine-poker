package org.smellit.quarantinepoker.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PlayerStats {
    private String playerName;
    private BigDecimal totalBuyIn;
    private BigDecimal totalProfit;
    private long totalGames;
    private BigDecimal averageProfit;
    private BigDecimal biggestWin;
    private BigDecimal worstLoss;
    private BigDecimal last3TotalProfit;
    private BigDecimal last5TotalProfit;
    private int numberWins;
    private int numberLosses;
}
