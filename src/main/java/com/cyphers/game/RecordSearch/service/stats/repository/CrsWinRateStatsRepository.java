package com.cyphers.game.RecordSearch.service.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cyphers.game.RecordSearch.model.stats.entity.CrsWinRateStats;

public interface CrsWinRateStatsRepository extends JpaRepository<CrsWinRateStats, String> {

}
