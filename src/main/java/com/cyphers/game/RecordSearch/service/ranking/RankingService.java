package com.cyphers.game.RecordSearch.service.ranking;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cyphers.game.RecordSearch.cyphers.CyphersApiService;
import com.cyphers.game.RecordSearch.model.ranking.PlayerRankInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RankingService {

	@Autowired
	ObjectMapper objectmapper;
	@Autowired
	CyphersApiService cyApiService;
	
	public PlayerRankInfo getPlayerRankInfo(String playerId, String nickname) throws Exception {
		return null;
	}
	
	public List<PlayerRankInfo> getPlayerRankList(String nickname, Integer offset, Integer limit) throws Exception {
		return null;
	}
}
