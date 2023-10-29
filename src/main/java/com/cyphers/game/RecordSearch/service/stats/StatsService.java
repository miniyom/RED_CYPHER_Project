package com.cyphers.game.RecordSearch.service.stats;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import com.cyphers.game.RecordSearch.model.stats.PickRateStatsInfo;
import com.cyphers.game.RecordSearch.model.stats.WinRateStatsInfo;
import com.cyphers.game.RecordSearch.openapi.model.CyphersCharacterInfo;
import com.cyphers.game.RecordSearch.openapi.model.CyphersMatchedInfo;
import com.cyphers.game.RecordSearch.openapi.model.CyphersMatches;
import com.cyphers.game.RecordSearch.openapi.model.CyphersMatchingDetails;
import com.cyphers.game.RecordSearch.openapi.model.CyphersPlayInfo;
import com.cyphers.game.RecordSearch.openapi.model.CyphersPlayerRankInfo;
import com.cyphers.game.RecordSearch.openapi.model.CyphersPlayersInGame;
import com.cyphers.game.RecordSearch.openapi.model.enumuration.CyphersGameType;
import com.cyphers.game.RecordSearch.openapi.service.CyphersApiService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StatsService {
	
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
		
		//키는 캐릭터 이름, Pair의 첫번째는 해당캐릭터의 판수, 두번째는 이긴 횟수
		Map<String, Pair<Integer, Integer>> winRateMap = new HashMap<>();
		
		LocalDateTime endDate = LocalDateTime.now();
		LocalDateTime startDate = endDate.minusWeeks(2);
		for (String playerId : rankerPlayerIds) {
			//playerId로 하나하나 가져와서 matchId다 집어넣음 -> 1위부터 10위까지 record를 모두 긁어옴.
			CyphersMatches matches = cyApiService.searchMatchingHistory(playerId, CyphersGameType.RATING, startDate.toString(), endDate.toString(), null).getMatches();
			//record에 있는 플레이 캐릭터와 승패여부를 담음.
			for (CyphersMatchedInfo cyMatchedInfo : matches.getRows()) {
				CyphersPlayInfo playInfo = cyMatchedInfo.getPlayInfo();
				String characterName = playInfo.getCharacterName();
				Pair<Integer, Integer> playAndWinCount = winRateMap.getOrDefault(characterName, Pair.of(0, 0));
				winRateMap.put(characterName, Pair.of(playAndWinCount.getFirst() + 1,
						playAndWinCount.getSecond() + (playInfo.getResult().equals("win") ? 1 : 0)));
			}
		}
		
		List<CyphersCharacterInfo> chracterInfos = cyApiService.searchCharacter().getRows();
		List<String> characters = new ArrayList<>();
		for (CyphersCharacterInfo characterInfo : chracterInfos) {
			String characterName = characterInfo.getCharacterName();
			characters.add(characterName);
		}
		
		//캐릭터 이름, '캐릭터 승수/캐릭터 판수'로 된 리스트를 만들어야함 
		List<WinRateStatsInfo> winRateStats = new ArrayList<>();
		for (String character : characters) {
			WinRateStatsInfo wrStatsInfo = new WinRateStatsInfo();
			Float winCount = winRateMap.getOrDefault(character, Pair.of(0, 0)).getSecond().floatValue();
			Integer playCount = winRateMap.getOrDefault(character, Pair.of(0, 0)).getFirst();
			Float winRate =
					playCount == 0 ? 0.0f : Math.round(100 * (100 * winCount/playCount)) / 100.0f;
			wrStatsInfo.setWinRate(winRate);
			wrStatsInfo.setCharacterName(character);
			winRateStats.add(wrStatsInfo);
		}
		Collections.sort(winRateStats, Comparator.comparing(WinRateStatsInfo::getWinRate).reversed());
		return winRateStats;
	} 
	
	public List<PickRateStatsInfo> getPickRateStats() throws Exception {
		List<String> rankerPlayerIds = new ArrayList<>();
		List<CyphersPlayerRankInfo> cyRankList = cyApiService.searchRankingList(RANK_OFFSET, RANK_LIMIT).getRows();
		for (CyphersPlayerRankInfo cyRankInfo : cyRankList) {
			rankerPlayerIds.add(cyRankInfo.getPlayerId());
		}
		
		Set<String> matchIds = new HashSet<>();
		LocalDateTime endDate = LocalDateTime.now();
		LocalDateTime startDate = endDate.minusWeeks(2);
		//1위부터 10위까지 RECORDS 다 가져와야함
		for (String playerId : rankerPlayerIds) {
			//playerId로 하나하나 가져와서 matchId다 집어넣음 -> 1위부터 10위까지 게임을 모두 긁어옴.
			CyphersMatches matches = cyApiService.searchMatchingHistory(playerId, CyphersGameType.RATING, startDate.toString(), endDate.toString(), null).getMatches();
			for (CyphersMatchedInfo match : matches.getRows()) {
				matchIds.add(match.getMatchId());
			}
		}
		//키는 캐릭터 이름, 값은 픽된 횟수
		Map<String, Integer> characterMap = new HashMap<>();
		List<String> matchIdList = new ArrayList<>(matchIds);
		for (String matchId : matchIdList) {
			//matchId 로 게임 조회
			CyphersMatchingDetails matchDetail = cyApiService.searchMatchingDetail(matchId);
			//게임 내 플레이 되었던 캐릭터를 모두 map에 집어 넣음
			for (CyphersPlayersInGame playerInfo : matchDetail.getPlayers()) {
				CyphersPlayInfo playInfo = playerInfo.getPlayInfo();
				characterMap.put(playInfo.getCharacterName(), 
								characterMap.getOrDefault(playInfo.getCharacterName(), 0) + 1);
			}
		}
		List<CyphersCharacterInfo> chracterInfos = cyApiService.searchCharacter().getRows();
		List<String> characters = new ArrayList<>();
		for (CyphersCharacterInfo characterInfo : chracterInfos) {
			characters.add(characterInfo.getCharacterName());
		}
		//캐릭터 이름, 'map에 들어있는 각 캐릭터 숫자/matchIdList의 크기'로 된 리스트를 만들어야함 
		List<PickRateStatsInfo> pickRateStats = new ArrayList<>();
		for (String character : characters) {
			PickRateStatsInfo prStatsInfo = new PickRateStatsInfo();
			Float characterNum = characterMap.getOrDefault(character, 0).floatValue();
			Integer matchListNum = matchIdList.size();
			Float pickRate = Math.round(100 * (100 * characterNum/matchListNum)) / 100.0f;
			prStatsInfo.setPickRate(pickRate);
			
			prStatsInfo.setCharacterName(character);
			pickRateStats.add(prStatsInfo);
		}
		Collections.sort(pickRateStats, Comparator.comparing(PickRateStatsInfo::getPickRate).reversed());
		return pickRateStats;
	}
	
}
