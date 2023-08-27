package com.cyphers.game.RecordSearch.controller.search.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IoSearchDetailMostCypherInfo {
    private String characterId;
    private String characterName;
    private Integer winRate;
    private Integer playCount;
    private Float kda;
}
