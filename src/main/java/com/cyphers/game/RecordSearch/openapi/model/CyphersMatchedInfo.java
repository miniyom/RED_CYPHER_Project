package com.cyphers.game.RecordSearch.openapi.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CyphersMatchedInfo {
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDate date;
	private String matchId;
	private CyphersMapInfo map;
	private CyphersPlayInfo playInfo;
	private CyphersCharacterPosition position;
}
