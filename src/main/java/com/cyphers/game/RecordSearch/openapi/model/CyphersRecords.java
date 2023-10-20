package com.cyphers.game.RecordSearch.openapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CyphersRecords {

	private String gameTypeId;
	private Integer winCount;
	private Integer loseCount;
	private Integer stopCount;

}
	