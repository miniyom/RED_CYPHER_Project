package com.cyphers.game.RecordSearch.model.search;

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
public class MostCypherInfoResponse {
	private String characterImage;
    private String characterName;
    private Integer winRate;
    private Integer playCount;
    private Float kda;
}
