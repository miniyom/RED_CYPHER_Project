package com.cyphers.game.RecordSearch.openapi.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CyphersMatchingDetails {
	private String date;
	private String gameTypeId;
	private CyphersMapInfo map;
	private List<CyphersTeamInfo> teams;
	private List<CyphersPlayersInGame> players;
	
}
