package org.smellit.quarantinepoker.service;

import org.smellit.quarantinepoker.model.PlayerStats;
import org.smellit.quarantinepoker.repo.PlayerRepository;
import org.smellit.quarantinepoker.util.HtmlTableBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
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

    public String rankAndPrint(String title, Function<? super PlayerStats, ? extends Comparable> keyExtractor) {
        return rankAndPrint(title, keyExtractor, 0);
    }

    public String rankAndPrint(String title, Function<? super PlayerStats, ? extends Comparable> keyExtractor, int minNumGames) {
        List<PlayerStats> playerStatsList = getAllPlayerStats().stream()
                .filter(playerStats -> playerStats.getTotalGames() >= minNumGames)
                .collect(Collectors.toList());

        playerStatsList.sort(Comparator.comparing(keyExtractor));
        Collections.reverse(playerStatsList);

        toConsole(title, playerStatsList, keyExtractor);

        return toHtml(title, playerStatsList, keyExtractor);
    }

    private String toHtml(String title, List<PlayerStats> playerStatsList, Function<? super PlayerStats, ? extends Comparable> keyExtractor) {

        List<String> headers = new ArrayList<>();
        headers.add("#");
        headers.add("Name");
        headers.add(title);

        List<List<String>> rows = new ArrayList<>();
        for (PlayerStats playerStats : playerStatsList) {
            List<String> row = new ArrayList<>();
            row.add(playerStats.getPlayerName());

            Object key = keyExtractor.apply(playerStats);
            if (key instanceof BigDecimal) {
                BigDecimal bigDecimal = (BigDecimal) key;
                NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
                String formatted = numberFormat.format(bigDecimal.doubleValue());
                key = formatted;
            }

            row.add(key.toString());
            rows.add(row);
        }

        HtmlTableBuilder htmlTableBuilder = new HtmlTableBuilder(3, headers, rows);
        return htmlTableBuilder.build();
    }

    private String toConsole(String title, List<PlayerStats> playerStatsList, Function<? super PlayerStats, ? extends Comparable> keyExtractor) {

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
        return sb.toString();
    }

    private List<PlayerStats> getAllPlayerStats() {
        return playerRepository.findAll()
                .stream()
                .map(player -> playerStatsService.getPlayerStats(player.getId()))
                .collect(Collectors.toList());
    }

}
