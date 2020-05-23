package org.smellit.quarantinepoker.repo;

import org.smellit.quarantinepoker.model.PlayerGameStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerGameStatsRepository extends JpaRepository<PlayerGameStats, Integer> {

    @Query("SELECT pgs FROM PlayerGameStats pgs WHERE pgs.gameId = ?1")
    List<PlayerGameStats> findAllByGameId(int gameId);

    @Query("SELECT pgs FROM PlayerGameStats pgs WHERE pgs.playerId = ?1")
    List<PlayerGameStats> findAllByPlayerId(int playerId);
}
