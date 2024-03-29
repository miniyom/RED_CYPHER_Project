package com.cyphers.game.RecordSearch.model.ranking;

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
public class PlayerRankInfo {

	private Integer rank;
	private Integer beforeRank;
	private String nickname;
	private Integer grade;
	private String tier;
	private Integer ratingPoint;
}
