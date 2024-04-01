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
		rankInfo.setTier(getTierName(cyPlayerRankInfo.getRatingPoint(), cyPlayerRankInfo.getRank()));
		rankInfo.setRatingPoint(cyPlayerRankInfo.getRatingPoint());
		return rankInfo;
	}
	
	public List<PlayerRankInfo> getPlayerRankList(Integer offset, Integer limit) throws Exception {
		List<PlayerRankInfo> rankList = new ArrayList<>();
		List<CyphersPlayerRankInfo> cyRankList = cyApiService.searchRankingList(offset, limit).getRows();
		for (CyphersPlayerRankInfo cyRankInfo : cyRankList) {
			PlayerRankInfo rankInfo = new PlayerRankInfo();
			rankInfo.setRank(cyRankInfo.getRank());
			rankInfo.setBeforeRank(cyRankInfo.getBeforeRank());
			rankInfo.setNickname(cyRankInfo.getNickname());
			rankInfo.setTier(getTierName(cyRankInfo.getRatingPoint(), cyRankInfo.getRank()));
			rankInfo.setGrade(cyRankInfo.getGrade());
			rankInfo.setRatingPoint(cyRankInfo.getRatingPoint());
			rankList.add(rankInfo);
		}
		return rankList;
	}
	
	public Integer getRankerNum() throws Exception {
		Integer offset = 0;
		Integer limit = 500;
		Integer rankerNum = 0;
		Integer apiRankerNum = 1;
		
		while (apiRankerNum != 0) {
			apiRankerNum = cyApiService.searchRankingList(offset, limit).getRows().size();
			rankerNum += apiRankerNum;
			offset += 500;
		}
		return rankerNum;
	}
	
	public String getTierName(Integer ratingPoint, Integer rank) {
		if (ratingPoint < 1300) {
			return "Bronze4";
		}
		if (ratingPoint < 1400) {
			return "Bronze3";
		}
		if (ratingPoint < 1500) {
			return "Bronze2";
		}
		if (ratingPoint < 1600) {
			return "Bronze1";
		}
		if (ratingPoint < 1700) {
			return "Silver4";
		}
		if (ratingPoint < 1800) {
			return "Silver3";
		}
		if (ratingPoint < 1900) {
			return "Silver2";
		}
		if (ratingPoint < 2000) {
			return "Silver1";
		}
		if (ratingPoint < 2100) {
			return "Gold4";
		}
		if (ratingPoint < 2200) {
			return "Gold3";
		}
		if (ratingPoint < 2300) {
			return "Gold2";
		}
		if (ratingPoint < 2400) {
			return "Gold1";
		}
		if (ratingPoint < 2500) {
			return "Zoker4";
		}
		if (ratingPoint < 2600) {
			return "Zoker3";
		}
		if (ratingPoint < 2700) {
			return "Zoker2";
		}
		if (ratingPoint < 2800) {
			return "Zoker1";
		}
		if (ratingPoint > 2800 && rank <= 30) {
			return "Legend";
		}
		if (ratingPoint > 2800 && rank <= 300) {
			return "Hero";
		}
		if (ratingPoint > 2800) {
			return "Ace";
		}
		return "unranked";
	}
}
