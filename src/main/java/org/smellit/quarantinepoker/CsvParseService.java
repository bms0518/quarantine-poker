package org.smellit.quarantinepoker;

import org.smellit.quarantinepoker.model.Game;
import org.smellit.quarantinepoker.model.PlayerGameStats;
import org.smellit.quarantinepoker.repo.GameRepository;
import org.smellit.quarantinepoker.repo.PlayerGameStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CsvParseService {

    private GameRepository gameRepository;
    private PlayerGameStatsRepository playerGameStatsRepository;

    @Autowired
    public CsvParseService(GameRepository gameRepository, PlayerGameStatsRepository playerGameStatsRepository) {
        this.gameRepository = gameRepository;
        this.playerGameStatsRepository = playerGameStatsRepository;
    }


    public void parse() throws IOException {

        File resource = new ClassPathResource("gamedata.csv").getFile();
        List<String> fileLines = Files.lines(resource.toPath()).collect(Collectors.toList());

        int gameIndex = 0;
        int statsIndex = 0;

        List<Game> gamesList = new ArrayList<>();
        List<PlayerGameStats> playerGameStatsList = new ArrayList<>();

        for (String fileLine : fileLines) {
            List<String> stats = Arrays.asList(fileLine.split(","));

            Iterator<String> statsIterator = stats.iterator();
            String date = statsIterator.next();

            Game game = new Game();
            game.setId(gameIndex);
            MonthDay monthDay = MonthDay.parse(date, DateTimeFormatter.ofPattern("M/dd"));
            game.setDate(monthDay.atYear(2020));

            int playerIndex = 1;
            while (statsIterator.hasNext()) {


                String buyin = statsIterator.next();
                buyin = buyin.replace("$", "");
                String totalProfit = statsIterator.next();
                totalProfit = totalProfit.replace("$", "");

                if (!StringUtils.isEmpty(buyin) && !StringUtils.isEmpty(totalProfit)) {
                    PlayerGameStats playerGameStats = new PlayerGameStats();
                    playerGameStats.setId(statsIndex);
                    playerGameStats.setPlayerId(playerIndex);
                    playerGameStats.setGameId(gameIndex);
                    playerGameStats.setTotalProfit(new BigDecimal(totalProfit));
                    playerGameStats.setBuyIn(new BigDecimal(buyin));
                    playerGameStatsList.add(playerGameStats);
                }

                playerIndex++;
                statsIndex++;

            }
            gamesList.add(game);
            gameIndex++;
        }

        System.err.println(gamesList);
        System.err.println(playerGameStatsList);
        System.err.println();
        
        gameRepository.saveAll(gamesList);
        playerGameStatsRepository.saveAll(playerGameStatsList);
        
    }
}
