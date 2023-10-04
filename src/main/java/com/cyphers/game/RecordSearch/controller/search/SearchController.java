package com.cyphers.game.RecordSearch.controller.search;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cyphers.game.RecordSearch.controller.search.model.IoSearchDetailResponse;
import com.cyphers.game.RecordSearch.service.search.CrsDetailSearchService;
import com.cyphers.game.RecordSearch.service.search.SearchService;

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
    private CrsDetailSearchService crsSearchService;

    @GetMapping("/auto-complete/{nickname}")
    public List<String> searchAutoComplete(@PathVariable("nickname") String nickname) throws Exception {
        return searchService.getNicknameList(nickname).stream()
                .sorted()       // 일반 String 정렬 순으로 검색 개수가 표시되도록 한다.
                .limit(5).collect(Collectors.toList());
    }

    @GetMapping("/{nickname}")
    public IoSearchDetailResponse searchDetail(@PathVariable("nickname") String nickname) throws Exception {
    	IoSearchDetailResponse res = searchService.getDetailSearch(nickname);
    	crsSearchService.input(res);
        return res;
    }
}
