package com.cyphers.game.RecordSearch.service.ranking;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cyphers.game.RecordSearch.model.ranking.PlayerRankInfo;
import com.cyphers.game.RecordSearch.openapi.model.CyphersPlayerRankInfo;
import com.cyphers.game.RecordSearch.openapi.model.CyphersPlayerRanking;
import com.cyphers.game.RecordSearch.openapi.model.enumuration.CyphersPlayerWordType;
import com.cyphers.game.RecordSearch.openapi.service.CyphersApiService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RankingService {

	@Autowired
	CyphersApiService cyApiService;
	
	public PlayerRankInfo getPlayerRankInfo(String nickname) throws Exception {
		String playerId = cyApiService.searchPlayers(nickname, CyphersPlayerWordType.MATCH, null).getRows().get(0).getPlayerId();
		PlayerRankInfo rankInfo = new PlayerRankInfo();
		CyphersPlayerRankInfo cyPlayerRankInfo = cyApiService.searchPlayerRanking(playerId).getRows().get(0);
		rankInfo.setRank(cyPlayerRankInfo.getRank());
		rankInfo.setNickname(cyPlayerRankInfo.getNickname());
		rankInfo.setGrade(cyPlayerRankInfo.getGrade());
		//티어 계산식 필요
		rankInfo.setTier(null);
		rankInfo.setRatingPoint(cyPlayerRankInfo.getRatingPoint());
		return rankInfo;
	}
	
	public List<PlayerRankInfo> getPlayerRankList(Integer offset, Integer limit) throws Exception {
		List<PlayerRankInfo> rankList = new ArrayList<>();
		List<CyphersPlayerRankInfo> cyRankList = cyApiService.searchRankingList(offset, limit).getRows();
		for (CyphersPlayerRankInfo cyRankInfo : cyRankList) {
			PlayerRankInfo rankInfo = new PlayerRankInfo();
			rankInfo.setRank(cyRankInfo.getRank());
			rankInfo.setNickname(cyRankInfo.getNickname());
			//티어 계산식 필요
			rankInfo.setTier(null);
			rankInfo.setGrade(cyRankInfo.getGrade());
			rankInfo.setRatingPoint(cyRankInfo.getRatingPoint());
			rankList.add(rankInfo);
		}
		return rankList;
	}
}
