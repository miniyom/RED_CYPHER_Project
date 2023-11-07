package com.cyphers.game.RecordSearch.openapi.model;

import java.time.LocalDateTime;

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
	private LocalDateTime date;
	private String matchId;
	private CyphersMapInfo map;
	private CyphersPlayInfo playInfo;
	private CyphersCharacterPosition position;
}
