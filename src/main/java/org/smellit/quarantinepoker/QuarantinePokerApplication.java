package org.smellit.quarantinepoker;

import org.smellit.quarantinepoker.model.PlayerStats;
import org.smellit.quarantinepoker.service.RankingsService;
import org.smellit.quarantinepoker.util.CsvParseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class QuarantinePokerApplication implements CommandLineRunner {

    private RankingsService rankingsService;
    private CsvParseService csvParseService;

    @Autowired
    public QuarantinePokerApplication(RankingsService rankingsService,
                                      CsvParseService csvParseService) {
        this.rankingsService = rankingsService;
        this.csvParseService = csvParseService;
    }

    public static void main(String[] args) {
        SpringApplication.run(QuarantinePokerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        StringBuilder sb = new StringBuilder();

        sb.append(rankingsService.rankAndPrint("Total Profit", PlayerStats::getTotalProfit, 5));
        sb.append("\n<br>\n");
        sb.append(rankingsService.rankAndPrint("Biggest Win", PlayerStats::getBiggestWin, 5));
        sb.append("\n<br>\n");
        sb.append(rankingsService.rankAndPrint("Worst Loss", PlayerStats::getWorstLoss, 5));
        sb.append("\n<br>\n");
        sb.append(rankingsService.rankAndPrint("Average Profit", PlayerStats::getAverageProfit, 5));
        sb.append("\n<br>\n");
        sb.append(rankingsService.rankAndPrint("Total Games", PlayerStats::getTotalGames));
        sb.append("\n<br>\n");
        sb.append(rankingsService.rankAndPrint("Number Wins", PlayerStats::getNumberWins, 5));
        sb.append("\n<br>\n");
        sb.append(rankingsService.rankAndPrint("Number Losses", PlayerStats::getNumberLosses, 5));
        sb.append("\n<br>\n");
        sb.append(rankingsService.rankAndPrint("Last 3 Games Profit", PlayerStats::getLast3TotalProfit, 5));
        sb.append("\n<br>\n");
        sb.append(rankingsService.rankAndPrint("Last 5 Games Profit", PlayerStats::getLast5TotalProfit, 5));

        System.err.println(sb.toString());
    }
}
