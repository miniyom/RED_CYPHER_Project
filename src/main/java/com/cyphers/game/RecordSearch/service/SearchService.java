package com.cyphers.game.RecordSearch.service;

import java.util.ArrayList;
import java.util.List;

import com.cyphers.game.RecordSearch.controller.search.model.IoSearchDetailResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cyphers.game.RecordSearch.cyphers.CyphersApiService;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersPlayerResponse;
import com.cyphers.game.RecordSearch.cyphers.model.enumuration.CyphersPlayerWordType;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SearchService {

    @Autowired
    ObjectMapper objectmapper;
    @Autowired
    CyphersApiService cyApiService;

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

    public List<IoSearchDetailResponse> getDetailSearch(String nickname) throws Exception {
        // TODO 작업 순서
        // 1. 데이터 가져와서 IoSearchDetailResponse 스펙에 맞게 데이터 넣기
        // 2. Vue에서 데이터 가져와서 올바르게 넣기
        return null;
    }

}
