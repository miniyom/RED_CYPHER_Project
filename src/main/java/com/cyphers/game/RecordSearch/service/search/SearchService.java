package com.cyphers.game.RecordSearch.service.search;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cyphers.game.RecordSearch.controller.search.model.GameRecordsVO;
import com.cyphers.game.RecordSearch.controller.search.model.IoSearchDetailGameRecord;
import com.cyphers.game.RecordSearch.controller.search.model.IoSearchDetailMostCypherInfo;
import com.cyphers.game.RecordSearch.controller.search.model.IoSearchDetailMostPositionInfo;
import com.cyphers.game.RecordSearch.controller.search.model.IoSearchDetailRecentlyPlayCyphersInfo;
import com.cyphers.game.RecordSearch.controller.search.model.IoSearchDetailResponse;
import com.cyphers.game.RecordSearch.controller.search.model.IoSearchDetailWinAndLoseCountHistoryInfo;
import com.cyphers.game.RecordSearch.cyphers.CyphersApiService;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersCharacterAttribute;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersCharacterInfo;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersCharacterSearch;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersEquipItems;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersMatchedInfo;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersMatchingDetails;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersMatchingHistory;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersPlayInfo;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersPlayer;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersPlayerInfo;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersPlayerRepresent;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersPlayerResponse;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersPlayersInGame;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersRecords;
import com.cyphers.game.RecordSearch.cyphers.model.enumuration.CyphersGameType;
import com.cyphers.game.RecordSearch.cyphers.model.enumuration.CyphersPlayerWordType;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SearchService {

	@Autowired
	ObjectMapper objectmapper;
	@Autowired
	CyphersApiService cyApiService;

	final static Integer API_LIMIT = 100; // 매칭기록 조회 api에서 정해진 limit
	final static Integer MOST_CYPHER_LENGTH = 10; // 모스트 사이퍼에서 보여줄 캐릭터 개수
	final static Integer RECENT_CYPHER_LENGTH = 3; // 최근 2주간 데이터에서 보여줄 캐릭터 개수
	final static Float PERFECT_KDA = -1.0f; // kda에서 death수가 0일 경우 리턴할 값
	final static Integer WIN_AND_LOSE_KEY = 6; // 승패 그래프에서 보여줄 데이터 키(0~6, 총 7개)

	public List<String> getNicknameList(String nickname) throws Exception {
		List<String> nicknameList = new ArrayList<>();
		Integer limit = 10;

		CyphersPlayerResponse cyPlayerRes = cyApiService.searchPlayers(nickname, CyphersPlayerWordType.FULL, limit);
		Integer minSize = Math.min(cyPlayerRes.getRows().size(), limit);
		for (int i = 0; i < minSize; i++) {
			nicknameList.add(cyPlayerRes.getRows().get(i).getNickname());
		}
//		cyPlayerRes.getRows().stream().limit(limit).map(CyphersPlayer::getNickname).collect(Collectors.toList());
		return nicknameList;
	}

	public IoSearchDetailResponse getDetailSearch(String nickname) throws Exception {

		CyphersPlayerResponse cyPlayerResponse = cyApiService.searchPlayers(nickname, CyphersPlayerWordType.MATCH,
				null);

		if (CollectionUtils.isEmpty(cyPlayerResponse.getRows())) {
			throw new Exception("닉네임 정보가 없습니다.");
		}

		IoSearchDetailResponse ioGameRecords = new IoSearchDetailResponse();
		CyphersPlayer cyPlayer = cyPlayerResponse.getRows().get(0);
		String myPlayerId = cyPlayer.getPlayerId();
		;
		CyphersCharacterSearch cyCharacter = cyApiService.searchCharacter();

		// 플레이어 기본정보
		CyphersPlayerRepresent cyPlayerRepresent = cyPlayer.getRepresent();
		String profileCharacterId = cyPlayerRepresent.getCharacterId();
		String profileNickname = cyPlayerRepresent.getCharacterName();
		ioGameRecords.setPlayerId(myPlayerId);
		ioGameRecords.setProfileCharacterId(profileCharacterId);
		ioGameRecords.setNickname(profileNickname);

		// 현재 시즌 공식전, 일반전 기록 가져오기
		CyphersMatchingHistory cyMatchingHistoryRating = cyApiService.searchMatchingHistory(myPlayerId,
				CyphersGameType.RATING, null, null, API_LIMIT);
		CyphersMatchingHistory cyMatchingHistoryNormal = cyApiService.searchMatchingHistory(myPlayerId,
				CyphersGameType.NORMAL, null, null, API_LIMIT);

		List<CyphersMatchedInfo> cyMatchedInfoRows = new ArrayList<>(); // 각 기능에서 쓰일 리스트

		for (CyphersMatchedInfo cyMatchedInfoRating : cyMatchingHistoryRating.getMatches().getRows()) {
			cyMatchedInfoRows.add(cyMatchedInfoRating);
		}
		for (CyphersMatchedInfo cyMatchedInfoNormal : cyMatchingHistoryNormal.getMatches().getRows()) {
			cyMatchedInfoRows.add(cyMatchedInfoNormal);
		}
		Comparator<CyphersMatchedInfo> comparator = new Comparator<CyphersMatchedInfo>() {
			@Override
			public int compare(CyphersMatchedInfo cy1, CyphersMatchedInfo cy2) {
				LocalDate dateTime1 = cy1.getDate();
				LocalDate dateTime2 = cy2.getDate();
				return dateTime2.compareTo(dateTime1);
			}
		};
		Collections.sort(cyMatchedInfoRows, comparator);

		Map<String, Pair<Integer, Integer>> characterIdMap = new HashMap<>(); // Pair의 첫번째는 전체 플레이 횟수, 두번째는 이긴 횟수
		for (CyphersMatchedInfo cyMatchedInfo : cyMatchedInfoRows) {
			CyphersPlayInfo cyPlayInfo = cyMatchedInfo.getPlayInfo();
			String characterId = cyPlayInfo.getCharacterId();
			Pair<Integer, Integer> playAndWinCount = characterIdMap.getOrDefault(characterId, Pair.of(0, 0));

			characterIdMap.put(characterId, Pair.of(playAndWinCount.getFirst() + 1,
					playAndWinCount.getSecond() + (cyPlayInfo.getResult().equals("win") ? 1 : 0)));
		}
		List<String> idList = new ArrayList<>(characterIdMap.keySet());
		Collections.sort(idList, Comparator.comparing(id -> id,
				(a, b) -> characterIdMap.get(b).getFirst() - characterIdMap.get(a).getFirst()));
		;

		// 모스트 사이퍼
		ioGameRecords.setMostCypherInfos(Collections.emptyList());

		if (cyMatchedInfoRows.size() != 0) {
			List<IoSearchDetailMostCypherInfo> mostCypherRows = new ArrayList<>();

			for (int i = 0; i < MOST_CYPHER_LENGTH; i++) {
				IoSearchDetailMostCypherInfo mostCypherInfo = new IoSearchDetailMostCypherInfo();
				mostCypherInfo.setCharacterId(idList.get(i));
				for (CyphersCharacterInfo characterInfo : cyCharacter.getRows()) {
					if (characterInfo.getCharacterId().equals(idList.get(i))) {
						mostCypherInfo.setCharacterName(characterInfo.getCharacterName());
						break;
					}
				}
				Integer winCount = characterIdMap.get(idList.get(i)).getSecond();
				Integer playCount = characterIdMap.get(idList.get(i)).getFirst();
				mostCypherInfo.setWinRate(100 * winCount / playCount);
				mostCypherInfo.setPlayCount(playCount);

				Float killCount = 0.0f;
				Float deathCount = 0.0f;
				Float assistCount = 0.0f;
				for (CyphersMatchedInfo cyMatchedInfo : cyMatchedInfoRows) {
					CyphersPlayInfo cyPlayInfo = cyMatchedInfo.getPlayInfo();
					if (cyPlayInfo.getCharacterId().equals(idList.get(i))) {
						killCount += cyPlayInfo.getKillCount();
						deathCount += cyPlayInfo.getDeathCount();
						assistCount += cyPlayInfo.getAssistCount();
					}
				}
				if (deathCount != 0) {
					mostCypherInfo.setKda(Math.round((killCount + assistCount) / deathCount * 100) / 100.0f);
				} else {
					mostCypherInfo.setKda(PERFECT_KDA);
				}

				mostCypherRows.add(mostCypherInfo);
			}
			ioGameRecords.setMostCypherInfos(mostCypherRows);

		}

		// 모스트 포지션
		IoSearchDetailMostPositionInfo mostPositionInfo = new IoSearchDetailMostPositionInfo();

		Integer tankerUseCount = 0;
		Integer rangeDealerUseCount = 0;
		Integer supporterUseCount = 0;
		Integer meleeDealerUseCount = 0;
		Integer mostPositionPlayCount = cyMatchedInfoRows.size();

		mostPositionInfo.setTankerUseRate(0);
		mostPositionInfo.setRangeDealerUseRate(0);
		mostPositionInfo.setSupporterUseRate(0);
		mostPositionInfo.setMeleeDealerUseRate(0);
		ioGameRecords.setMostPositionInfos(mostPositionInfo);

		if (mostPositionPlayCount != 0) {
			for (CyphersMatchedInfo cyMatchedInfo : cyMatchedInfoRows) {
				String position = cyMatchedInfo.getPosition().getName();
				switch (position) {
				case "탱커": {
					tankerUseCount++;
					break;
				}
				case "원거리딜러": {
					rangeDealerUseCount++;
					break;
				}
				case "서포터": {
					supporterUseCount++;
					break;
				}
				case "근거리딜러": {
					meleeDealerUseCount++;
					break;
				}
				}
			}
			mostPositionInfo.setTankerUseRate((100 * tankerUseCount) / mostPositionPlayCount);
			mostPositionInfo.setRangeDealerUseRate((100 * rangeDealerUseCount) / mostPositionPlayCount);
			mostPositionInfo.setSupporterUseRate((100 * supporterUseCount) / mostPositionPlayCount);
			mostPositionInfo.setMeleeDealerUseRate((100 * meleeDealerUseCount) / mostPositionPlayCount);
			ioGameRecords.setMostPositionInfos(mostPositionInfo);
		}

		// 공식전, 일반전 데이터
		CyphersPlayerInfo cyPlayerInfo = cyApiService.searchPlayerInfo(myPlayerId);

		ioGameRecords.setRatingGameTier("-");
		ioGameRecords.setRatingWinCount(0);
		ioGameRecords.setRatingLoseCount(0);
		ioGameRecords.setRatingStopCount(0);
		ioGameRecords.setRatingWinRate(0);
		ioGameRecords.setNormalWinCount(0);
		ioGameRecords.setNormalLoseCount(0);
		ioGameRecords.setNormalStopCount(0);
		ioGameRecords.setNormalWinRate(0);

		if (cyPlayerInfo.getRecords().size() != 0) {
			String gameTypeIdRow0 = cyPlayerInfo.getRecords().get(0).getGameTypeId(); // Rows의 0번째를 가져옴

			if (gameTypeIdRow0.equals("rating")) {
				CyphersRecords cyPlayerRatingRecord = cyPlayerInfo.getRecords().get(0);
				ioGameRecords.setRatingGameTier(cyPlayerInfo.getTierName());
				ioGameRecords.setRatingWinCount(cyPlayerRatingRecord.getWinCount());
				ioGameRecords.setRatingLoseCount(cyPlayerRatingRecord.getLoseCount());
				ioGameRecords.setRatingStopCount(cyPlayerRatingRecord.getStopCount());
				if (cyPlayerRatingRecord.getWinCount() + cyPlayerRatingRecord.getLoseCount() != 0) {
					ioGameRecords.setRatingWinRate(100 * cyPlayerRatingRecord.getWinCount()
							/ (cyPlayerRatingRecord.getLoseCount() + cyPlayerRatingRecord.getWinCount()));
				}
			}
			if (gameTypeIdRow0.equals("normal") || cyPlayerInfo.getRecords().size() == 2) {
				Integer recordsLength = Math.max(0, cyPlayerInfo.getRecords().size() - 1);
				CyphersRecords cyPlayerNormalRecord = cyPlayerInfo.getRecords().get(recordsLength);
				ioGameRecords.setNormalWinCount(cyPlayerNormalRecord.getWinCount());
				ioGameRecords.setNormalLoseCount(cyPlayerNormalRecord.getLoseCount());
				ioGameRecords.setNormalStopCount(cyPlayerNormalRecord.getStopCount());
				if (cyPlayerNormalRecord.getWinCount() + cyPlayerNormalRecord.getLoseCount() != 0) {
					ioGameRecords.setNormalWinRate(100 * cyPlayerNormalRecord.getWinCount()
							/ (cyPlayerNormalRecord.getLoseCount() + cyPlayerNormalRecord.getWinCount()));
				}
			}
		}

		// 승, 패수 데이터(그래프)
		List<IoSearchDetailWinAndLoseCountHistoryInfo> winAndLoseCountHistoryInfos = new ArrayList<>();
		LocalDate now = LocalDate.now();
		LocalDate oneWeekAgo = now.minus(1, ChronoUnit.WEEKS);
		List<CyphersMatchedInfo> weeklyMatchedInfoRows = filterDataByDate(cyMatchedInfoRows, oneWeekAgo, now);

		Map<Integer, Pair<Integer, Integer>> cyMatchingHistoryMap = new HashMap<>(); // pair 앞은 승수, 뒤는 패수
		Pair<LocalDate, Integer> matchedDateAndInt = Pair.of(now, WIN_AND_LOSE_KEY);

		for (int i = 0; i <= WIN_AND_LOSE_KEY; i++) {
			IoSearchDetailWinAndLoseCountHistoryInfo defaultWinAndLoseHisory = new IoSearchDetailWinAndLoseCountHistoryInfo();
			defaultWinAndLoseHisory.setHistoryDate(i);
			defaultWinAndLoseHisory.setWinCount(0);
			defaultWinAndLoseHisory.setLoseCount(0);
			winAndLoseCountHistoryInfos.add(defaultWinAndLoseHisory);
			cyMatchingHistoryMap.put(i, Pair.of(0, 0));
		}

		for (CyphersMatchedInfo weeklyMatchedInfo : weeklyMatchedInfoRows) {
			IoSearchDetailWinAndLoseCountHistoryInfo winAndLoseHisory = new IoSearchDetailWinAndLoseCountHistoryInfo();

			if (!weeklyMatchedInfo.getDate().isEqual(matchedDateAndInt.getFirst())) {
				matchedDateAndInt = Pair.of(matchedDateAndInt.getFirst().minusDays(1),
						matchedDateAndInt.getSecond() - 1); // 날짜 및 정수 감소
				if (matchedDateAndInt.getSecond() < 0) {
					break;
				}
			}
			// 같은날인지 체크하지 않으면, 중간에 빈 날이 그 전날의 기록을 조회해버림. ex) 9/15에 기록이 없으면 9/14의 기록을 참조함.
			if (weeklyMatchedInfo.getDate().isEqual(matchedDateAndInt.getFirst())) {
				Pair<Integer, Integer> winAndLoseCount = cyMatchingHistoryMap.get(matchedDateAndInt.getSecond());
				if (weeklyMatchedInfo.getPlayInfo().getResult().equals("win")) {
					cyMatchingHistoryMap.put(matchedDateAndInt.getSecond(),
							Pair.of(winAndLoseCount.getFirst() + 1, winAndLoseCount.getSecond()));
				}
				if (weeklyMatchedInfo.getPlayInfo().getResult().equals("lose")
						|| weeklyMatchedInfo.getPlayInfo().getResult().equals("stop")) {
					cyMatchingHistoryMap.put(matchedDateAndInt.getSecond(),
							Pair.of(winAndLoseCount.getFirst(), winAndLoseCount.getSecond() + 1));
				}
			}
			winAndLoseHisory.setHistoryDate(matchedDateAndInt.getSecond());
			winAndLoseHisory.setWinCount(cyMatchingHistoryMap.get(matchedDateAndInt.getSecond()).getFirst());
			winAndLoseHisory.setLoseCount(cyMatchingHistoryMap.get(matchedDateAndInt.getSecond()).getSecond());
			winAndLoseCountHistoryInfos.set(matchedDateAndInt.getSecond(), winAndLoseHisory);
		}

		ioGameRecords.setWinAndLoseCountHistoryInfos(winAndLoseCountHistoryInfos);

		// 최근 2주간 게임 데이터
		LocalDate twoWeekAgo = LocalDate.now().minus(2, ChronoUnit.WEEKS);
		List<CyphersMatchedInfo> recentMatchedInfoRows = filterDataByDate(cyMatchedInfoRows, twoWeekAgo,
				LocalDate.now());

		Integer recentlyPlayCount = recentMatchedInfoRows.size();
		ioGameRecords.setRecentlyPlayCount(0);
		ioGameRecords.setRecentlyWinRate(0);
		ioGameRecords.setRecentlyKda(0.0f);
		ioGameRecords.setRecentlyAverageSurvivalRate(0);
		ioGameRecords.setRecentlyPlayCyphersInfos(Collections.emptyList());

		if (recentlyPlayCount != 0) {

			ioGameRecords.setRecentlyPlayCount(recentlyPlayCount);

			Integer recentlyWinCount = 0;
			for (CyphersMatchedInfo recentMatchedInfo : recentMatchedInfoRows) {
				String result = recentMatchedInfo.getPlayInfo().getResult();
				if (result.equals("win")) {
					recentlyWinCount++;
				}
			}
			ioGameRecords.setRecentlyWinRate(100 * recentlyWinCount / recentlyPlayCount);

			Float totalKillCount = 0.0f;
			Float totalDeathCount = 0.0f;
			Float totalAssistCount = 0.0f;
			for (CyphersMatchedInfo recentMatchedInfo : recentMatchedInfoRows) {
				CyphersPlayInfo cyPlayInfo = recentMatchedInfo.getPlayInfo();
				totalKillCount += cyPlayInfo.getKillCount();
				totalDeathCount += cyPlayInfo.getDeathCount();
				totalAssistCount += cyPlayInfo.getAssistCount();
			}
			if (totalDeathCount != 0) {
				ioGameRecords.setRecentlyKda(
						Math.round((totalKillCount + totalAssistCount) / totalDeathCount * 100) / 100.0f);
			} else {
				ioGameRecords.setRecentlyKda(PERFECT_KDA);
			}

			Integer avgSurvivalRate = 0;
			for (CyphersMatchedInfo recentMatchedInfo : recentMatchedInfoRows) {
				Integer playTime = recentMatchedInfo.getPlayInfo().getPlayTime();
				Integer responseTime = recentMatchedInfo.getPlayInfo().getResponseTime();

				avgSurvivalRate += 100 * (playTime - responseTime) / playTime;
			}
			ioGameRecords.setRecentlyAverageSurvivalRate(avgSurvivalRate / recentlyPlayCount);

			// top3 캐릭터 데이터
			List<IoSearchDetailRecentlyPlayCyphersInfo> recentCypherRows = new ArrayList<>();

			for (int i = 0; i < RECENT_CYPHER_LENGTH; i++) {
				IoSearchDetailRecentlyPlayCyphersInfo recentCypherInfo = new IoSearchDetailRecentlyPlayCyphersInfo();
				recentCypherInfo.setCharacterId(idList.get(i));
				for (CyphersCharacterInfo characterInfo : cyCharacter.getRows()) {
					if (characterInfo.getCharacterId().equals(idList.get(i))) {
						recentCypherInfo.setCharacterName(characterInfo.getCharacterName());
						break;
					}
				}
				recentCypherInfo.setWinCount(characterIdMap.get(idList.get(i)).getSecond());
				recentCypherInfo.setLoseCount(
						characterIdMap.get(idList.get(i)).getFirst() - characterIdMap.get(idList.get(i)).getSecond());

				Float avgKillCount = 0.0f;
				Float avgDeathCount = 0.0f;
				Float avgAssistCount = 0.0f;
				Float denominator = 0.0f;
				for (CyphersMatchedInfo recentMatchedInfo : recentMatchedInfoRows) {
					CyphersPlayInfo cyPlayInfo = recentMatchedInfo.getPlayInfo();
					if (cyPlayInfo.getCharacterId().equals(idList.get(i))) {
						avgKillCount += cyPlayInfo.getKillCount();
						avgDeathCount += cyPlayInfo.getDeathCount();
						avgAssistCount += cyPlayInfo.getAssistCount();
						denominator++;
					}
				}
				if (denominator != 0) {
					recentCypherInfo.setKillCount(Math.round(avgKillCount / denominator * 10) / 10.0f);
					recentCypherInfo.setDeathCount(Math.round(avgDeathCount / denominator * 10) / 10.0f);
					recentCypherInfo.setAssistCount(Math.round(avgAssistCount / denominator * 10) / 10.0f);
				}

				recentCypherRows.add(recentCypherInfo);
			}

			ioGameRecords.setRecentlyPlayCyphersInfos(recentCypherRows);
		}

//        //게임기록
//        List<IoSearchDetailGameRecord> gameRecords = new ArrayList<>();
//        ioGameRecords.setGameRecords(Collections.emptyList());
//        
//        if (cyMatchedInfoRows.size() != 0) {
//        	
//        	for (CyphersMatchedInfo matchedInfo : cyMatchedInfoRows) {
//        		IoSearchDetailGameRecord gameRecord = new IoSearchDetailGameRecord();
//                String matchId = matchedInfo.getMatchId();
//                CyphersMatchingDetails matchingDetail = cyApiService.searchMatchingDetail(matchId);
//                Integer totalKillCount = 0;
//                for (CyphersPlayersInGame cyPlayersInGame : matchingDetail.getPlayers()) {
//                	totalKillCount += cyPlayersInGame.getPlayInfo().getKillCount();
//				}
//                
//                List<String> playerNicknames = new ArrayList<>();
//                
//                for (CyphersPlayersInGame playerDataInGame : matchingDetail.getPlayers()) {
//                	if (playerDataInGame.getPlayerId().equals(myPlayerId)) {
//    					CyphersPlayInfo playInfo = playerDataInGame.getPlayInfo();
//    					
//    					gameRecord.setPlayCharacterId(playInfo.getCharacterId());
//    					gameRecord.setPositionName(playerDataInGame.getPosition().getName());
//    					List<String> attributeIds = new ArrayList<>();
//    					for (CyphersCharacterAttribute attribute : playerDataInGame.getPosition().getAttribute()) {
//    						attributeIds.add(attribute.getId());
//						}
//    					gameRecord.setKillParticipation(0);
//    					if (totalKillCount > 0) {
//    						gameRecord.setKillParticipation(100 * (playInfo.getKillCount() + playInfo.getAssistCount()) / totalKillCount);
//						} 
//    					
//    					gameRecord.setAttributeIds(attributeIds);
//    					gameRecord.setKillCount(playInfo.getKillCount());
//    					gameRecord.setDeathCount(playInfo.getDeathCount());
//    					gameRecord.setAssistCount(playInfo.getAssistCount());
//    					
//    					
//    					Float killCount = playInfo.getKillCount().floatValue();
//    					Float deathCount = playInfo.getDeathCount().floatValue();
//    					Float assistCount = playInfo.getAssistCount().floatValue();
//    					if (deathCount != 0) {
//    						gameRecord.setKda(Math.round((killCount + assistCount) / deathCount * 100) / 100.0f);
//    	    			} else {
//    	    				gameRecord.setKda(PERFECT_KDA);
//    	    			}
//    					gameRecord.setCsCount(playInfo.getDemolisherKillCount() + playInfo.getSentinelKillCount());
//    					
//    					List<String> itemIds = new ArrayList<>();
//    					for (CyphersEquipItems item : playerDataInGame.getItems()) {
//    						itemIds.add(item.getItemId());
//						}
//    					gameRecord.setItemIds(itemIds);
//    					
//    					gameRecord.setHealAmount(playInfo.getHealAmount());
//    					gameRecord.setAttackPoint(playInfo.getAttackPoint());
//    					gameRecord.setDamagePoint(playInfo.getDamagePoint());
//    					gameRecord.setGetCoin(playInfo.getGetCoin());
//    					gameRecord.setBattlePoint(playInfo.getBattlePoint());
//    					gameRecord.setSightPoint(playInfo.getSightPoint());
//    				}
//    				playerNicknames.add(playerDataInGame.getNickname());
//				}
//                String gameType = matchingDetail.getGameTypeId();
//                switch (gameType) {
//					case "rating": {
//						gameRecord.setGameType(CyphersGameType.RATING);
//						break;
//					}
//					case "normal": {
//						gameRecord.setGameType(CyphersGameType.NORMAL);
//						break;
//					}
//                }
//                gameRecord.setPlayerNicknames(playerNicknames);
//                gameRecords.add(gameRecord);
//                
//                ioGameRecords.setGameRecords(gameRecords);
//			}
//        	
//        }

		return ioGameRecords;
	}

	public GameRecordsVO getGameRecords(String nickname) throws Exception {
		GameRecordsVO gameRecordsVo = new GameRecordsVO();
		
		CyphersPlayerResponse cyPlayerResponse = cyApiService.searchPlayers(nickname, CyphersPlayerWordType.MATCH,
				null);

		if (CollectionUtils.isEmpty(cyPlayerResponse.getRows())) {
			throw new Exception("닉네임 정보가 없습니다.");
		}

		String myPlayerId = cyPlayerResponse.getRows().get(0).getPlayerId();

		// 현재 시즌 공식전, 일반전 기록 가져오기
		CyphersMatchingHistory cyMatchingHistoryRating = cyApiService.searchMatchingHistory(myPlayerId,
				CyphersGameType.RATING, null, null, API_LIMIT);
		CyphersMatchingHistory cyMatchingHistoryNormal = cyApiService.searchMatchingHistory(myPlayerId,
				CyphersGameType.NORMAL, null, null, API_LIMIT);

		List<CyphersMatchedInfo> cyMatchedInfoRows = new ArrayList<>(); // 각 기능에서 쓰일 리스트

		for (CyphersMatchedInfo cyMatchedInfoRating : cyMatchingHistoryRating.getMatches().getRows()) {
			cyMatchedInfoRows.add(cyMatchedInfoRating);
		}
		for (CyphersMatchedInfo cyMatchedInfoNormal : cyMatchingHistoryNormal.getMatches().getRows()) {
			cyMatchedInfoRows.add(cyMatchedInfoNormal);
		}
		Comparator<CyphersMatchedInfo> comparator = new Comparator<CyphersMatchedInfo>() {
			@Override
			public int compare(CyphersMatchedInfo cy1, CyphersMatchedInfo cy2) {
				LocalDate dateTime1 = cy1.getDate();
				LocalDate dateTime2 = cy2.getDate();
				return dateTime2.compareTo(dateTime1);
			}
		};
		Collections.sort(cyMatchedInfoRows, comparator);
		
		List<IoSearchDetailGameRecord> gameRecords = new ArrayList<>();
		gameRecordsVo.setGameRecords(Collections.emptyList());

		if (cyMatchedInfoRows.size() != 0) {

			for (CyphersMatchedInfo matchedInfo : cyMatchedInfoRows) {
				IoSearchDetailGameRecord gameRecord = new IoSearchDetailGameRecord();
				String matchId = matchedInfo.getMatchId();
				CyphersMatchingDetails matchingDetail = cyApiService.searchMatchingDetail(matchId);
				Integer totalKillCount = 0;
				for (CyphersPlayersInGame cyPlayersInGame : matchingDetail.getPlayers()) {
					totalKillCount += cyPlayersInGame.getPlayInfo().getKillCount();
				}

				List<String> playerNicknames = new ArrayList<>();

				for (CyphersPlayersInGame playerDataInGame : matchingDetail.getPlayers()) {
					if (playerDataInGame.getPlayerId().equals(myPlayerId)) {
						CyphersPlayInfo playInfo = playerDataInGame.getPlayInfo();

						gameRecord.setPlayCharacterId(playInfo.getCharacterId());
						gameRecord.setPositionName(playerDataInGame.getPosition().getName());
						List<String> attributeIds = new ArrayList<>();
						for (CyphersCharacterAttribute attribute : playerDataInGame.getPosition().getAttribute()) {
							attributeIds.add(attribute.getId());
						}
						gameRecord.setKillParticipation(0);
						if (totalKillCount > 0) {
							gameRecord.setKillParticipation(
									100 * (playInfo.getKillCount() + playInfo.getAssistCount()) / totalKillCount);
						}

						gameRecord.setAttributeIds(attributeIds);
						gameRecord.setKillCount(playInfo.getKillCount());
						gameRecord.setDeathCount(playInfo.getDeathCount());
						gameRecord.setAssistCount(playInfo.getAssistCount());

						Float killCount = playInfo.getKillCount().floatValue();
						Float deathCount = playInfo.getDeathCount().floatValue();
						Float assistCount = playInfo.getAssistCount().floatValue();
						if (deathCount != 0) {
							gameRecord.setKda(Math.round((killCount + assistCount) / deathCount * 100) / 100.0f);
						} else {
							gameRecord.setKda(PERFECT_KDA);
						}
						gameRecord.setCsCount(playInfo.getDemolisherKillCount() + playInfo.getSentinelKillCount());

						List<String> itemIds = new ArrayList<>();
						for (CyphersEquipItems item : playerDataInGame.getItems()) {
							itemIds.add(item.getItemId());
						}
						gameRecord.setItemIds(itemIds);

						gameRecord.setHealAmount(playInfo.getHealAmount());
						gameRecord.setAttackPoint(playInfo.getAttackPoint());
						gameRecord.setDamagePoint(playInfo.getDamagePoint());
						gameRecord.setGetCoin(playInfo.getGetCoin());
						gameRecord.setBattlePoint(playInfo.getBattlePoint());
						gameRecord.setSightPoint(playInfo.getSightPoint());
					}
					playerNicknames.add(playerDataInGame.getNickname());
				}
				String gameType = matchingDetail.getGameTypeId();
				switch (gameType) {
				case "rating": {
					gameRecord.setGameType(CyphersGameType.RATING);
					break;
				}
				case "normal": {
					gameRecord.setGameType(CyphersGameType.NORMAL);
					break;
				}
				}
				gameRecord.setPlayerNicknames(playerNicknames);
				gameRecords.add(gameRecord);

				gameRecordsVo.setGameRecords(gameRecords);
			}

		}
		return gameRecordsVo;
	}

	// 데이터 필터링 메소드
	private static List<CyphersMatchedInfo> filterDataByDate(List<CyphersMatchedInfo> dataList, LocalDate startDate,
			LocalDate endDate) {
		List<CyphersMatchedInfo> filteredData = new ArrayList<>();
		for (CyphersMatchedInfo data : dataList) {
			LocalDate dataDate = data.getDate();
			if (!dataDate.isBefore(startDate) && !dataDate.isAfter(endDate)) {
				filteredData.add(data);
			}
		}
		return filteredData;
	}
}
