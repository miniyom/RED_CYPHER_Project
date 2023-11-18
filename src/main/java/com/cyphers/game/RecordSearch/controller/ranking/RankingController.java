package com.cyphers.game.RecordSearch.controller.ranking;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cyphers.game.RecordSearch.model.ranking.PlayerRankInfo;
import com.cyphers.game.RecordSearch.openapi.model.CyphersPlayerRanking;
import com.cyphers.game.RecordSearch.openapi.service.CyphersApiService;
import com.cyphers.game.RecordSearch.service.ranking.RankingService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/ranking")
@AllArgsConstructor
@Slf4j
public class RankingController {
	
	@Autowired
	private RankingService rankingService;

	@GetMapping("/player/{nickname}")
	public PlayerRankInfo getPlayerRanking(@PathVariable("nickname") String nickname) throws Exception {
		return rankingService.getPlayerRankInfo(nickname);
	}
	
	@GetMapping("/player/{offset}/{limit}")
	public List<PlayerRankInfo> getRankingList(@PathVariable("offset") Integer offset, 
											  @PathVariable("limit") Integer limit) throws Exception {
		return rankingService.getPlayerRankList(offset, limit);
	}
}
