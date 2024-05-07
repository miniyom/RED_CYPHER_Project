package com.cyphers.game.RecordSearch.service.search;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.cyphers.game.RecordSearch.model.search.AttributeInfoResponse;
import com.cyphers.game.RecordSearch.model.search.GameRecordResponse;
import com.cyphers.game.RecordSearch.model.search.IoSearchDetail;
import com.cyphers.game.RecordSearch.model.search.IoSearchDetailGameRecord;
import com.cyphers.game.RecordSearch.model.search.IoSearchDetailMostCypherInfo;
import com.cyphers.game.RecordSearch.model.search.IoSearchDetailRecentlyPlayCyphersInfo;
import com.cyphers.game.RecordSearch.model.search.IoSearchDetailResultHistoryInfo;
import com.cyphers.game.RecordSearch.model.search.ItemInfoResponse;
import com.cyphers.game.RecordSearch.model.search.TeamPlayerResponse;
import com.cyphers.game.RecordSearch.openapi.model.CyphersCharacterAttribute;
import com.cyphers.game.RecordSearch.openapi.model.CyphersCharacterInfo;
import com.cyphers.game.RecordSearch.openapi.model.CyphersCharacterSearch;
import com.cyphers.game.RecordSearch.openapi.model.CyphersEquipItems;
import com.cyphers.game.RecordSearch.openapi.model.CyphersItemDetailInfo;
import com.cyphers.game.RecordSearch.openapi.model.CyphersMatchedInfo;
import com.cyphers.game.RecordSearch.openapi.model.CyphersMatches;
import com.cyphers.game.RecordSearch.openapi.model.CyphersMatchingDetails;
import com.cyphers.game.RecordSearch.openapi.model.CyphersMatchingHistory;
import com.cyphers.game.RecordSearch.openapi.model.CyphersPlayInfo;
import com.cyphers.game.RecordSearch.openapi.model.CyphersPlayer;
import com.cyphers.game.RecordSearch.openapi.model.CyphersPlayerResponse;
import com.cyphers.game.RecordSearch.openapi.model.CyphersPlayersInGame;
import com.cyphers.game.RecordSearch.openapi.model.CyphersPositionAttribute;
import com.cyphers.game.RecordSearch.openapi.model.enumuration.CyphersGameType;
import com.cyphers.game.RecordSearch.openapi.model.enumuration.CyphersPlayerWordType;
import com.cyphers.game.RecordSearch.openapi.service.CyphersApiService;
import com.cyphers.game.RecordSearch.utils.ApiDate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {

	private final CyphersApiService cyApiService;

	final static Integer RECORDS_LIMIT = 20; // 한번에 가져올 기록의 개수
	final static Integer MOST_CYPHER_LENGTH = 10; // 모스트 사이퍼에서 보여줄 캐릭터 개수
	final static Integer RECENT_CYPHER_LENGTH = 3; // 최근 2주간 데이터에서 보여줄 캐릭터 개수
	final static Float PERFECT_KDA = -1.0f; // kda에서 death수가 0일 경우 리턴할 값
	final static Integer WIN_AND_LOSE_KEY = 13; // 승패 그래프에서 보여줄 데이터 키(0~6, 총 7개)
	
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
	
	public String getNickname(String nickname) throws Exception {
		CyphersPlayerResponse cyPlayerRes = cyApiService.searchPlayers(nickname, CyphersPlayerWordType.MATCH, null);
		return cyPlayerRes.getRows().get(0).getNickname();
	}
	
	public IoSearchDetail renewalDetailSearch(String nickname, CyphersGameType gameType) throws Exception {

		CyphersPlayerResponse cyPlayerResponse = cyApiService.searchPlayers(nickname, CyphersPlayerWordType.MATCH, null);

		if (CollectionUtils.isEmpty(cyPlayerResponse.getRows())) {
			throw new Exception("닉네임 정보가 없습니다.");
		}

		IoSearchDetail ioDetail = new IoSearchDetail();
		CyphersPlayer cyPlayer = cyPlayerResponse.getRows().get(0);
		String myPlayerId = cyPlayer.getPlayerId();
		CyphersCharacterSearch cyCharacter = cyApiService.searchCharacter();

		// pk가 될 플레이어ID
		ioDetail.setPlayerId(myPlayerId);
		ioDetail.setNickname(nickname);

		List<CyphersMatchedInfo> totalMatchedInfoRows = getMatchedInfos(myPlayerId, gameType); // 모스트 사이퍼, 모스트 포지션용 리스트
		LocalDate today = LocalDate.now();
		LocalDate twoWeekAgo = today.minusWeeks(2).plusDays(1);	//14일을 범위로 지정하기 때문에 2주전 당일을 제외할 필요가 있음
		List<CyphersMatchedInfo> twoWeekMatchedInfoRows = filterDataByDate(totalMatchedInfoRows, twoWeekAgo, today);	//승패 그래프, 최근 게임기록 분석용 리스트

		//모스트사이퍼, 모스트 포지션에 사용될 데이터
		Map<String, Pair<Integer, Integer>> totalCharacterIdMap = new HashMap<>(); // Pair의 첫번째는 전체 플레이 횟수, 두번째는 이긴 횟수
		for (CyphersMatchedInfo cyMatchedInfo : totalMatchedInfoRows) {
			CyphersPlayInfo cyPlayInfo = cyMatchedInfo.getPlayInfo();
			String characterId = cyPlayInfo.getCharacterId();
			Pair<Integer, Integer> playAndWinCount = totalCharacterIdMap.getOrDefault(characterId, Pair.of(0, 0));

			totalCharacterIdMap.put(characterId, Pair.of(playAndWinCount.getFirst() + 1,
					playAndWinCount.getSecond() + (cyPlayInfo.getResult().equals("win") ? 1 : 0)));
		}
		List<String> totalIdList = new ArrayList<>(totalCharacterIdMap.keySet());
		Collections.sort(totalIdList, Comparator.comparing(id -> id,
				(a, b) -> totalCharacterIdMap.get(b).getFirst() - totalCharacterIdMap.get(a).getFirst()));	//게임 수가 많은 캐릭터부터 내림차순 정렬

		
		//승패 그래프, 최근 게임기록 분석에 사용될 데이터
		Map<String, Pair<Integer, Integer>> twoWeekCharacterIdMap = new HashMap<>(); // Pair의 첫번째는 전체 플레이 횟수, 두번째는 이긴 횟수
		for (CyphersMatchedInfo cyMatchedInfo : twoWeekMatchedInfoRows) {
			CyphersPlayInfo cyPlayInfo = cyMatchedInfo.getPlayInfo();
			String characterId = cyPlayInfo.getCharacterId();
			Pair<Integer, Integer> playAndWinCount = twoWeekCharacterIdMap.getOrDefault(characterId, Pair.of(0, 0));

			twoWeekCharacterIdMap.put(characterId, Pair.of(playAndWinCount.getFirst() + 1,
					playAndWinCount.getSecond() + (cyPlayInfo.getResult().equals("win") ? 1 : 0)));
		}
		List<String> twoWeekIdList = new ArrayList<>(twoWeekCharacterIdMap.keySet());
		Collections.sort(twoWeekIdList, Comparator.comparing(id -> id,
						(a, b) -> twoWeekCharacterIdMap.get(b).getFirst() - twoWeekCharacterIdMap.get(a).getFirst()));	//게임 수가 많은 캐릭터부터 내림차순 정렬


		// 모스트 사이퍼
		ioDetail.setMostCypherInfos(Collections.emptyList());

		if (totalMatchedInfoRows.size() != 0) {
			List<IoSearchDetailMostCypherInfo> mostCypherRows = new ArrayList<>();
			
			//만약 모스트 사이퍼에 선정할 캐릭터가 MOST_CYPHER_LENGTH보다 적다면 idList만큼만 생성
			for (int i = 0; i < (totalIdList.size() >= MOST_CYPHER_LENGTH ? MOST_CYPHER_LENGTH : totalIdList.size()); i++) {
				IoSearchDetailMostCypherInfo mostCypherInfo = new IoSearchDetailMostCypherInfo();
				mostCypherInfo.setCharacterImage("https://img-api.neople.co.kr/cy/characters/"+totalIdList.get(i)+"?zoom=3");
				for (CyphersCharacterInfo characterInfo : cyCharacter.getRows()) {
					if (characterInfo.getCharacterId().equals(totalIdList.get(i))) {
						mostCypherInfo.setCharacterName(characterInfo.getCharacterName());
						break;
					}
				}
				Integer playCount = totalCharacterIdMap.get(totalIdList.get(i)).getFirst();
				Integer winCount = totalCharacterIdMap.get(totalIdList.get(i)).getSecond();
				mostCypherInfo.setPlayCount(playCount);
				mostCypherInfo.setWinRate(100 * winCount / playCount);

				Float killCount = 0.0f;
				Float deathCount = 0.0f;
				Float assistCount = 0.0f;
				for (CyphersMatchedInfo cyMatchedInfo : totalMatchedInfoRows) {
					CyphersPlayInfo cyPlayInfo = cyMatchedInfo.getPlayInfo();
					if (cyPlayInfo.getCharacterId().equals(totalIdList.get(i))) {
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
			ioDetail.setMostCypherInfos(mostCypherRows);

		}

		// 모스트 포지션
		Integer tankerUseCount = 0;
		Integer rangeDealerUseCount = 0;
		Integer supporterUseCount = 0;
		Integer meleeDealerUseCount = 0;
		Integer mostPositionPlayCount = totalMatchedInfoRows.size();

		ioDetail.setTankerUseRate(0);
		ioDetail.setRangeDealerUseRate(0);
		ioDetail.setSupporterUseRate(0);
		ioDetail.setMeleeDealerUseRate(0);

		if (mostPositionPlayCount != 0) {
			for (CyphersMatchedInfo cyMatchedInfo : totalMatchedInfoRows) {
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
			ioDetail.setTankerUseRate((100 * tankerUseCount) / mostPositionPlayCount);
			ioDetail.setRangeDealerUseRate((100 * rangeDealerUseCount) / mostPositionPlayCount);
			ioDetail.setSupporterUseRate((100 * supporterUseCount) / mostPositionPlayCount);
			ioDetail.setMeleeDealerUseRate((100 * meleeDealerUseCount) / mostPositionPlayCount);
		}

		

		// 승패 데이터(그래프)
		List<IoSearchDetailResultHistoryInfo> resultHistory = new ArrayList<>();

		Map<Integer, Pair<Integer, Integer>> cyMatchingHistoryMap = new HashMap<>(); // pair 앞은 승수, 뒤는 패수
		Pair<LocalDate, Integer> matchedDateAndInt = Pair.of(today, WIN_AND_LOSE_KEY);	//그래프 날짜 값. 최초 날짜는 오늘, 13(0~13의 기록을 저장하기 때문에)

		//초기 값을 모두 넣어야 판 수가 0인 날짜에도 승,패수가 각각 0이 들어감
		for (int i = 0; i <= WIN_AND_LOSE_KEY; i++) {
			IoSearchDetailResultHistoryInfo defaultResultHisory = new IoSearchDetailResultHistoryInfo();
			defaultResultHisory.setHistoryDate(i);
			defaultResultHisory.setWinCount(0);
			defaultResultHisory.setLoseCount(0);
			resultHistory.add(defaultResultHisory);
			cyMatchingHistoryMap.put(i, Pair.of(0, 0));
		}

		for (CyphersMatchedInfo weeklyMatchedInfo : twoWeekMatchedInfoRows) {
			IoSearchDetailResultHistoryInfo winAndLoseHistory = new IoSearchDetailResultHistoryInfo();
			
			//주간 게임 기록에서 가져온 날짜가 그래프 날짜 값과 같아질 때까지 날짜-1을 실행
			//날짜를 매치해야 현 루프의 데이터를 해당 날짜에 저장 가능하기 때문.
			LocalDate weeklyMatchedDate = weeklyMatchedInfo.getDate().toLocalDate();
			while (!weeklyMatchedDate.isEqual(matchedDateAndInt.getFirst())) {
				matchedDateAndInt = Pair.of(matchedDateAndInt.getFirst().minusDays(1),
											matchedDateAndInt.getSecond() - 1); // 날짜 및 정수 감소
			}
			if (matchedDateAndInt.getSecond() < 0) {
				break;
			}
			
			// 같은날인지 체크하지 않으면, 중간에 빈 날이 그 전날의 기록을 조회해버림. ex) 9/15에 기록이 없으면 9/14의 기록을 참조함.
			if (weeklyMatchedDate.isEqual(matchedDateAndInt.getFirst())) {
				Pair<Integer, Integer> winAndLoseCount = cyMatchingHistoryMap.get(matchedDateAndInt.getSecond());
				
				//결과가 win이면 winCount+1, 결과가 lose이거나 stop이면 loseCount+1
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
			winAndLoseHistory.setHistoryDate(matchedDateAndInt.getSecond());
			winAndLoseHistory.setWinCount(cyMatchingHistoryMap.get(matchedDateAndInt.getSecond()).getFirst());
			winAndLoseHistory.setLoseCount(cyMatchingHistoryMap.get(matchedDateAndInt.getSecond()).getSecond());
			resultHistory.set(matchedDateAndInt.getSecond(), winAndLoseHistory);
		}

		ioDetail.setResultHistory(resultHistory);

		// 최근 2주간 게임 데이터
		Integer recentlyPlayCount = twoWeekMatchedInfoRows.size();
		ioDetail.setRecentlyPlayCount(0);
		ioDetail.setRecentlyWinRate(0);
		ioDetail.setRecentlyKda(0.0f);
		ioDetail.setRecentlyAverageSurvivalRate(0);
		ioDetail.setRecentlyPlayCyphersInfos(Collections.emptyList());

		if (recentlyPlayCount != 0) {

			ioDetail.setRecentlyPlayCount(recentlyPlayCount);

			Integer recentlyWinCount = 0;
			for (CyphersMatchedInfo recentMatchedInfo : twoWeekMatchedInfoRows) {
				String result = recentMatchedInfo.getPlayInfo().getResult();
				if (result.equals("win")) {
					recentlyWinCount++;
				}
			}
			ioDetail.setRecentlyWinRate(100 * recentlyWinCount / recentlyPlayCount);

			Float totalKillCount = 0.0f;
			Float totalDeathCount = 0.0f;
			Float totalAssistCount = 0.0f;
			for (CyphersMatchedInfo recentMatchedInfo : twoWeekMatchedInfoRows) {
				CyphersPlayInfo cyPlayInfo = recentMatchedInfo.getPlayInfo();
				totalKillCount += cyPlayInfo.getKillCount();
				totalDeathCount += cyPlayInfo.getDeathCount();
				totalAssistCount += cyPlayInfo.getAssistCount();
			}
			if (totalDeathCount != 0) {
				ioDetail.setRecentlyKda(
						Math.round((totalKillCount + totalAssistCount) / totalDeathCount * 100) / 100.0f);
			} else {
				ioDetail.setRecentlyKda(PERFECT_KDA);
			}

			Integer avgSurvivalRate = 0;
			for (CyphersMatchedInfo recentMatchedInfo : twoWeekMatchedInfoRows) {
				Integer playTime = recentMatchedInfo.getPlayInfo().getPlayTime();
				Integer responseTime = recentMatchedInfo.getPlayInfo().getResponseTime();

				avgSurvivalRate += 100 * (playTime - responseTime) / playTime;
			}
			ioDetail.setRecentlyAverageSurvivalRate(avgSurvivalRate / recentlyPlayCount);

			// 최근 플레이 횟수 top3 캐릭터 데이터
			List<IoSearchDetailRecentlyPlayCyphersInfo> recentCypherRows = new ArrayList<>();

			for (int i = 0; i < RECENT_CYPHER_LENGTH; i++) {
				IoSearchDetailRecentlyPlayCyphersInfo recentCypherInfo = new IoSearchDetailRecentlyPlayCyphersInfo();
				recentCypherInfo.setCharacterImage("https://img-api.neople.co.kr/cy/characters/"+twoWeekIdList.get(i)+"?zoom=3");
				for (CyphersCharacterInfo characterInfo : cyCharacter.getRows()) {
					if (characterInfo.getCharacterId().equals(twoWeekIdList.get(i))) {
						recentCypherInfo.setCharacterName(characterInfo.getCharacterName());
						break;
					}
				}
				recentCypherInfo.setWinCount(twoWeekCharacterIdMap.get(twoWeekIdList.get(i)).getSecond());
				recentCypherInfo.setLoseCount(
						twoWeekCharacterIdMap.get(twoWeekIdList.get(i)).getFirst() - twoWeekCharacterIdMap.get(twoWeekIdList.get(i)).getSecond());

				Float avgKillCount = 0.0f;
				Float avgDeathCount = 0.0f;
				Float avgAssistCount = 0.0f;
				Float denominator = 0.0f;
				for (CyphersMatchedInfo recentMatchedInfo : twoWeekMatchedInfoRows) {
					CyphersPlayInfo cyPlayInfo = recentMatchedInfo.getPlayInfo();
					if (cyPlayInfo.getCharacterId().equals(twoWeekIdList.get(i))) {
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

			ioDetail.setRecentlyPlayCyphersInfos(recentCypherRows);
		}

		return ioDetail;
	}
	
	public CyphersMatches getGameRecordsNext(String playerId, String next) throws Exception {

		CyphersMatches cyMatches = cyApiService.searchGameRecords(playerId, next);
		return cyMatches;
		
	}
	
	public CyphersMatches getGameRecordsFirst(String playerId, CyphersGameType gameType, String startDate, String endDate) throws Exception {
		
		CyphersMatches cyMatches = cyApiService.searchGameRecords(playerId, gameType, startDate, endDate, RECORDS_LIMIT);
		return cyMatches;
	}

	public GameRecordResponse getGameRecords(CyphersMatches cyMatches, String playerId) throws Exception {
			
		GameRecordResponse gameRecordsInfo = new GameRecordResponse();
		
		List<CyphersMatchedInfo> cyMatchedInfos = cyMatches.getRows();
		
		List<IoSearchDetailGameRecord> gameRecords = new ArrayList<>();
		gameRecordsInfo.setGameRecords(Collections.emptyList());
		gameRecordsInfo.setNext("no more records");
		if (cyMatches.getNext() != null) {
			gameRecordsInfo.setNext(cyMatches.getNext());
		}

		if (cyMatchedInfos.size() > 0) {

			for (CyphersMatchedInfo matchedInfo : cyMatchedInfos) {
				IoSearchDetailGameRecord gameRecord = new IoSearchDetailGameRecord();
				CyphersMatchingDetails matchingDetail = cyApiService.searchMatchingDetail(matchedInfo.getMatchId());
				Integer totalKillCount = 0;
				for (CyphersPlayersInGame cyPlayersInGame : matchingDetail.getPlayers()) {
					totalKillCount += cyPlayersInGame.getPlayInfo().getKillCount();
				}

				List<TeamPlayerResponse> teamPlayerRes = new ArrayList<>();

				for (CyphersPlayersInGame playerDataInGame : matchingDetail.getPlayers()) {
					
					if (playerDataInGame.getPlayerId().equals(playerId)) {
						CyphersPlayInfo playInfo = playerDataInGame.getPlayInfo();

						gameRecord.setCharacterId(playInfo.getCharacterId());
						gameRecord.setCharacterName(playInfo.getCharacterName());
						gameRecord.setResult(matchedInfo.getPlayInfo().getResult());
						gameRecord.setPositionName(playerDataInGame.getPosition().getName());
						List<CyphersCharacterAttribute> attributeInfos = new ArrayList<>();
						for (CyphersCharacterAttribute attributeInfo : playerDataInGame.getPosition().getAttribute()) {
							attributeInfos.add(attributeInfo);
						}
						gameRecord.setKillParticipation(0);
						if (totalKillCount > 0) {
							gameRecord.setKillParticipation(
									100 * (playInfo.getKillCount() + playInfo.getAssistCount()) / totalKillCount);
						}

						gameRecord.setAttributeInfos(attributeInfos);
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

						Map<String, CyphersEquipItems> defaultItemData = new LinkedHashMap<>();
						CyphersEquipItems tempItem = new CyphersEquipItems();
						defaultItemData.put("101", tempItem);
						defaultItemData.put("102", tempItem);
						defaultItemData.put("103", tempItem);
						defaultItemData.put("104", tempItem);
						defaultItemData.put("105", tempItem);
						defaultItemData.put("106", tempItem);
						defaultItemData.put("202", tempItem);
						defaultItemData.put("203", tempItem);
						defaultItemData.put("301", tempItem);
						defaultItemData.put("302", tempItem);
						defaultItemData.put("303", tempItem);
						defaultItemData.put("304", tempItem);
						defaultItemData.put("305", tempItem);
						defaultItemData.put("107", tempItem);
						defaultItemData.put("204", tempItem);
						defaultItemData.put("205", tempItem);
						for (CyphersEquipItems item : playerDataInGame.getItems()) {
							defaultItemData.put(item.getSlotCode(), item);
						}
						List<CyphersEquipItems> itemInfos = new ArrayList<>(defaultItemData.values());

						gameRecord.setItemInfos(itemInfos);

						gameRecord.setHealAmount(playInfo.getHealAmount());
						gameRecord.setAttackPoint(playInfo.getAttackPoint());
						gameRecord.setDamagePoint(playInfo.getDamagePoint());
						gameRecord.setGetCoin(playInfo.getGetCoin());
						gameRecord.setBattlePoint(playInfo.getBattlePoint());
						gameRecord.setSightPoint(playInfo.getSightPoint());
					}
					teamPlayerRes.add(TeamPlayerResponse.builder()
										.characterId(playerDataInGame.getPlayInfo().getCharacterId())
										.nickname(playerDataInGame.getNickname())
										.build());
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
				gameRecord.setMatchId(matchedInfo.getMatchId());
				gameRecord.setPlayDate(matchingDetail.getDate());
				gameRecord.setTeamPlayerInfos(teamPlayerRes);
				gameRecords.add(gameRecord);

				gameRecordsInfo.setGameRecords(gameRecords);
			}

		} 
		return gameRecordsInfo;
	}
	
	// 현재 시즌 공식전, 일반전 기록 가져오기
	// API 사양은 90일씩만 허용하기 때문에 90일씩 두번 검색
	public List<CyphersMatchedInfo> getAllMatchedInfos(String playerId) throws Exception {
		
		CyphersMatchingHistory cyMatchingHistoryRating = cyApiService.searchMatchingHistory(playerId,
				CyphersGameType.RATING, ApiDate.NINETY_DAYS_AGO, ApiDate.NOW);
		CyphersMatchingHistory cyMatchingHistoryRating2 = cyApiService.searchMatchingHistory(playerId,
				CyphersGameType.RATING, ApiDate.HALF_YEARS_AGO, ApiDate.NINETY_DAYS_AGO);
		CyphersMatchingHistory cyMatchingHistoryNormal = cyApiService.searchMatchingHistory(playerId,
				CyphersGameType.NORMAL, ApiDate.NINETY_DAYS_AGO, ApiDate.NOW);
		CyphersMatchingHistory cyMatchingHistoryNormal2 = cyApiService.searchMatchingHistory(playerId,
				CyphersGameType.NORMAL, ApiDate.HALF_YEARS_AGO, ApiDate.NINETY_DAYS_AGO);

		List<CyphersMatchedInfo> matchedInfos = new ArrayList<>(); // 각 기능에서 쓰일 리스트

		for (CyphersMatchedInfo cyMatchedInfoRating : cyMatchingHistoryRating.getMatches().getRows()) {
			matchedInfos.add(cyMatchedInfoRating);
		}
		for (CyphersMatchedInfo cyMatchedInfoRating2 : cyMatchingHistoryRating2.getMatches().getRows()) {
			matchedInfos.add(cyMatchedInfoRating2);
		}
		for (CyphersMatchedInfo cyMatchedInfoNormal : cyMatchingHistoryNormal.getMatches().getRows()) {
			matchedInfos.add(cyMatchedInfoNormal);
		}
		for (CyphersMatchedInfo cyMatchedInfoNormal2 : cyMatchingHistoryNormal2.getMatches().getRows()) {
			matchedInfos.add(cyMatchedInfoNormal2);
		}
		Comparator<CyphersMatchedInfo> comparator = new Comparator<CyphersMatchedInfo>() {
			@Override
			public int compare(CyphersMatchedInfo cy1, CyphersMatchedInfo cy2) {
				LocalDateTime dateTime1 = cy1.getDate();
				LocalDateTime dateTime2 = cy2.getDate();
				return dateTime2.compareTo(dateTime1);
			}
		};
		Collections.sort(matchedInfos, comparator);
		return matchedInfos;
	}
	
	public List<CyphersMatchedInfo> getMatchedInfos(String playerId, CyphersGameType gameType) throws Exception {
		CyphersMatchingHistory cyMatchingHistory1 = cyApiService.searchMatchingHistory(playerId,
				gameType, ApiDate.NINETY_DAYS_AGO, ApiDate.NOW);
		CyphersMatchingHistory cyMatchingHistory2 = cyApiService.searchMatchingHistory(playerId,
				gameType, ApiDate.HALF_YEARS_AGO, ApiDate.NINETY_DAYS_AGO);
		
		List<CyphersMatchedInfo> matchedInfos = new ArrayList<>();

		for (CyphersMatchedInfo cyMatchedInfo1 : cyMatchingHistory1.getMatches().getRows()) {
			matchedInfos.add(cyMatchedInfo1);
		}
		for (CyphersMatchedInfo cyMatchedInfo2 : cyMatchingHistory2.getMatches().getRows()) {
			matchedInfos.add(cyMatchedInfo2);
		}
		return matchedInfos;
	}
	
	// 데이터 필터링 메소드
	private static List<CyphersMatchedInfo> filterDataByDate(List<CyphersMatchedInfo> dataList, LocalDate startDate,
			LocalDate endDate) {
		List<CyphersMatchedInfo> filteredData = new ArrayList<>();
		for (CyphersMatchedInfo data : dataList) {
			LocalDate dataDate = data.getDate().toLocalDate();
			if (!dataDate.isBefore(startDate) && !dataDate.isAfter(endDate)) {
				filteredData.add(data);
			}
		}
		return filteredData;
	}
	
	//api로 아이템 정보 가져오기
	public ItemInfoResponse getItemDetailInfo(String itemId) throws Exception {
		CyphersItemDetailInfo cyItemDetail = cyApiService.searchItemDetail(itemId);
		ItemInfoResponse itemRes = ItemInfoResponse.builder()
							.itemId(itemId)
							.itemName(cyItemDetail.getItemName())
							.rarity(cyItemDetail.getRarityName())
							.slotName(cyItemDetail.getSlotName())
							.seasonName(cyItemDetail.getSeasonName())
							.explainDetail(cyItemDetail.getExplainDetail())
							.build();
		return itemRes;
	}
	
	//api로 특성 정보 가져오기
	public AttributeInfoResponse getAttributeDetailInfo(String attributeId) throws Exception {
		CyphersPositionAttribute cyAttrDetail = cyApiService.searchAttribute(attributeId);
		AttributeInfoResponse attrRes = AttributeInfoResponse.builder()
							.attributeId(cyAttrDetail.getAttributeId())
							.attributeName(cyAttrDetail.getAttributeName())
							.explain(cyAttrDetail.getExplain())
							.positionName(cyAttrDetail.getPositionName())
							.build();
		return attrRes;
	}
	
	public CyphersGameType getGameType(String gameType) {
		switch (gameType) {
			case "rating": {
				return CyphersGameType.RATING;
			}
			case "normal": {
				return CyphersGameType.NORMAL;
			}
			default:
				throw new IllegalArgumentException("잘못된 게임타입: " + gameType);
			}
	}
}
