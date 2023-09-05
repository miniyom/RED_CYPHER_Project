package com.cyphers.game.RecordSearch.service;

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

import com.cyphers.game.RecordSearch.controller.search.model.IoSearchDetailRecentlyPlayCyphersInfo;
import com.cyphers.game.RecordSearch.controller.search.model.IoSearchDetailResponse;
import com.cyphers.game.RecordSearch.cyphers.CyphersApiService;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersCharacterSearch;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersMatchingHistory;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersPlayInfo;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersPlayerInfo;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersPlayerResponse;
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
    	String playerId = cyApiService.searchPlayers(nickname, CyphersPlayerWordType.MATCH, null)
				.getRows().get(0).getPlayerId();
    	
    	
    	//공식전, 일반전 데이터
        CyphersPlayerInfo cyPlayerInfo = cyApiService.searchPlayerInfo(playerId); 
        
        if (!(cyPlayerInfo.getRecords().size() == 0)) {
        	String gameTypeIdRow0 = cyPlayerInfo.getRecords().get(0).getGameTypeId();
        	
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
        
        
        //최근 2주간 게임 데이터
        Integer limit = 10;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime twoWeekAgo = now.minus(2, ChronoUnit.WEEKS); // 게임기록 서치 2주 설정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String startDate = twoWeekAgo.format(formatter).toString();
        String endDate = now.format(formatter).toString();
        
        CyphersMatchingHistory cyMatchingHistory = cyApiService.searchMatchingHistory(playerId, CyphersGameType.NORMAL, startDate, endDate, limit);
        
        ioGameRecords.setRecentlyPlayCount(cyMatchingHistory.getMatches().getRows().size());
        
        Integer recentlyWinCount = 0;
        for (int i = 0; i < cyMatchingHistory.getMatches().getRows().size(); i++) {
        	String result = cyMatchingHistory.getMatches().getRows().get(i).getPlayInfo().getResult();
        	if (result.equals("win")) {
        		recentlyWinCount++;
			}
		}
        ioGameRecords.setRecentlyWinRate(100 * recentlyWinCount / ioGameRecords.getRecentlyPlayCount());
        
        Float totalKillCount = 0.0f;
        Float totalDeathCount = 0.0f;
        Float totalAssistCount = 0.0f;
        for (int i = 0; i < cyMatchingHistory.getMatches().getRows().size(); i++) {
        	CyphersPlayInfo cyPlayInfo = cyMatchingHistory.getMatches().getRows().get(i).getPlayInfo();
			totalKillCount += cyPlayInfo.getKillCount();
			totalDeathCount += cyPlayInfo.getDeathCount();
			totalAssistCount += cyPlayInfo.getAssistCount();
		}
        ioGameRecords.setRecentlyKda((totalKillCount + totalAssistCount) / totalDeathCount);
        
        Integer avgSurvivalRate = 0;
        for (int i = 0; i < cyMatchingHistory.getMatches().getRows().size(); i++) {
        	Integer playTime = cyMatchingHistory.getMatches().getRows().get(i).getPlayInfo().getPlayTime();
        	Integer responseTime = cyMatchingHistory.getMatches().getRows().get(i).getPlayInfo().getResponseTime();
        	
        	avgSurvivalRate += 100 * (playTime - responseTime) / playTime;
		}
        ioGameRecords.setRecentlyAverageSurvivalRate(avgSurvivalRate / cyMatchingHistory.getMatches().getRows().size());
        
        
        //최근 2주간 top3 캐릭터 데이터
        //Pair의 첫번째는 전체 플레이 횟수, 두번째는 이긴 횟수
        Map<String, Pair<Integer, Integer>> characterIdMap = new HashMap<>();
        for (int i = 0; i < cyMatchingHistory.getMatches().getRows().size(); i++) {
        	CyphersPlayInfo cyPlayInfo = cyMatchingHistory.getMatches().getRows().get(i).getPlayInfo();
        	String characterId = cyPlayInfo.getCharacterId();
        	Pair<Integer, Integer> playAndWinCount = characterIdMap.getOrDefault(characterId, Pair.of(0, 0));
        	
        	characterIdMap.put(characterId, Pair.of(playAndWinCount.getFirst() + 1, 
        											playAndWinCount.getSecond() + (cyPlayInfo.getResult().equals("win") ? 1 : 0)));
		}
        
        //Top3 캐릭터 선정(플레이 수 기준 내림차순 정렬)
        List<String> idList = new ArrayList<>();
        for (String string : characterIdMap.keySet()) {
			idList.add(string);
		}
        Collections.sort(idList, Comparator.comparing(id -> id, (a, b) -> characterIdMap.get(b).getFirst() - characterIdMap.get(a).getFirst()));;
        
        //캐릭터 데이터 넣기
        List<IoSearchDetailRecentlyPlayCyphersInfo> recentCypherRows = new ArrayList<>(3);	//가져오고 싶은 캐릭터 개수 설정
        CyphersCharacterSearch cyCharacter = cyApiService.searchCharacter();
        for (int i = 0; i < recentCypherRows.size(); i++) {
			IoSearchDetailRecentlyPlayCyphersInfo recentCypherInfo = new IoSearchDetailRecentlyPlayCyphersInfo();
			recentCypherInfo.setCharacterId(idList.get(i));
			for (int j = 0; j < cyCharacter.getRows().size(); j++) {
				if (cyCharacter.getRows().get(j).getCharacterId().equals(idList.get(i))) {
					recentCypherInfo.setCharacterName(cyCharacter.getRows().get(j).getCharacterName());
					break;
				}
			}
			recentCypherInfo.setWinCount(characterIdMap.get(idList.get(i)).getSecond());
			recentCypherInfo.setLoseCount(characterIdMap.get(idList.get(i)).getFirst()
										- characterIdMap.get(idList.get(i)).getSecond());
			
			Float avgKillCount = 0.0f;
			Float avgDeathCount = 0.0f;
			Float avgAssistCount = 0.0f;
			Float denominator = 0.0f;
			for (int j = 0; j < cyMatchingHistory.getMatches().getRows().size(); j++) {
				CyphersPlayInfo cyPlayInfo = cyMatchingHistory.getMatches().getRows().get(j).getPlayInfo();
				if (cyPlayInfo.getCharacterId().equals(idList.get(i))) {
					avgKillCount += cyPlayInfo.getKillCount();
					avgDeathCount += cyPlayInfo.getDeathCount();
					avgAssistCount += cyPlayInfo.getAssistCount();
					denominator++;
				}
			}
			recentCypherInfo.setKillCount(avgKillCount / denominator);
			recentCypherInfo.setDeathCount(avgDeathCount / denominator);
			recentCypherInfo.setAssistCount(avgAssistCount / denominator);
			
			recentCypherRows.add(recentCypherInfo);
		}
        
        ioGameRecords.setRecentlyPlayCyphersInfos(recentCypherRows);
        
        
        return ioGameRecords;
    }

}
