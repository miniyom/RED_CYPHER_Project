package com.cyphers.game.RecordSearch.controller.stats;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cyphers.game.RecordSearch.model.stats.entity.CrsPickRateStats;
import com.cyphers.game.RecordSearch.model.stats.entity.CrsWinRateStats;
import com.cyphers.game.RecordSearch.service.stats.CrsStatsService;
import com.cyphers.game.RecordSearch.service.stats.StatsService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api/stats")
@AllArgsConstructor
@Slf4j
public class StatsController {
	
	@Autowired
	private StatsService statsService;
	@Autowired
	private CrsStatsService crsStatsService;
	
	@GetMapping("/insert")
	public String inputStats() throws Exception {
		
		crsStatsService.insert(statsService.getWinRateStats(), statsService.getPickRateStats());
		return "입력이 완료되었습니다.";
	}
	
	@GetMapping("/winRate")
	public List<CrsWinRateStats> getWinRateStats() throws Exception {
		return crsStatsService.getWinRateStats();
	}

	@GetMapping("/pickRate")
	public List<CrsPickRateStats> getPickRateStats() throws Exception {
		return crsStatsService.getPickRateStats();
	}
	
	
}
