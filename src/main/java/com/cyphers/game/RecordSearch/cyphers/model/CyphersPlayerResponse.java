package com.cyphers.game.RecordSearch.cyphers.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CyphersPlayerResponse {
    private List<CyphersPlayer> rows;


}
