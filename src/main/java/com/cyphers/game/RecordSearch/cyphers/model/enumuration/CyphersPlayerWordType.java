package com.cyphers.game.RecordSearch.cyphers.model.enumuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum CyphersPlayerWordType {
	MATCH("match"),
    FULL("full"),;

    private String value;
}
