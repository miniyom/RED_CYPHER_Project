package com.cyphers.game.RecordSearch.service.stats;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.cyphers.game.RecordSearch.model.stats.PickRateStatsInfo;
import com.cyphers.game.RecordSearch.model.stats.WinRateStatsInfo;
import com.cyphers.game.RecordSearch.model.stats.entity.CrsPickRateStats;
import com.cyphers.game.RecordSearch.model.stats.entity.CrsWinRateStats;
import com.cyphers.game.RecordSearch.service.stats.repository.CrsPickRateStatsRepository;
import com.cyphers.game.RecordSearch.service.stats.repository.CrsWinRateStatsRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CrsStatsService {
	
	private final CrsWinRateStatsRepository crsWinRateStatsRepository;
	private final CrsPickRateStatsRepository crsPickRateStatsRepository;
	
	public void insert(List<WinRateStatsInfo> winRateStats, List<PickRateStatsInfo> pickRateStats) {
		
		for (WinRateStatsInfo winRateStatsInfo : winRateStats) {
			CrsWinRateStats crsWrStats = CrsWinRateStats.builder()
									.characterName(winRateStatsInfo.getCharacterName())
									.winRate(winRateStatsInfo.getWinRate())
									.build();
			crsWinRateStatsRepository.save(crsWrStats);
		}
		
		for (PickRateStatsInfo pickRateStatsInfo : pickRateStats) {
			CrsPickRateStats crsPrStats = CrsPickRateStats.builder()
									.characterName(pickRateStatsInfo.getCharacterName())
									.pickRate(pickRateStatsInfo.getPickRate())
									.build();
			crsPickRateStatsRepository.save(crsPrStats);
		}
		
	} 
	
	public List<CrsWinRateStats> getWinRateStats() {
		return crsWinRateStatsRepository.findAll(Sort.by(Sort.Direction.DESC, "winRate"));
	}
	
	public List<CrsPickRateStats> getPickRateStats() {
		return crsPickRateStatsRepository.findAll(Sort.by(Sort.Direction.DESC, "pickRate"));
	}

}
