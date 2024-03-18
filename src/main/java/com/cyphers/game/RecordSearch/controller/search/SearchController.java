package com.cyphers.game.RecordSearch.controller.search;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cyphers.game.RecordSearch.model.search.GameRecordResponse;
import com.cyphers.game.RecordSearch.model.search.IoSearchDetailResponse;
import com.cyphers.game.RecordSearch.model.search.SearchDetailResponse;
import com.cyphers.game.RecordSearch.model.search.entity.CrsDetailSearch;
import com.cyphers.game.RecordSearch.openapi.model.CyphersMatches;
import com.cyphers.game.RecordSearch.openapi.model.CyphersPlayerInfo;
import com.cyphers.game.RecordSearch.openapi.model.CyphersPlayerResponse;
import com.cyphers.game.RecordSearch.openapi.model.enumuration.CyphersGameType;
import com.cyphers.game.RecordSearch.openapi.model.enumuration.CyphersPlayerWordType;
import com.cyphers.game.RecordSearch.openapi.service.CyphersApiService;
import com.cyphers.game.RecordSearch.service.search.CrsSearchService;
import com.cyphers.game.RecordSearch.service.search.SearchService;
import com.cyphers.game.RecordSearch.service.search.repository.CrsDetailSearchRepository;
import com.cyphers.game.RecordSearch.utils.ApiDate;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/search")
@AllArgsConstructor
@Slf4j
public class SearchController {

    @Autowired
    private SearchService searchService;
    @Autowired
    private CrsSearchService crsSearchService;
    @Autowired
    private CrsDetailSearchRepository crsSearchRepository;
    @Autowired
    private CyphersApiService cyApiService;

    @GetMapping("/auto-complete/{nickname}")
    public List<String> searchAutoComplete(@PathVariable("nickname") String nickname) throws Exception {
        return searchService.getNicknameList(nickname).stream()
                .sorted()       // 일반 String 정렬 순으로 검색 개수가 표시되도록 한다.
                .limit(5).collect(Collectors.toList());
    }
    
    @GetMapping("/{nickname}")
    public SearchDetailResponse getSearchDetail(@PathVariable("nickname") String nickname) throws Exception {
    	SearchDetailResponse res = crsSearchService.getDetailSearch(nickname);
        return res;
    }
    
    @GetMapping("/player/search/{nickname}")
    public CyphersPlayerInfo getPlayerInfo(@PathVariable("nickname") String nickname) throws Exception {
    	CyphersPlayerResponse cyPlayerResponse = cyApiService.searchPlayers(nickname, CyphersPlayerWordType.MATCH, null);
    	CyphersPlayerInfo cyPlayerInfo = cyApiService.searchPlayerInfo(cyPlayerResponse.getRows().get(0).getPlayerId());
    	return cyPlayerInfo;
    }
    
    @GetMapping("/records/{gameType}/{playerId}")
    public GameRecordResponse getGameRecords(@PathVariable("gameType") CyphersGameType gameType,
    										 @PathVariable("playerId") String playerId) throws Exception {
    	CyphersMatches matches = searchService.getGameRecordsFirst(playerId, gameType, ApiDate.NINETY_DAYS_AGO, ApiDate.NOW);
    	GameRecordResponse res = searchService.getGameRecords(matches, playerId);	
    	return res;
    }
    
    @GetMapping("/records/next/{playerId}/{next}")
    public GameRecordResponse getGameRecordsNext(@PathVariable("playerId") String playerId, 
    											 @PathVariable("next") String next) throws Exception {
    	CyphersMatches matches = searchService.getGameRecordsNext(playerId, next);
    	GameRecordResponse res = searchService.getGameRecords(matches, playerId);

    	return res;
    }
    
    @GetMapping("/renewal/{nickname}")
    public SearchDetailResponse renewalDetail(@PathVariable("nickname") String nickname) throws Exception {
    	
		IoSearchDetailResponse detailSearch = searchService.getDetailSearch(nickname);
    	crsSearchService.upsert(detailSearch);
    	SearchDetailResponse searchDetailDTO = crsSearchService.getDetailSearch(nickname);
    	
        return searchDetailDTO;
    }
    
    @GetMapping("/find/id/{playerId}")
    public CrsDetailSearch findCyphersUserInfo(@PathVariable("playerId") String playerId) throws Exception {
    	CrsDetailSearch res = crsSearchRepository.findByPlayerId(playerId).get();
        return null;
    }
}
