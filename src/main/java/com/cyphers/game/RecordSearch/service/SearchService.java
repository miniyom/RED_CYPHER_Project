package com.cyphers.game.RecordSearch.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cyphers.game.RecordSearch.controller.search.model.IoSearchDetailRecentlyPlayCyphersInfo;
import com.cyphers.game.RecordSearch.controller.search.model.IoSearchDetailResponse;
import com.cyphers.game.RecordSearch.cyphers.CyphersApiService;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersMatchingHistory;
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
        
        
        //최근 게임 데이터
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
			totalKillCount += cyMatchingHistory.getMatches().getRows().get(i).getPlayInfo().getKillCount();
			totalDeathCount += cyMatchingHistory.getMatches().getRows().get(i).getPlayInfo().getDeathCount();
			totalAssistCount += cyMatchingHistory.getMatches().getRows().get(i).getPlayInfo().getAssistCount();
		}
        ioGameRecords.setRecentlyKda((totalKillCount + totalAssistCount) / totalDeathCount);
        
        List<IoSearchDetailRecentlyPlayCyphersInfo> recentCypherInfo = new ArrayList<>();
        
        Map<String, Integer> characterIdMap = new HashMap<>();
        Integer firstCypher = 0;
        Integer secondCypher = 0;
        Integer thirdCypher = 0;
        for (int i = 0; i < cyMatchingHistory.getMatches().getRows().size(); i++) {
        	String characterId = cyMatchingHistory.getMatches().getRows().get(i).getPlayInfo().getCharacterId();
        	characterIdMap.put(characterId, characterIdMap.getOrDefault(characterId, 0) + 1);
		}
        Map<String, Integer> sortedCharacterIdMap = new LinkedHashMap<>();
        characterIdMap.entrySet()
        .stream()
        .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
        .forEachOrdered(entry -> sortedCharacterIdMap.put(entry.getKey(), entry.getValue()));
        
        
        return ioGameRecords;
    }

}
