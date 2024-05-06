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
public class IoSearchDetailRecentlyPlayCyphersInfo {
    private String characterImage;
    private String characterName;
    private Integer winCount;
    private Integer loseCount;
    private Float killCount;
    private Float deathCount;
    private Float assistCount;
}
