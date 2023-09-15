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
import org.springframework.util.CollectionUtils;

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
    
    final static Integer MOST_CYPHER_LIMIT = 100;
    final static Integer MOST_CYPHER_LENGTH = 10;
    final static Integer WIN_LOSE_COUNT_LIMIT = 100;
    final static Integer RECENT_MATCHING_HISTORY_LIMIT = 100;
    final static Integer GAME_RECORD_LIMIT = 100;
    

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
    	
    	CyphersPlayerResponse cyPlayerResponse = cyApiService.searchPlayers(nickname, CyphersPlayerWordType.MATCH, null);
    	
		if (CollectionUtils.isEmpty(cyPlayerResponse.getRows())) {
			throw new Exception("닉네임 정보가 없습니다.");
		} 
		
		IoSearchDetailResponse ioGameRecords = new IoSearchDetailResponse();
    	String myPlayerId = cyPlayerResponse.getRows().get(0).getPlayerId();;
    	CyphersCharacterSearch cyCharacter = cyApiService.searchCharacter();
        
    	
    	//플레이어 기본정보
    	CyphersPlayerRepresent cyPlayerRepresent = cyApiService.searchPlayerInfo(myPlayerId).getRepresent();
    	String profileCharacterId = cyPlayerRepresent.getCharacterId();
    	String profileNickname = cyPlayerRepresent.getCharacterName();
    	ioGameRecords.setProfileCharacterId(profileCharacterId);
    	ioGameRecords.setNickname(profileNickname);
    	
    	
    	//모스트 사이퍼
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime ninetyDaysAgo = now.minus(90, ChronoUnit.DAYS); // 게임기록 90일 서치
        DateTimeFormatter apiSearchFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String mostStartDate = ninetyDaysAgo.format(apiSearchFormatter);
        String mostEndDate = now.format(apiSearchFormatter);
        
        CyphersMatchingHistory mostCyMatchingHistoryRating = cyApiService.searchMatchingHistory(myPlayerId, CyphersGameType.RATING, mostStartDate, mostEndDate, MOST_CYPHER_LIMIT);
        
        ioGameRecords.setMostCypherInfos(Collections.emptyList());
        
        if(mostCyMatchingHistoryRating.getMatches().getRows().size() != 0) {
        	
        	Map<String, Pair<Integer, Integer>> mostCharacterIdMap = new HashMap<>();	//Pair의 첫번째는 전체 플레이 횟수, 두번째는 이긴 횟수
            for (int i = 0; i < mostCyMatchingHistoryRating.getMatches().getRows().size(); i++) {
            	CyphersPlayInfo cyPlayInfo = mostCyMatchingHistoryRating.getMatches().getRows().get(i).getPlayInfo();
            	String characterId = cyPlayInfo.getCharacterId();
            	Pair<Integer, Integer> playAndWinCount = mostCharacterIdMap.getOrDefault(characterId, Pair.of(0, 0));
            	
            	mostCharacterIdMap.put(characterId, Pair.of(playAndWinCount.getFirst() + 1, 
            											playAndWinCount.getSecond() + (cyPlayInfo.getResult().equals("win") ? 1 : 0)));
    		}
            
            List<String> mostIdList = new ArrayList<>(mostCharacterIdMap.keySet());
            Collections.sort(mostIdList, Comparator.comparing(id -> id, (a, b) -> mostCharacterIdMap.get(b).getFirst() - mostCharacterIdMap.get(a).getFirst()));;
            
            List<IoSearchDetailMostCypherInfo> mostCypherRows = new ArrayList<>();	//10명 선정(내림차순)
            
            for (int i = 0; i < MOST_CYPHER_LENGTH; i++) {
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
            
        } 
        
        
        //모스트 포지션
        IoSearchDetailMostPositionInfo mostPositionInfo = new IoSearchDetailMostPositionInfo();
        
    	Integer tankerUseCount = 0;
    	Integer rangeDealerUseCount = 0;
    	Integer supporterUseCount = 0;
    	Integer meleeDealerUseCount = 0;
    	Integer mostPositionPlayCount = mostCyMatchingHistoryRating.getMatches().getRows().size();
    	
    	mostPositionInfo.setTankerUseRate(0);
    	mostPositionInfo.setRangeDealerUseRate(0);
    	mostPositionInfo.setSupporterUseRate(0);
    	mostPositionInfo.setMeleeDealerUseRate(0);
		ioGameRecords.setMostPositionInfos(mostPositionInfo);
		
    	if (mostPositionPlayCount != 0) {
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
		} 
        
        
    	//공식전, 일반전 데이터
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
        	String gameTypeIdRow0 = cyPlayerInfo.getRecords().get(0).getGameTypeId();	//Rows의 0번째를 가져옴
        	
        	if (gameTypeIdRow0.equals("rating")) {
        		CyphersRecords cyPlayerRatingRecord = cyPlayerInfo.getRecords().get(0);
        		ioGameRecords.setRatingGameTier(cyPlayerInfo.getTierName());
                ioGameRecords.setRatingWinCount(cyPlayerRatingRecord.getWinCount());
                ioGameRecords.setRatingLoseCount(cyPlayerRatingRecord.getLoseCount());
                ioGameRecords.setRatingStopCount(cyPlayerRatingRecord.getStopCount());
                ioGameRecords.setRatingWinRate(100 * cyPlayerRatingRecord.getWinCount() / 
                							(cyPlayerRatingRecord.getLoseCount() + cyPlayerRatingRecord.getWinCount()));
			}
    		if (gameTypeIdRow0.equals("normal") || cyPlayerInfo.getRecords().size() == 2) {
        		Integer recordsLength = Math.max(0, cyPlayerInfo.getRecords().size()-1);
    			CyphersRecords cyPlayerNormalRecord = cyPlayerInfo.getRecords().get(recordsLength);
                ioGameRecords.setNormalWinCount(cyPlayerNormalRecord.getWinCount());        
                ioGameRecords.setNormalLoseCount(cyPlayerNormalRecord.getLoseCount());        
                ioGameRecords.setNormalStopCount(cyPlayerNormalRecord.getStopCount());        
                ioGameRecords.setNormalWinRate(100 * cyPlayerNormalRecord.getWinCount() / 
                						(cyPlayerNormalRecord.getLoseCount() + cyPlayerNormalRecord.getWinCount()));
			}
		}
        
        
        //승, 패수 데이터(그래프)
        //리팩토링 필요
        LocalDateTime today = now;
        LocalDateTime oneWeekAgo = today.minusWeeks(1);
        String winAndLoseStartDate = oneWeekAgo.format(apiSearchFormatter);
        String winAndLoseEndDate = today.format(apiSearchFormatter);
        CyphersMatchingHistory weeklyCyMatchingHistory = cyApiService.searchMatchingHistory(myPlayerId, CyphersGameType.RATING, winAndLoseStartDate, winAndLoseEndDate, WIN_LOSE_COUNT_LIMIT);	
        
        List<IoSearchDetailWinAndLoseCountHistoryInfo> winAndLoseCountHistoryInfos = new ArrayList<>();
        
        Map<Integer, Pair<Integer, Integer>> weeklyCyMatchingHistoryMap = new HashMap<>();	//pair 앞은 승수, 뒤는 패수
        
        Integer week = 6;
        Pair<LocalDate, Integer> weeklyMatchedDate = Pair.of(weeklyCyMatchingHistory.getMatches().getRows().get(0).getDate(), week);
        for (int i = 0; i < weeklyCyMatchingHistory.getMatches().getRows().size(); i++) {
        	CyphersMatchedInfo cyMatchedInfo = weeklyCyMatchingHistory.getMatches().getRows().get(i);
        	
        	if (!cyMatchedInfo.getDate().isEqual(weeklyMatchedDate.getFirst())) {
        		IoSearchDetailWinAndLoseCountHistoryInfo winAndLoseHisory = new IoSearchDetailWinAndLoseCountHistoryInfo();
				winAndLoseHisory.setHistoryDate(weeklyMatchedDate.getSecond());
				winAndLoseHisory.setWinCount(weeklyCyMatchingHistoryMap.get(weeklyMatchedDate.getSecond()).getFirst());
	        	winAndLoseHisory.setLoseCount(weeklyCyMatchingHistoryMap.get(weeklyMatchedDate.getSecond()).getSecond());
	        	winAndLoseCountHistoryInfos.add(winAndLoseHisory);
				weeklyMatchedDate = Pair.of(weeklyMatchedDate.getFirst().minusDays(1), weeklyMatchedDate.getSecond() - 1);
			} 
        	Pair<Integer, Integer> winAndLoseCount = weeklyCyMatchingHistoryMap.getOrDefault(weeklyMatchedDate.getSecond(), Pair.of(0, 0));
        	
    		if (cyMatchedInfo.getPlayInfo().getResult().equals("win")) {
    			weeklyCyMatchingHistoryMap.put(weeklyMatchedDate.getSecond(), Pair.of(winAndLoseCount.getFirst() + 1, winAndLoseCount.getSecond()));
			} else {
				weeklyCyMatchingHistoryMap.put(weeklyMatchedDate.getSecond(), Pair.of(winAndLoseCount.getFirst(), winAndLoseCount.getSecond() + 1));
			}
		}
        
        ioGameRecords.setWinAndLoseCountHistoryInfos(winAndLoseCountHistoryInfos);
        
        
        //최근 2주간 게임 데이터
        LocalDateTime twoWeekAgo = now.minus(2, ChronoUnit.WEEKS); // 게임기록 서치 2주 설정
        String recentStartDate = twoWeekAgo.format(apiSearchFormatter);
        String recentEndDate = now.format(apiSearchFormatter);
        CyphersMatchingHistory recentCyMatchingHistoryNormal = cyApiService.searchMatchingHistory(myPlayerId, CyphersGameType.NORMAL, recentStartDate, recentEndDate, RECENT_MATCHING_HISTORY_LIMIT);	        // limit = 100
        
        Integer recentlyPlayCount = recentCyMatchingHistoryNormal.getMatches().getRows().size();
        ioGameRecords.setRecentlyPlayCount(0);
		ioGameRecords.setRecentlyWinRate(0);
		ioGameRecords.setRecentlyKda(0.0f);
		ioGameRecords.setRecentlyAverageSurvivalRate(0);
		ioGameRecords.setRecentlyPlayCyphersInfos(Collections.emptyList());
		
        if (recentlyPlayCount != 0) {
        	
        	ioGameRecords.setRecentlyPlayCount(recentlyPlayCount);
            
            Integer recentlyWinCount = 0;
            for (int i = 0; i < recentlyPlayCount; i++) {
            	String result = recentCyMatchingHistoryNormal.getMatches().getRows().get(i).getPlayInfo().getResult();
            	if (result.equals("win")) {
            		recentlyWinCount++;
    			}
    		}
            ioGameRecords.setRecentlyWinRate(100 * recentlyWinCount / recentlyPlayCount);
            
            Float totalKillCount = 0.0f;
            Float totalDeathCount = 0.0f;
            Float totalAssistCount = 0.0f;
            for (int i = 0; i < recentCyMatchingHistoryNormal.getMatches().getRows().size(); i++) {
            	CyphersPlayInfo cyPlayInfo = recentCyMatchingHistoryNormal.getMatches().getRows().get(i).getPlayInfo();
    			totalKillCount += cyPlayInfo.getKillCount();
    			totalDeathCount += cyPlayInfo.getDeathCount();
    			totalAssistCount += cyPlayInfo.getAssistCount();
    		}
            ioGameRecords.setRecentlyKda(Math.round((totalKillCount + totalAssistCount) / totalDeathCount * 100) / 100.0f);
            
            Integer avgSurvivalRate = 0;
            for (int i = 0; i < recentlyPlayCount; i++) {
            	Integer playTime = recentCyMatchingHistoryNormal.getMatches().getRows().get(i).getPlayInfo().getPlayTime();
            	Integer responseTime = recentCyMatchingHistoryNormal.getMatches().getRows().get(i).getPlayInfo().getResponseTime();
            	
            	avgSurvivalRate += 100 * (playTime - responseTime) / playTime;
    		}
            ioGameRecords.setRecentlyAverageSurvivalRate(avgSurvivalRate / recentlyPlayCount);
            
            
            //top3 캐릭터 데이터
            Map<String, Pair<Integer, Integer>> recentCharacterIdMap = new HashMap<>();	//Pair의 첫번째는 전체 플레이 횟수, 두번째는 이긴 횟수
            for (int i = 0; i < recentCyMatchingHistoryNormal.getMatches().getRows().size(); i++) {
            	CyphersPlayInfo cyPlayInfo = recentCyMatchingHistoryNormal.getMatches().getRows().get(i).getPlayInfo();
            	String characterId = cyPlayInfo.getCharacterId();
            	Pair<Integer, Integer> playAndWinCount = recentCharacterIdMap.getOrDefault(characterId, Pair.of(0, 0));
            	
            	recentCharacterIdMap.put(characterId, 	Pair.of(playAndWinCount.getFirst() + 1, 
            													playAndWinCount.getSecond() + (cyPlayInfo.getResult().equals("win") ? 1 : 0)));
    		}
            
            List<String> recentIdList = new ArrayList<>(recentCharacterIdMap.keySet());
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
		}
        
        
        //게임기록
        List<IoSearchDetailGameRecord> gameRecords = new ArrayList<>();
        CyphersMatchingHistory recordCyMatchingHistory = cyApiService.searchMatchingHistory(myPlayerId, CyphersGameType.RATING, null, null, GAME_RECORD_LIMIT); 
        ioGameRecords.setGameRecords(Collections.emptyList());
        
        if (recordCyMatchingHistory.getMatches().getRows().size() != 0) {
        	
        	for (int i = 0; i < recordCyMatchingHistory.getMatches().getRows().size(); i++) {
                IoSearchDetailGameRecord gameRecord = new IoSearchDetailGameRecord();
                String matchId = recordCyMatchingHistory.getMatches().getRows().get(i).getMatchId();
                CyphersMatchingDetails matchingDetail = cyApiService.searchMatchingDetail(matchId);
                Integer totalKillCount = 0;
                for (CyphersPlayersInGame cyPlayersInGame : matchingDetail.getPlayers()) {
                	totalKillCount += cyPlayersInGame.getPlayInfo().getKillCount();
				}
                
                List<String> playerNicknames = new ArrayList<>();
                
                for (int j = 0; j < matchingDetail.getPlayers().size(); j++) {
    				if (matchingDetail.getPlayers().get(j).getPlayerId().equals(myPlayerId)) {
    					CyphersPlayersInGame player = matchingDetail.getPlayers().get(j);
    					CyphersPlayInfo playInfo = matchingDetail.getPlayers().get(j).getPlayInfo();
    					
    					gameRecord.setPlayCharacterId(player.getPlayerId());
    					gameRecord.setPostionName(player.getPosition().getName());
    					List<String> attributeIds = new ArrayList<>();
    					for (int k = 0; k < player.getPosition().getAttribute().size(); k++) {
    						attributeIds.add(player.getPosition().getAttribute().get(k).getId());
    					}
    					gameRecord.setKillParticipation(0);
    					if (totalKillCount <= 0) {
    						gameRecord.setKillParticipation(100 * (playInfo.getKillCount() + playInfo.getAssistCount()) / totalKillCount);
						} 
    					
    					gameRecord.setAttributeIds(attributeIds);
    					gameRecord.setKillCount(playInfo.getKillCount());
    					gameRecord.setDeathCount(playInfo.getDeathCount());
    					gameRecord.setAssistCount(playInfo.getAssistCount());
    					
    					
    					Float killCount = playInfo.getKillCount().floatValue();
    					Float deathCount = playInfo.getDeathCount().floatValue();
    					Float assistCount = playInfo.getAssistCount().floatValue();
    					gameRecord.setKda(Math.round((killCount + assistCount) / deathCount * 100) / 100.0f);
    					gameRecord.setCsCount(gameRecord.getCsCount());
    					
    					List<String> itemIds = new ArrayList<>();
    					for (int k = 0; k < player.getItems().size(); k++) {
    						itemIds.add(player.getItems().get(k).getItemId());
    					}
    					gameRecord.setItemIds(itemIds);
    					
    					gameRecord.setHealAmount(playInfo.getHealAmount());
    					gameRecord.setAttackPoint(playInfo.getAttackPoint());
    					gameRecord.setDamagePoint(playInfo.getDamagePoint());
    					gameRecord.setGetCoin(playInfo.getGetCoin());
    					gameRecord.setBattlePoint(playInfo.getBattlePoint());
    					gameRecord.setSightPoint(playInfo.getSightPoint());
    				}
    				playerNicknames.add(matchingDetail.getPlayers().get(j).getNickname());
    			}
                gameRecord.setGameType(CyphersGameType.RATING);
                gameRecord.setPlayerNicknames(playerNicknames);
                gameRecords.add(gameRecord);
    		}
            ioGameRecords.setGameRecords(gameRecords);
            
		} 
        
        return ioGameRecords;
    }

}
