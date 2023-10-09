package com.cyphers.game.RecordSearch.controller.search;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cyphers.game.RecordSearch.controller.search.model.GameRecordsVO;
import com.cyphers.game.RecordSearch.controller.search.model.IoSearchDetailResponse;
import com.cyphers.game.RecordSearch.controller.search.model.SearchDetailResponseDTO;
import com.cyphers.game.RecordSearch.model.CrsDetailSearch;
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

//    @GetMapping("/{nickname}")
//    public IoSearchDetailResponse searchDetail(@PathVariable("nickname") String nickname) throws Exception {
//    	IoSearchDetailResponse res = searchService.getDetailSearch(nickname);
//    	crsSearchService.input(res);
//        return res;
//    }
    
    @GetMapping("/{nickname}")
    public SearchDetailResponseDTO searchDetail(@PathVariable("nickname") String nickname) throws Exception {
    	IoSearchDetailResponse detailSearch = searchService.getDetailSearch(nickname);
    	crsSearchService.input(detailSearch);
    	GameRecordsVO gameRecordsVO = searchService.getGameRecords(nickname);
    	
    	SearchDetailResponseDTO res = new SearchDetailResponseDTO();
    	res.setDetailResponse(detailSearch);
    	res.setGameRecords(gameRecordsVO.getGameRecords());
        return res;
    }
    
    @GetMapping("/find/id/{playerId}")
    public CrsDetailSearch findCyphersUserInfo(@PathVariable("playerId") String playerId) throws Exception {
    	CrsDetailSearch res = crsSearchRepository.findByPlayerId(playerId).get();
        return null;
    }
}
