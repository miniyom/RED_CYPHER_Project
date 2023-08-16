package com.cyphers.game.RecordSearch.controller.search;

import com.cyphers.game.RecordSearch.service.SearchService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/search")
@AllArgsConstructor
@Slf4j
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/{nickname}")
    public List<String> search(@PathVariable("nickname") String nickname) {
        return searchService.getNicknameList(nickname).stream()
                .sorted()       // 일반 String 정렬 순으로 검색 개수가 표시되도록 한다.
                .limit(5).collect(Collectors.toList());
    }
}
