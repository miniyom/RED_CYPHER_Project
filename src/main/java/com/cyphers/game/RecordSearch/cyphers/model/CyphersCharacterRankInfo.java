package com.cyphers.game.RecordSearch.cyphers.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CyphersCharacterRankInfo {
	private Integer rank;
	private Integer beforeRank;
	private String playerId;
	private String nickname;
	private Integer grade;
	private Integer winCount;
	private Integer loseCount;
	private String clanName;
}
