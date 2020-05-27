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

        final int minGames = 5;

        sb.append(titleWrap("Total Profit (min 5 games)", rankingsService.rankAndPrint("Total Profit", PlayerStats::getTotalProfit, minGames)));
        sb.append(titleWrap("Biggest Win (min 5 games)", rankingsService.rankAndPrint("Biggest Win", PlayerStats::getBiggestWin, minGames)));

        sb.append(titleWrap("Worst Loss (min 5 games)", rankingsService.rankAndPrint("Worst Loss", PlayerStats::getWorstLoss, minGames)));
        sb.append(titleWrap("Average Profit (min 5 games)", rankingsService.rankAndPrint("Average Profit", PlayerStats::getAverageProfit, minGames)));
        sb.append(titleWrap("Total Games Played", rankingsService.rankAndPrint("Total Games", PlayerStats::getTotalGames, minGames)));

        sb.append(titleWrap("&#128293 3 Game Hot List &#128293 (min 3 games)", rankingsService.rankAndPrint("Last 3 Games Profit", PlayerStats::getLast3TotalProfit, 3)));
        sb.append(titleWrap("&#128293 5 Game Hot List &#128293 (min 5 games)", rankingsService.rankAndPrint("Last 5 Games Profit", PlayerStats::getLast5TotalProfit, minGames)));

        sb.append(titleWrap("Win/Loss (min 3 games)", rankingsService.getWinLossChart(3)));

        System.err.println(sb.toString());
    }

    private String titleWrap(String title, String tableHtml) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div>");
        sb.append("<h3>");
        sb.append(title);
        sb.append("</h3>");
        sb.append(tableHtml);
        sb.append("</div>");
        sb.append("<br>");
        sb.append("<hr>");
        sb.append("\n\n");
        return sb.toString();
    }

}
