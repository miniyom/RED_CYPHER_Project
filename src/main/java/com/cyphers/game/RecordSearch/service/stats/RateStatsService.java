package com.cyphers.game.RecordSearch.service.stats;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cyphers.game.RecordSearch.model.stats.PickRateStatsInfo;
import com.cyphers.game.RecordSearch.model.stats.WinRateStatsInfo;
import com.cyphers.game.RecordSearch.openapi.model.CyphersMatchedInfo;
import com.cyphers.game.RecordSearch.openapi.model.CyphersMatches;
import com.cyphers.game.RecordSearch.openapi.model.CyphersPlayerRankInfo;
import com.cyphers.game.RecordSearch.openapi.model.enumuration.CyphersGameType;
import com.cyphers.game.RecordSearch.openapi.service.CyphersApiService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RateStatsService {
	
	final static Integer RANK_OFFSET = 0;	//offset+1위부터 limit위까지 가져올 수 있음
	final static Integer RANK_LIMIT = 10;

	@Autowired
	CyphersApiService cyApiService;
	
	public List<WinRateStatsInfo> getWinRateStats() throws Exception {
		List<String> rankerPlayerIds = new ArrayList<>();
		List<CyphersPlayerRankInfo> cyRankList = cyApiService.searchRankingList(RANK_OFFSET, RANK_LIMIT).getRows();
		for (CyphersPlayerRankInfo cyRankInfo : cyRankList) {
			rankerPlayerIds.add(cyRankInfo.getPlayerId());
		}
		
		Set<String> matchIds = new HashSet<>();
		LocalDateTime endDate = LocalDateTime.now();
		LocalDateTime startDate = endDate.minusWeeks(2);
		for (String playerId : rankerPlayerIds) {
			CyphersMatches matches = cyApiService.searchMatchingHistory(playerId, CyphersGameType.RATING, startDate.toString(), endDate.toString(), null).getMatches();
			for (CyphersMatchedInfo match : matches.getRows()) {
				matchIds.add(match.getMatchId());
			}
		}
		List<WinRateStatsInfo> winRateStats = new ArrayList<>();
		return null;
	} 
	
	public List<PickRateStatsInfo> getPickRateStats() throws Exception {
		return null;
	}
	
}
