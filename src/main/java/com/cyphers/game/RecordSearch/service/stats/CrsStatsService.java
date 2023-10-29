package com.cyphers.game.RecordSearch.service.stats;

import org.springframework.stereotype.Service;

import com.cyphers.game.RecordSearch.service.stats.repository.CrsPickRateStatsRepository;
import com.cyphers.game.RecordSearch.service.stats.repository.CrsWinRateStatsRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CrsStatsService {
	
	private final CrsWinRateStatsRepository crsWinRateStatsRepository;
	private final CrsPickRateStatsRepository crsPickRateStatsRepository;

}
