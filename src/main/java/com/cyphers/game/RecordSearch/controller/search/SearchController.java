package com.cyphers.game.RecordSearch.controller.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cyphers.game.RecordSearch.controller.search.model.GameRecordResponse;
import com.cyphers.game.RecordSearch.controller.search.model.IoSearchDetailResponse;
import com.cyphers.game.RecordSearch.controller.search.model.MostCypherInfoResponse;
import com.cyphers.game.RecordSearch.controller.search.model.MostPositionInfoResponse;
import com.cyphers.game.RecordSearch.controller.search.model.RecentlyPlayCypherInfoResponse;
import com.cyphers.game.RecordSearch.controller.search.model.SearchDetailResponse;
import com.cyphers.game.RecordSearch.controller.search.model.WinAndLoseCountHistoryResponse;
import com.cyphers.game.RecordSearch.model.CrsDetailSearch;
import com.cyphers.game.RecordSearch.model.CrsMostCypherInfos;
import com.cyphers.game.RecordSearch.model.CrsMostPositionInfos;
import com.cyphers.game.RecordSearch.model.CrsRecentlyPlayCypherInfos;
import com.cyphers.game.RecordSearch.model.CrsWinAndLoseCountHistory;
import com.cyphers.game.RecordSearch.service.search.CrsSearchService;
import com.cyphers.game.RecordSearch.service.search.SearchService;
import com.cyphers.game.RecordSearch.service.search.repository.CrsDetailSearchRepository;

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

    @GetMapping("/auto-complete/{nickname}")
    public List<String> searchAutoComplete(@PathVariable("nickname") String nickname) throws Exception {
        return searchService.getNicknameList(nickname).stream()
                .sorted()       // 일반 String 정렬 순으로 검색 개수가 표시되도록 한다.
                .limit(5).collect(Collectors.toList());
    }
    
//    api로 직접 가져올 때
//    @GetMapping("/{nickname}")
//    public IoSearchDetailResponse searchDetail(@PathVariable("nickname") String nickname) throws Exception {
//        return searchService.getDetailSearch(nickname);
//    }
    
    @GetMapping("/{nickname}")
    public SearchDetailResponse getSearchDetail(@PathVariable("nickname") String nickname) throws Exception {
    	Optional<CrsDetailSearch> cds = crsSearchService.getDetailSearch(nickname);
    	GameRecordResponse gameRecordResponse = searchService.getGameRecords(nickname);
    	
    	SearchDetailResponse res = new SearchDetailResponse();
    	res.setGameRecords(gameRecordResponse.getGameRecords());
    	if (cds.isEmpty()) {
			return res;
		}
    	CrsDetailSearch crsDetailSearch = cds.get();
    	res.setPlayerId(crsDetailSearch.getPlayerId());
    	res.setProfileCharacterId(crsDetailSearch.getProfileCharacterId());
    	res.setNickname(crsDetailSearch.getNickname());
    	res.setRecentlyUpdatedDate(crsDetailSearch.getRecentlyUpdatedDate());
    	List<MostCypherInfoResponse> mostCypherResponse = new ArrayList<>();
    	for (CrsMostCypherInfos crsMostCypher : crsDetailSearch.getMostCypherInfos()) {
    		MostCypherInfoResponse mres = new MostCypherInfoResponse();
    		mres.setCharacterId(crsMostCypher.getCharacterId());
    		mres.setCharacterName(crsMostCypher.getCharacterName());
    		mres.setWinRate(crsMostCypher.getWinRate());
    		mres.setPlayCount(crsMostCypher.getPlayCount());
    		mres.setKda(crsMostCypher.getKda());
    		mostCypherResponse.add(mres);
		}
    	res.setMostCypherInfos(mostCypherResponse);
    	MostPositionInfoResponse mostPositionResponse = new MostPositionInfoResponse();
    	CrsMostPositionInfos crsMostPosition = crsDetailSearch.getMostPositionInfos();
    	mostPositionResponse.setTankerUseRate(crsMostPosition.getTankerUseRate());
    	mostPositionResponse.setRangeDealerUseRate(crsMostPosition.getRangeDealerUseRate());
    	mostPositionResponse.setSupporterUseRate(crsMostPosition.getSupporterUseRate());
    	mostPositionResponse.setMeleeDealerUseRate(crsMostPosition.getMeleeDealerUseRate());
    	res.setMostPositionInfos(mostPositionResponse);
    	res.setRatingGameTier(crsDetailSearch.getRatingGameTier());
    	res.setRatingWinCount(crsDetailSearch.getRatingWinCount());
    	res.setRatingLoseCount(crsDetailSearch.getRatingLoseCount());
    	res.setRatingStopCount(crsDetailSearch.getRatingStopCount());
    	res.setRatingWinRate(crsDetailSearch.getRatingWinRate());
    	res.setNormalWinCount(crsDetailSearch.getNormalWinCount());
    	res.setNormalLoseCount(crsDetailSearch.getNormalLoseCount());
    	res.setNormalStopCount(crsDetailSearch.getNormalStopCount());
    	res.setNormalWinRate(crsDetailSearch.getNormalWinRate());
    	List<WinAndLoseCountHistoryResponse> outcomeResponse = new ArrayList<>();
    	for (CrsWinAndLoseCountHistory crsOutcome : crsDetailSearch.getWinAndLoseCountHistory()) {
    		WinAndLoseCountHistoryResponse wres = new WinAndLoseCountHistoryResponse();
    		wres.setHistoryDate(crsOutcome.getHistoryDate());
    		wres.setWinCount(crsOutcome.getWinCount());
    		wres.setLoseCount(crsOutcome.getLoseCount());
    		outcomeResponse.add(wres);
		}
    	res.setWinAndLoseCountHistory(outcomeResponse);
    	res.setRecentlyPlayCount(crsDetailSearch.getRecentlyPlayCount());
    	res.setRecentlyWinRate(crsDetailSearch.getRecentlyWinRate());
    	res.setRecentlyKda(crsDetailSearch.getRecentlyKda());
    	res.setRecentlyAverageSurvivalRate(crsDetailSearch.getRecentlyAverageSurvivalRate());
    	List<RecentlyPlayCypherInfoResponse> recentCypherResponse = new ArrayList<>();
    	for (CrsRecentlyPlayCypherInfos crsRecentCypher : crsDetailSearch.getRecentlyPlayCyphersInfos()) {
    		RecentlyPlayCypherInfoResponse rcres = new RecentlyPlayCypherInfoResponse();
    		rcres.setCharacterId(crsRecentCypher.getCharacterId());
    		rcres.setCharacterName(crsRecentCypher.getCharacterName());
    		rcres.setWinCount(crsRecentCypher.getWinCount());
    		rcres.setLoseCount(crsRecentCypher.getLoseCount());
    		rcres.setKillCount(crsRecentCypher.getKillCount());
    		rcres.setDeathCount(crsRecentCypher.getDeathCount());
    		rcres.setAssistCount(crsRecentCypher.getAssistCount());
    		recentCypherResponse.add(rcres);
		}
    	res.setRecentlyPlayCyphersInfos(recentCypherResponse);
    	
        return res;
    }
    
    @GetMapping("/renewal/{nickname}")
    public SearchDetailResponse renewalDetail(@PathVariable("nickname") String nickname) throws Exception {
    	
		IoSearchDetailResponse detailSearch = searchService.getDetailSearch(nickname);
    	crsSearchService.input(detailSearch);
    	Optional<CrsDetailSearch> cds = crsSearchService.getDetailSearch(nickname);
    	SearchDetailResponse res = new SearchDetailResponse();
    	if (cds.isEmpty()) {
			return res;
		}
    	CrsDetailSearch crsDetailSearch = cds.get();
    	res.setPlayerId(crsDetailSearch.getPlayerId());
    	res.setProfileCharacterId(crsDetailSearch.getProfileCharacterId());
    	res.setNickname(crsDetailSearch.getNickname());
    	res.setRecentlyUpdatedDate(crsDetailSearch.getRecentlyUpdatedDate());
    	List<MostCypherInfoResponse> mostCypherResponse = new ArrayList<>();
    	for (CrsMostCypherInfos crsMostCypher : crsDetailSearch.getMostCypherInfos()) {
    		MostCypherInfoResponse mres = new MostCypherInfoResponse();
    		mres.setCharacterId(crsMostCypher.getCharacterId());
    		mres.setCharacterName(crsMostCypher.getCharacterName());
    		mres.setWinRate(crsMostCypher.getWinRate());
    		mres.setPlayCount(crsMostCypher.getPlayCount());
    		mres.setKda(crsMostCypher.getKda());
    		mostCypherResponse.add(mres);
		}
    	res.setMostCypherInfos(mostCypherResponse);
    	MostPositionInfoResponse mostPositionResponse = new MostPositionInfoResponse();
    	CrsMostPositionInfos crsMostPosition = crsDetailSearch.getMostPositionInfos();
    	mostPositionResponse.setTankerUseRate(crsMostPosition.getTankerUseRate());
    	mostPositionResponse.setRangeDealerUseRate(crsMostPosition.getRangeDealerUseRate());
    	mostPositionResponse.setSupporterUseRate(crsMostPosition.getSupporterUseRate());
    	mostPositionResponse.setMeleeDealerUseRate(crsMostPosition.getMeleeDealerUseRate());
    	res.setMostPositionInfos(mostPositionResponse);
    	res.setRatingGameTier(crsDetailSearch.getRatingGameTier());
    	res.setRatingWinCount(crsDetailSearch.getRatingWinCount());
    	res.setRatingLoseCount(crsDetailSearch.getRatingLoseCount());
    	res.setRatingStopCount(crsDetailSearch.getRatingStopCount());
    	res.setRatingWinRate(crsDetailSearch.getRatingWinRate());
    	res.setNormalWinCount(crsDetailSearch.getNormalWinCount());
    	res.setNormalLoseCount(crsDetailSearch.getNormalLoseCount());
    	res.setNormalStopCount(crsDetailSearch.getNormalStopCount());
    	res.setNormalWinRate(crsDetailSearch.getNormalWinRate());
    	List<WinAndLoseCountHistoryResponse> outcomeResponse = new ArrayList<>();
    	for (CrsWinAndLoseCountHistory crsOutcome : crsDetailSearch.getWinAndLoseCountHistory()) {
    		WinAndLoseCountHistoryResponse wres = new WinAndLoseCountHistoryResponse();
    		wres.setHistoryDate(crsOutcome.getHistoryDate());
    		wres.setWinCount(crsOutcome.getWinCount());
    		wres.setLoseCount(crsOutcome.getLoseCount());
    		outcomeResponse.add(wres);
		}
    	res.setWinAndLoseCountHistory(outcomeResponse);
    	res.setRecentlyPlayCount(crsDetailSearch.getRecentlyPlayCount());
    	res.setRecentlyWinRate(crsDetailSearch.getRecentlyWinRate());
    	res.setRecentlyKda(crsDetailSearch.getRecentlyKda());
    	res.setRecentlyAverageSurvivalRate(crsDetailSearch.getRecentlyAverageSurvivalRate());
    	List<RecentlyPlayCypherInfoResponse> recentCypherResponse = new ArrayList<>();
    	for (CrsRecentlyPlayCypherInfos crsRecentCypher : crsDetailSearch.getRecentlyPlayCyphersInfos()) {
    		RecentlyPlayCypherInfoResponse rcres = new RecentlyPlayCypherInfoResponse();
    		rcres.setCharacterId(crsRecentCypher.getCharacterId());
    		rcres.setCharacterName(crsRecentCypher.getCharacterName());
    		rcres.setWinCount(crsRecentCypher.getWinCount());
    		rcres.setLoseCount(crsRecentCypher.getLoseCount());
    		rcres.setKillCount(crsRecentCypher.getKillCount());
    		rcres.setDeathCount(crsRecentCypher.getDeathCount());
    		rcres.setAssistCount(crsRecentCypher.getAssistCount());
    		recentCypherResponse.add(rcres);
		}
    	res.setRecentlyPlayCyphersInfos(recentCypherResponse);
    	
        return res;
    }
    
    @GetMapping("/find/id/{playerId}")
    public CrsDetailSearch findCyphersUserInfo(@PathVariable("playerId") String playerId) throws Exception {
    	CrsDetailSearch res = crsSearchRepository.findByPlayerId(playerId).get();
        return null;
    }
}
