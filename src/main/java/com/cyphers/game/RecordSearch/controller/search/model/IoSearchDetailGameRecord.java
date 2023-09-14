package com.cyphers.game.RecordSearch.controller.search.model;

import com.cyphers.game.RecordSearch.cyphers.model.enumuration.CyphersGameType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IoSearchDetailGameRecord {

    private CyphersGameType gameType;
    private String playCharacterId;
    private String postionName;
    private List<String> attributeIds;

    private Integer killCount;
    private Integer deathCount;
    private Integer assistCount;
    private Integer killParticipation;	//킬 관여율. (나의 킬 + 어시/아군팀 총 킬수)
    private Float kda;
    private Integer csCount;

    private List<String> itemIds;

    private Integer healAmount;
    private Integer attackPoint;
    private Integer damagePoint;
    private Integer getCoin;
    private Integer battlePoint;
    private Integer sightPoint;

    private List<String> playerNicknames;
}
