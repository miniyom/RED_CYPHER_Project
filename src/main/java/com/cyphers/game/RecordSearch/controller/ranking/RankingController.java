package com.cyphers.game.RecordSearch.controller.ranking;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cyphers.game.RecordSearch.openapi.model.CyphersPlayerRanking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/search")
@AllArgsConstructor
@Slf4j
public class RankingController {

	@GetMapping("/ranking/player/{playerId}/{nickname}")
	public CyphersPlayerRanking getPlayerRanking(@PathVariable("playerId") String playerId, 
												 @PathVariable("nickname") String nickname) throws Exception {
		return null;
	}
	
	@GetMapping("/ranking/player/{nickname}")
	public CyphersPlayerRanking getAllRanking(@PathVariable("nickname") String nickname) throws Exception {
		return null;
	}
}
