package com.cyphers.game.RecordSearch.model.search;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IoSearchDetailMostCypherInfo {
    private String characterImage;
    private String characterName;
    private Integer winRate;
    private Integer playCount;
    private Float kda;
}
