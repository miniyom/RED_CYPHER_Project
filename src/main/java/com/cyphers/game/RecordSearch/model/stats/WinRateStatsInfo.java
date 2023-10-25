package com.cyphers.game.RecordSearch.model.stats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WinRateStatsInfo {

	private String characterName;
	private Float winRate;

}
