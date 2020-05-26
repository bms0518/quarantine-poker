package org.smellit.quarantinepoker.service;

import org.smellit.quarantinepoker.model.Player;
import org.smellit.quarantinepoker.model.PlayerStats;
import org.smellit.quarantinepoker.repo.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RankingsService {

    private PlayerStatsService playerStatsService;
    private PlayerRepository playerRepository;

    @Autowired
    public RankingsService(PlayerStatsService playerStatsService, PlayerRepository playerRepository) {
        this.playerStatsService = playerStatsService;
        this.playerRepository = playerRepository;
    }

    public void rankAndPrint(String title, Function<? super PlayerStats, ? extends Comparable> keyExtractor) {
        rankAndPrint(title, keyExtractor, 0);
    }

    public void rankAndPrint(String title, Function<? super PlayerStats, ? extends Comparable> keyExtractor, int minNumGames) {
        List<PlayerStats> playerStatsList = getAllPlayerStats().stream()
                .filter(playerStats -> playerStats.getTotalGames() >= minNumGames)
                .collect(Collectors.toList());
        
        playerStatsList.sort(Comparator.comparing(keyExtractor));
        Collections.reverse(playerStatsList);

        int i = 1;
        StringBuilder sb = new StringBuilder();
        sb.append(title);
        sb.append("\n");
        sb.append("\n");
        for (PlayerStats playerStats : playerStatsList) {
            sb.append(i);
            sb.append(", ");
            sb.append(playerStats.getPlayerName());
            sb.append(", ");
            sb.append(keyExtractor.apply(playerStats));
            sb.append("\n");
            i++;
        }
        sb.append("\n");
        System.err.println(sb.toString());
    }

    private List<PlayerStats> getAllPlayerStats() {
        return playerRepository.findAll()
                .stream()
                .map(player -> playerStatsService.getPlayerStats(player.getId()))
                .collect(Collectors.toList());
    }

}
