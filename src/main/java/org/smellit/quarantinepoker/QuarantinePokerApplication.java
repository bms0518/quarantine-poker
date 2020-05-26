package org.smellit.quarantinepoker;

import org.smellit.quarantinepoker.model.PlayerStats;
import org.smellit.quarantinepoker.repo.PlayerRepository;
import org.smellit.quarantinepoker.service.PlayerStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableTransactionManagement
public class QuarantinePokerApplication implements CommandLineRunner {

    private PlayerStatsService playerStatsService;
    private PlayerRepository playerRepository;

    @Autowired
    public QuarantinePokerApplication(PlayerStatsService playerStatsService, PlayerRepository playerRepository) {
        this.playerStatsService = playerStatsService;
        this.playerRepository = playerRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(QuarantinePokerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        List<PlayerStats> playerStatsList = playerRepository.findAll()
                .stream()
                .map(player -> playerStatsService.getPlayerStats(player.getId()))
                .collect(Collectors.toList());

        playerStatsList.sort(Comparator.comparing(PlayerStats::getTotalProfit));

        int i = 1;
        StringBuilder sb = new StringBuilder();
        for (PlayerStats playerStats : playerStatsList) {
            sb.append(i);
            sb.append(", ");
            sb.append(playerStats.getPlayerName());
            sb.append(", ");
            sb.append(playerStats.getTotalProfit());
            sb.append("\n");
            i++;
        }
        System.err.println(sb.toString());
    }
}
