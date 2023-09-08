package com.cyphers.game.RecordSearch.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

import com.cyphers.game.RecordSearch.controller.search.model.IoSearchDetailGameRecord;
import com.cyphers.game.RecordSearch.controller.search.model.IoSearchDetailMostCypherInfo;
import com.cyphers.game.RecordSearch.controller.search.model.IoSearchDetailMostPositionInfo;
import com.cyphers.game.RecordSearch.controller.search.model.IoSearchDetailRecentlyPlayCyphersInfo;
import com.cyphers.game.RecordSearch.controller.search.model.IoSearchDetailResponse;
import com.cyphers.game.RecordSearch.controller.search.model.IoSearchDetailWinAndLoseCountHistoryInfo;
import com.cyphers.game.RecordSearch.cyphers.CyphersApiService;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersCharacterSearch;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersMatchedInfo;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersMatchingDetails;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersMatchingHistory;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersPlayInfo;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersPlayerInfo;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersPlayerResponse;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersPlayersInGame;
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
//    @Autowired
//    IoSearchDetailResponse ioGameRecords;

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
    	
    	IoSearchDetailResponse ioGameRecords = new IoSearchDetailResponse();
    	String myPlayerId = cyApiService.searchPlayers(nickname, CyphersPlayerWordType.MATCH, null)
				.getRows().get(0).getPlayerId();
    	CyphersCharacterSearch cyCharacter = cyApiService.searchCharacter();
        
    	
    	//플레이어 기본정보
    	String profileCharacterId = cyApiService.searchPlayerInfo(myPlayerId).getRepresent().getCharacterId();
    	String profileNickname = cyApiService.searchPlayerInfo(myPlayerId).getRepresent().getCharacterName();
    	ioGameRecords.setProfileCharacterId(profileCharacterId);
    	ioGameRecords.setNickname(profileNickname);
    	
    	
    	//모스트 사이퍼
    	Integer limit = 100;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime ninetyDaysAgo = now.minus(90, ChronoUnit.DAYS); // 게임기록 90일 서치
        DateTimeFormatter apiSearchFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String mostStartDate = ninetyDaysAgo.format(apiSearchFormatter).toString();
        String mostEndDate = now.format(apiSearchFormatter).toString();
        
        CyphersMatchingHistory mostCyMatchingHistoryRating = cyApiService.searchMatchingHistory(myPlayerId, CyphersGameType.RATING, mostStartDate, mostEndDate, limit);

        Map<String, Pair<Integer, Integer>> mostCharacterIdMap = new HashMap<>();	//Pair의 첫번째는 전체 플레이 횟수, 두번째는 이긴 횟수
        for (int i = 0; i < mostCyMatchingHistoryRating.getMatches().getRows().size(); i++) {
        	CyphersPlayInfo cyPlayInfo = mostCyMatchingHistoryRating.getMatches().getRows().get(i).getPlayInfo();
        	String characterId = cyPlayInfo.getCharacterId();
        	Pair<Integer, Integer> playAndWinCount = mostCharacterIdMap.getOrDefault(characterId, Pair.of(0, 0));
        	
        	mostCharacterIdMap.put(characterId, Pair.of(playAndWinCount.getFirst() + 1, 
        											playAndWinCount.getSecond() + (cyPlayInfo.getResult().equals("win") ? 1 : 0)));
		}
        
        List<String> mostIdList = new ArrayList<>();
        for (String string : mostCharacterIdMap.keySet()) {
        	mostIdList.add(string);
		}
        Collections.sort(mostIdList, Comparator.comparing(id -> id, (a, b) -> mostCharacterIdMap.get(b).getFirst() - mostCharacterIdMap.get(a).getFirst()));;
        
        List<IoSearchDetailMostCypherInfo> mostCypherRows = new ArrayList<>();	//10명 선정(내림차순)
        
        Integer mostCypherLength = 10;
        for (int i = 0; i < mostCypherLength; i++) {
			IoSearchDetailMostCypherInfo mostCypherInfo = new IoSearchDetailMostCypherInfo();
			mostCypherInfo.setCharacterId(mostIdList.get(i));
			for (int j = 0; j < cyCharacter.getRows().size(); j++) {
				if (cyCharacter.getRows().get(j).getCharacterId().equals(mostIdList.get(i))) {
					mostCypherInfo.setCharacterName(cyCharacter.getRows().get(j).getCharacterName());
					break;
				}
			}
			
			Integer winCount = mostCharacterIdMap.get(mostIdList.get(i)).getSecond();
			Integer playCount = mostCharacterIdMap.get(mostIdList.get(i)).getFirst();
			mostCypherInfo.setWinRate(100 * winCount / playCount);
			mostCypherInfo.setPlayCount(playCount);
			
			Float killCount = 0.0f;
			Float deathCount = 0.0f;
			Float assistCount = 0.0f;
			for (int j = 0; j < mostCyMatchingHistoryRating.getMatches().getRows().size(); j++) {
				CyphersPlayInfo cyPlayInfo = mostCyMatchingHistoryRating.getMatches().getRows().get(j).getPlayInfo();
				if (cyPlayInfo.getCharacterId().equals(mostIdList.get(i))) {
					killCount += cyPlayInfo.getKillCount();
					deathCount += cyPlayInfo.getDeathCount();
					assistCount += cyPlayInfo.getAssistCount();
				}
			}
			if (deathCount != 0) {
				mostCypherInfo.setKda(Math.round((killCount + assistCount) / deathCount * 100) / 100.0f);
			} else {
				mostCypherInfo.setKda(-1.0f);
			}
			
			mostCypherRows.add(mostCypherInfo);
		}
        ioGameRecords.setMostCypherInfos(mostCypherRows);
        
        //모스트 포지션
        IoSearchDetailMostPositionInfo mostPositionInfo = new IoSearchDetailMostPositionInfo();
        
    	Integer tankerUseCount = 0;
    	Integer rangeDealerUseCount = 0;
    	Integer supporterUseCount = 0;
    	Integer meleeDealerUseCount = 0;
    	Integer mostPositionPlayCount = mostCyMatchingHistoryRating.getMatches().getRows().size();
    	for (int i = 0; i < mostPositionPlayCount; i++) {
			String postion = mostCyMatchingHistoryRating.getMatches().getRows().get(i).getPosition().getName();
			if (postion.equals("탱커")) {
				tankerUseCount++;
			} else if(postion.equals("원거리딜러")){
				rangeDealerUseCount++;
			} else if(postion.equals("서포터")) {
				supporterUseCount++;
			} else if(postion.equals("근거리딜러")) {
				meleeDealerUseCount++;
			}
		}
    	mostPositionInfo.setTankerUseRate(100 * tankerUseCount / mostPositionPlayCount);
    	mostPositionInfo.setRangeDealerUseRate(100 * rangeDealerUseCount / mostPositionPlayCount);
    	mostPositionInfo.setSupporterUseRate(100 * supporterUseCount / mostPositionPlayCount);
    	mostPositionInfo.setMeleeDealerUseRate(100 * meleeDealerUseCount / mostPositionPlayCount);
    	ioGameRecords.setMostPositionInfos(mostPositionInfo);
        
        
    	//공식전, 일반전 데이터
        CyphersPlayerInfo cyPlayerInfo = cyApiService.searchPlayerInfo(myPlayerId); 
        
        if (!(cyPlayerInfo.getRecords().size() == 0)) {
        	String gameTypeIdRow0 = cyPlayerInfo.getRecords().get(0).getGameTypeId();	//Rows의 0번째를 가져옴
        	
        	if (gameTypeIdRow0.equals("rating")) {
        		ioGameRecords.setRatingGameTier(cyPlayerInfo.getTierName());
                ioGameRecords.setRatingWinCount(cyPlayerInfo.getRecords().get(0).getWinCount());
                ioGameRecords.setRatingLoseCount(cyPlayerInfo.getRecords().get(0).getLoseCount());
                ioGameRecords.setRatingStopCount(cyPlayerInfo.getRecords().get(0).getStopCount());
                ioGameRecords.setRatingWinRate(100 * ioGameRecords.getRatingWinCount() / 
                							(ioGameRecords.getRatingLoseCount() + ioGameRecords.getRatingWinCount()));
			}
    		if (gameTypeIdRow0.equals("normal") || cyPlayerInfo.getRecords().size() == 2) {
        		Integer recordsLength = Math.max(0, cyPlayerInfo.getRecords().size()-1);
                ioGameRecords.setNormalWinCount(cyPlayerInfo.getRecords().get(recordsLength).getWinCount());        
                ioGameRecords.setNormalLoseCount(cyPlayerInfo.getRecords().get(recordsLength).getLoseCount());        
                ioGameRecords.setNormalStopCount(cyPlayerInfo.getRecords().get(recordsLength).getStopCount());        
                ioGameRecords.setNormalWinRate(100 * ioGameRecords.getNormalWinCount() / 
                						(ioGameRecords.getNormalLoseCount() + ioGameRecords.getNormalWinCount()));
			}
		}
        
        
        //승, 패수 데이터(그래프)
        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter graphFormatter = DateTimeFormatter.ofPattern("MM-dd");
        LocalDateTime oneWeekAgo = today.minusWeeks(1);
        String winAndLoseStartDate = oneWeekAgo.format(apiSearchFormatter).toString();
        String winAndLoseEndDate = today.format(apiSearchFormatter).toString();
        CyphersMatchingHistory weeklyCyMatchingHistory = cyApiService.searchMatchingHistory(myPlayerId, CyphersGameType.RATING, winAndLoseStartDate, winAndLoseEndDate, limit);	//limit은 100
        
        List<IoSearchDetailWinAndLoseCountHistoryInfo> winAndLoseCountHistoryInfos = new ArrayList<>();
        
        while (today.isAfter(oneWeekAgo)) {
        	IoSearchDetailWinAndLoseCountHistoryInfo winAndLoseHisory = new IoSearchDetailWinAndLoseCountHistoryInfo();
        	
        	winAndLoseHisory.setHistoryDate(today.format(graphFormatter));
        	
        	Integer winCount = 0;
        	Integer loseCount = 0;
        	for (int i = 0; i < weeklyCyMatchingHistory.getMatches().getRows().size(); i++) {
            	CyphersMatchedInfo cyMatchedInfo = weeklyCyMatchingHistory.getMatches().getRows().get(i);
            	
            	String matchedDate = cyMatchedInfo.getDate();
            	String[] matchedDateAndTime = matchedDate.split(" ");
            	
            	String todayDate = today.format(apiSearchFormatter);
            	String[] todayDateAndTime = todayDate.split(" ");
            	
            	if (matchedDateAndTime[0].equals(todayDateAndTime[0])) {
            		if (cyMatchedInfo.getPlayInfo().getResult().equals("win")) {
						winCount++;
					} else if (cyMatchedInfo.getPlayInfo().getResult().equals("lose")) {
						loseCount++;
					}
				}	//today의 날짜와 mathedInfo의 날짜가 같으면 승패 카운팅
			}
        	winAndLoseHisory.setWinCount(winCount);
        	winAndLoseHisory.setLoseCount(loseCount);
        	
        	winAndLoseCountHistoryInfos.add(winAndLoseHisory);      
        	
            today = today.minusDays(1); // 하루씩 감소
            
        }
        ioGameRecords.setWinAndLoseCountHistoryInfos(winAndLoseCountHistoryInfos);
        
        
        //최근 2주간 게임 데이터
        LocalDateTime twoWeekAgo = now.minus(2, ChronoUnit.WEEKS); // 게임기록 서치 2주 설정
        String recentStartDate = twoWeekAgo.format(apiSearchFormatter).toString();
        String recentEndDate = now.format(apiSearchFormatter).toString();
        
        
        CyphersMatchingHistory recentCyMatchingHistoryNormal = cyApiService.searchMatchingHistory(myPlayerId, CyphersGameType.NORMAL, recentStartDate, recentEndDate, limit);	        // limit = 100

        ioGameRecords.setRecentlyPlayCount(recentCyMatchingHistoryNormal.getMatches().getRows().size());
        
        Integer recentlyWinCount = 0;
        for (int i = 0; i < recentCyMatchingHistoryNormal.getMatches().getRows().size(); i++) {
        	String result = recentCyMatchingHistoryNormal.getMatches().getRows().get(i).getPlayInfo().getResult();
        	if (result.equals("win")) {
        		recentlyWinCount++;
			}
		}
        if (ioGameRecords.getRecentlyPlayCount() != 0) {
        	ioGameRecords.setRecentlyWinRate(100 * recentlyWinCount / ioGameRecords.getRecentlyPlayCount());
		}
        
        Float totalKillCount = 0.0f;
        Float totalDeathCount = 0.0f;
        Float totalAssistCount = 0.0f;
        for (int i = 0; i < recentCyMatchingHistoryNormal.getMatches().getRows().size(); i++) {
        	CyphersPlayInfo cyPlayInfo = recentCyMatchingHistoryNormal.getMatches().getRows().get(i).getPlayInfo();
			totalKillCount += cyPlayInfo.getKillCount();
			totalDeathCount += cyPlayInfo.getDeathCount();
			totalAssistCount += cyPlayInfo.getAssistCount();
		}
        if (ioGameRecords.getRecentlyPlayCount() != 0) {
        	ioGameRecords.setRecentlyKda(Math.round((totalKillCount + totalAssistCount) / totalDeathCount * 100) / 100.0f);
		}
        
        Integer avgSurvivalRate = 0;
        for (int i = 0; i < recentCyMatchingHistoryNormal.getMatches().getRows().size(); i++) {
        	Integer playTime = recentCyMatchingHistoryNormal.getMatches().getRows().get(i).getPlayInfo().getPlayTime();
        	Integer responseTime = recentCyMatchingHistoryNormal.getMatches().getRows().get(i).getPlayInfo().getResponseTime();
        	
        	avgSurvivalRate += 100 * (playTime - responseTime) / playTime;
		}
        Integer survivalPlayCount = recentCyMatchingHistoryNormal.getMatches().getRows().size();
        if (survivalPlayCount != 0) {
        	ioGameRecords.setRecentlyAverageSurvivalRate(avgSurvivalRate / survivalPlayCount);
		}
        
        
        //최근 2주간 top3 캐릭터 데이터
        Map<String, Pair<Integer, Integer>> recentCharacterIdMap = new HashMap<>();	//Pair의 첫번째는 전체 플레이 횟수, 두번째는 이긴 횟수
        for (int i = 0; i < recentCyMatchingHistoryNormal.getMatches().getRows().size(); i++) {
        	CyphersPlayInfo cyPlayInfo = recentCyMatchingHistoryNormal.getMatches().getRows().get(i).getPlayInfo();
        	String characterId = cyPlayInfo.getCharacterId();
        	Pair<Integer, Integer> playAndWinCount = recentCharacterIdMap.getOrDefault(characterId, Pair.of(0, 0));
        	
        	recentCharacterIdMap.put(characterId, Pair.of(playAndWinCount.getFirst() + 1, 
        											playAndWinCount.getSecond() + (cyPlayInfo.getResult().equals("win") ? 1 : 0)));
		}
        
        List<String> recentIdList = new ArrayList<>();
        for (String string : recentCharacterIdMap.keySet()) {
			recentIdList.add(string);
		}
        Collections.sort(recentIdList, Comparator.comparing(id -> id, (a, b) -> recentCharacterIdMap.get(b).getFirst() - recentCharacterIdMap.get(a).getFirst()));	//플레이 수 기준 내림차순 정렬
        
        List<IoSearchDetailRecentlyPlayCyphersInfo> recentCypherRows = new ArrayList<>();
        
        Integer recentCypherLength = 3;	//가져오고 싶은 캐릭터 개수 설정
        for (int i = 0; i < recentCypherLength; i++) {
			IoSearchDetailRecentlyPlayCyphersInfo recentCypherInfo = new IoSearchDetailRecentlyPlayCyphersInfo();
			recentCypherInfo.setCharacterId(recentIdList.get(i));
			for (int j = 0; j < cyCharacter.getRows().size(); j++) {
				if (cyCharacter.getRows().get(j).getCharacterId().equals(recentIdList.get(i))) {
					recentCypherInfo.setCharacterName(cyCharacter.getRows().get(j).getCharacterName());
					break;
				}
			}
			recentCypherInfo.setWinCount(recentCharacterIdMap.get(recentIdList.get(i)).getSecond());
			recentCypherInfo.setLoseCount(recentCharacterIdMap.get(recentIdList.get(i)).getFirst()
										- recentCharacterIdMap.get(recentIdList.get(i)).getSecond());
			
			Float avgKillCount = 0.0f;
			Float avgDeathCount = 0.0f;
			Float avgAssistCount = 0.0f;
			Float denominator = 0.0f;
			for (int j = 0; j < recentCyMatchingHistoryNormal.getMatches().getRows().size(); j++) {
				CyphersPlayInfo cyPlayInfo = recentCyMatchingHistoryNormal.getMatches().getRows().get(j).getPlayInfo();
				if (cyPlayInfo.getCharacterId().equals(recentIdList.get(i))) {
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
        
        
        //최근 게임기록
        List<IoSearchDetailGameRecord> gameRecords = new ArrayList<>();
        limit = 10;	//일단 10개만
        CyphersMatchingHistory recordCyMatchingHistory = cyApiService.searchMatchingHistory(myPlayerId, CyphersGameType.RATING, null, null, limit); 
        
        for (int i = 0; i < recordCyMatchingHistory.getMatches().getRows().size(); i++) {
            IoSearchDetailGameRecord gameRecord = new IoSearchDetailGameRecord();
            String matchId = recordCyMatchingHistory.getMatches().getRows().get(i).getMatchId();
            CyphersMatchingDetails matchingDetail = cyApiService.searchMatchingDetail(matchId);
            
            gameRecord.setGameType(CyphersGameType.RATING);
            
            List<String> playerNicknames = new ArrayList<>();
            
            for (int j = 0; j < matchingDetail.getPlayers().size(); j++) {
				if (matchingDetail.getPlayers().get(j).getPlayerId().equals(myPlayerId)) {
					CyphersPlayersInGame player = matchingDetail.getPlayers().get(j);
					
					gameRecord.setPlayCharacterId(player.getPlayerId());
					gameRecord.setPostionName(player.getPosition().getName());
					List<String> attributeIds = new ArrayList<>();
					for (int k = 0; k < player.getPosition().getAttribute().size(); k++) {
						attributeIds.add(player.getPosition().getAttribute().get(k).getId());
					}
					
					gameRecord.setAttributeIds(attributeIds);
					gameRecord.setKillCount(player.getPlayInfo().getKillCount());
					gameRecord.setDeathCount(player.getPlayInfo().getDeathCount());
					gameRecord.setAssistCount(player.getPlayInfo().getAssistCount());
					
					Float killCount = gameRecord.getKillCount().floatValue();
					Float deathCount = gameRecord.getDeathCount().floatValue();
					Float assistCount = gameRecord.getAssistCount().floatValue();
					gameRecord.setKda(Math.round((killCount + assistCount) / deathCount * 100) / 100.0f);
					gameRecord.setCsCount(gameRecord.getCsCount());
					
					List<String> itemIds = new ArrayList<>();
					for (int k = 0; k < player.getItems().size(); k++) {
						itemIds.add(player.getItems().get(k).getItemId());
					}
					gameRecord.setItemIds(itemIds);
					
					gameRecord.setHealAmount(player.getPlayInfo().getHealAmount());
					gameRecord.setAttackPoint(player.getPlayInfo().getAttackPoint());
					gameRecord.setDamagePoint(player.getPlayInfo().getDamagePoint());
					gameRecord.setGetCoin(player.getPlayInfo().getGetCoin());
					gameRecord.setBattlePoint(player.getPlayInfo().getBattlePoint());
					gameRecord.setSightPoint(player.getPlayInfo().getSightPoint());
				}
				playerNicknames.add(matchingDetail.getPlayers().get(j).getNickname());
			}
            
            gameRecord.setPlayerNicknames(playerNicknames);
            gameRecords.add(gameRecord);
		}
        
        log.info("게임기록 길이: " + gameRecords.size());
        ioGameRecords.setGameRecords(gameRecords);
        
        return ioGameRecords;
    }

}
