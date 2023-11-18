package com.cyphers.game.RecordSearch.model.search;

import java.util.List;

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
public class GameRecordResponse {
	
	private String next;
	private String playerId;
	
	private List<IoSearchDetailGameRecord> gameRecords;
	
}
