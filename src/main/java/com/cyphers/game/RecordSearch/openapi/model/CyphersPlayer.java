package com.cyphers.game.RecordSearch.openapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CyphersPlayer {
    private String playerId;
    private String nickname;
    private CyphersPlayerRepresent represent;
    private Integer grade;
}
