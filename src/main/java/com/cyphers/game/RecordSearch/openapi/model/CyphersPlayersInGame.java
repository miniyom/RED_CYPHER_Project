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
public class CyphersPlayersInGame {
	private String playerId;
	private String nickname;
	private CyphersMapInfo map;
	private CyphersPlayInfo playInfo;
	private CyphersCharacterPosition position;
	private List<CyphersEquipItems> items;
	
}
