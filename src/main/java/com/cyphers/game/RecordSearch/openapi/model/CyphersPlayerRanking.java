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
public class CyphersPlayerRanking {
	private List<CyphersPlayerRankInfo> rows;
}
