package com.cyphers.game.RecordSearch.service;

import java.util.ArrayList;
import java.util.List;

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
	
    public List<String> getNicknameList(String word) throws Exception {
    	List<String> nicknameList = new ArrayList<>();
    	Integer limit = 10;
    	CyphersPlayerResponse cyPlayerRes = cyApiService.searchPlayers(word, CyphersPlayerWordType.FULL, limit);
    	for (int i = 0; i < limit; i++) {
			nicknameList.add(cyPlayerRes.getRows().get(i).getNickname());
		}
        return nicknameList;
    }
}
