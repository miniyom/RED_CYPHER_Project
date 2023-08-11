package com.cyphers.game.RecordSearch.cyphers.model.enumuration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CyphersWordType {
    MATCH("match"),
    FULL("full"),;

    private String value;
}
