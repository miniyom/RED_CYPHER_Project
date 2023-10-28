package com.cyphers.game.RecordSearch.controller.stats;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cyphers.game.RecordSearch.model.stats.WinRateStatsInfo;
import com.cyphers.game.RecordSearch.service.stats.RateStatsService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/stats")
@AllArgsConstructor
@Slf4j
public class RateStatsController {
	
	@Autowired
	private RateStatsService rateStatsService;
	
	@GetMapping("/winRate")
	public List<WinRateStatsInfo> getWinRateStats() throws Exception {
		return rateStatsService.getWinRateStats();
	}

}
