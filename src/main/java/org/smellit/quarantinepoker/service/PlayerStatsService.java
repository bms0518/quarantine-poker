package org.smellit.quarantinepoker.service;

import org.smellit.quarantinepoker.model.Player;
import org.smellit.quarantinepoker.model.PlayerGameStats;
import org.smellit.quarantinepoker.model.PlayerStats;
import org.smellit.quarantinepoker.repo.PlayerGameStatsRepository;
import org.smellit.quarantinepoker.repo.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

@Service
public class PlayerStatsService {

    private PlayerGameStatsRepository playerGameStatsRepository;

    private PlayerRepository playerRepository;

    @Autowired
    public PlayerStatsService(PlayerGameStatsRepository playerGameStatsRepository, PlayerRepository playerRepository) {
        this.playerGameStatsRepository = playerGameStatsRepository;
        this.playerRepository = playerRepository;
    }

    public PlayerStats getPlayerStats(int playerId) {

        Player player = playerRepository.findById(playerId).get();
        List<PlayerGameStats> allPlayerStats = playerGameStatsRepository.findAllByPlayerId(playerId);

        BigDecimal totalProfit = getTotalProfit(allPlayerStats);
        BigDecimal maxWin = getMaxWin(allPlayerStats);
        BigDecimal worstLoss = getMinWin(allPlayerStats);
        int totalGames = allPlayerStats.size();

        BigDecimal averageProfit = totalGames == 0 ? BigDecimal.ZERO
                : totalProfit.divide(new BigDecimal(totalGames), RoundingMode.DOWN);

        BigDecimal totalBuyIn = getTotalBuyIn(allPlayerStats);
        BigDecimal last3GamesTotalProfit = getTotalProfitByRecentGames(3, allPlayerStats);
        BigDecimal last5GamesTotalProfit = getTotalProfitByRecentGames(5, allPlayerStats);

        int numberWins = getNumberWins(allPlayerStats);
        int numberLosses = getNumberLosses(allPlayerStats);

        PlayerStats playerStats = new PlayerStats();

        playerStats.setPlayerName(player.getName());
        playerStats.setTotalProfit(totalProfit);
        playerStats.setBiggestWin(maxWin);
        playerStats.setWorstLoss(worstLoss);
        playerStats.setTotalGames(totalGames);
        playerStats.setTotalBuyIn(totalBuyIn);
        playerStats.setAverageProfit(averageProfit);
        playerStats.setLast3TotalProfit(last3GamesTotalProfit);
        playerStats.setLast5TotalProfit(last5GamesTotalProfit);
        playerStats.setNumberWins(numberWins);
        playerStats.setNumberLosses(numberLosses);


        return playerStats;
    }


    private int getNumberWins(List<PlayerGameStats> allPlayerStats) {
        int count = 0;
        for (PlayerGameStats playerGameStats : allPlayerStats) {
            if (playerGameStats.getTotalProfit().compareTo(BigDecimal.ZERO) > 0) {
                count++;
            }
        }
        return count;
    }

    private int getNumberLosses(List<PlayerGameStats> allPlayerStats) {
        int count = 0;
        for (PlayerGameStats playerGameStats : allPlayerStats) {
            if (playerGameStats.getTotalProfit().compareTo(BigDecimal.ZERO) < 0) {
                count++;
            }
        }
        return count;
    }

    private BigDecimal getTotalProfitByRecentGames(int numRecentGames, List<PlayerGameStats> allPlayerStats) {

        allPlayerStats.sort(Comparator.comparing(PlayerGameStats::getGameId));

        int max = Math.max(0, allPlayerStats.size() - 1);
        int min = Math.max(0, max - numRecentGames);

        List<PlayerGameStats> playerStatsSubList = allPlayerStats.subList(min, max);
        return getTotalProfit(playerStatsSubList);
    }

    private BigDecimal getMinWin(List<PlayerGameStats> allPlayerStats) {
        return allPlayerStats.stream()
                .map(PlayerGameStats::getTotalProfit)
                .min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
    }

    private BigDecimal getMaxWin(List<PlayerGameStats> allPlayerStats) {
        return allPlayerStats.stream()
                .map(PlayerGameStats::getTotalProfit)
                .max(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
    }

    private BigDecimal getTotalProfit(List<PlayerGameStats> allPlayerStats) {
        return allPlayerStats.stream()
                .map(PlayerGameStats::getTotalProfit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal getTotalBuyIn(List<PlayerGameStats> allPlayerStats) {
        return allPlayerStats.stream()
                .map(PlayerGameStats::getBuyIn)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
