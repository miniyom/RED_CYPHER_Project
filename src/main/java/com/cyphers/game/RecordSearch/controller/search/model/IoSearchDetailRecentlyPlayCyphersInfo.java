package com.cyphers.game.RecordSearch.controller.search.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IoSearchDetailRecentlyPlayCyphersInfo {
    private String characterId;
    private String characterName;
    private Integer winCount;
    private Integer loseCount;
    private Integer killCount;
    private Integer deathCount;
    private Integer assistCount;
}
