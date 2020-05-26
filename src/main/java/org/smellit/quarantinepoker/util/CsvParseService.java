package org.smellit.quarantinepoker.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smellit.quarantinepoker.model.Game;
import org.smellit.quarantinepoker.model.PlayerGameStats;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.MonthDay;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CsvParseService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CsvParseService.class);

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
        buildInsertStatements(gamesList, playerGameStatsList);
    }

    private void buildInsertStatements(List<Game> gamesList, List<PlayerGameStats> playerGameStatsList) {
        Map<Integer, String> insertGameStatementsByGameId = gamesList.stream()
                .collect(Collectors.toMap(Game::getId, this::buildInsertGameStatement));

        Map<Integer, List<String>> playerGameStatementsByGameId = new HashMap<>();

        for (PlayerGameStats playerGameStats : playerGameStatsList) {
            String insertPlayerGameStats = buildInsertPlayerGameStatsStatement(playerGameStats);
            playerGameStatementsByGameId.putIfAbsent(playerGameStats.getGameId(), new ArrayList<>());
            playerGameStatementsByGameId.get(playerGameStats.getGameId()).add(insertPlayerGameStats);
        }

        prettyPrintAllStatements(insertGameStatementsByGameId, playerGameStatementsByGameId);
    }

    private void prettyPrintAllStatements(Map<Integer, String> insertGameStatementsByGameId,
                                          Map<Integer, List<String>> playerGameStatementsByGameId) {
        StringBuilder allStatements = new StringBuilder();

        for (Map.Entry<Integer, String> entry : insertGameStatementsByGameId.entrySet()) {
            String insertGameStatement = entry.getValue();
            List<String> playerGameStats = playerGameStatementsByGameId.get(entry.getKey());

            allStatements.append(insertGameStatement);
            allStatements.append("\n");
            allStatements.append("\n");
            for (String playerGameInsertStatement : playerGameStats) {
                allStatements.append(playerGameInsertStatement);
                allStatements.append("\n");
            }
            allStatements.append("\n");
            allStatements.append("\n");
        }

        LOGGER.info(allStatements.toString());

    }

    private String buildInsertGameStatement(Game game) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO game (id,date) VALUES ");
        sb.append("(");
        sb.append(game.getId());
        sb.append(",");
        sb.append("'");
        sb.append(game.getDate());
        sb.append("');");
        return sb.toString();
    }

    private String buildInsertPlayerGameStatsStatement(PlayerGameStats playerGameStats) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO player_game_stats (id, player_id, game_id, buy_in, total_profit) VALUES ");
        sb.append("(");
        sb.append(playerGameStats.getId());
        sb.append(",");
        sb.append(playerGameStats.getPlayerId());
        sb.append(",");
        sb.append(playerGameStats.getGameId());
        sb.append(",");
        sb.append(playerGameStats.getBuyIn());
        sb.append(",");
        sb.append(playerGameStats.getTotalProfit());
        sb.append(");");
        return sb.toString();
    }
}
