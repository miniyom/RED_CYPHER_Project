package com.cyphers.game.RecordSearch.cyphers.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CyphersCharacterPosition {
	private String name;
	private String explain;
	private List<CyphersCharacterAttribute> attribute;
}
