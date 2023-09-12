package com.cyphers.game.RecordSearch.cyphers.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CyphersMathedInfo {
	private String date;
	private String matchId;
	private CyphersMapInfo map;
	private CyphersPlayInfo playInfo;
	private CyphersCharacterPosition position;
}
