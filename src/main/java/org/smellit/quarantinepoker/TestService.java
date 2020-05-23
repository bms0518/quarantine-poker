package org.smellit.quarantinepoker;

import org.smellit.quarantinepoker.model.Game;
import org.smellit.quarantinepoker.model.Player;
import org.smellit.quarantinepoker.model.PlayerGameStats;
import org.smellit.quarantinepoker.repo.GameRepository;
import org.smellit.quarantinepoker.repo.PlayerGameStatsRepository;
import org.smellit.quarantinepoker.repo.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class TestService {

    private PlayerGameStatsRepository playerGameStatsRepository;

    private PlayerRepository playerRepository;

    private GameRepository gameRepository;

    @Autowired
    public TestService(PlayerGameStatsRepository playerGameStatsRepository, PlayerRepository playerRepository,
                       GameRepository gameRepository) {
        this.playerGameStatsRepository = playerGameStatsRepository;
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
    }

    @Transactional
    public void add() {
        PlayerGameStats playerGameStats1 = new PlayerGameStats();
        playerGameStats1.setId(1);
        playerGameStats1.setGameId(1);
        playerGameStats1.setPlayerId(1);
        playerGameStats1.setBuyIn(BigDecimal.ONE);
        playerGameStats1.setTotalProfit(BigDecimal.ZERO);

        PlayerGameStats playerGameStats2 = new PlayerGameStats();
        playerGameStats2.setId(2);
        playerGameStats2.setGameId(1);
        playerGameStats2.setPlayerId(2);
        playerGameStats2.setBuyIn(BigDecimal.ONE);
        playerGameStats2.setTotalProfit(BigDecimal.ONE);

        playerGameStatsRepository.save(playerGameStats1);
        playerGameStatsRepository.save(playerGameStats2);


    }

    public void print() {
        List<PlayerGameStats> playerGameStats1 = playerGameStatsRepository.findAllByGameId(1);
        List<PlayerGameStats> playerGameStats2 = playerGameStatsRepository.findAllByPlayerId(1);
        List<PlayerGameStats> playerGameStats3 = playerGameStatsRepository.findAllByPlayerId(2);
        System.err.println(playerGameStats1);
        System.err.println(playerGameStats2);
        System.err.println(playerGameStats3);

        System.err.println(playerRepository.findAll());
        System.err.println(gameRepository.findAll());
    }
}
