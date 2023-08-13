package com.cyphers.game.RecordSearch.cyphers.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CyphersItemDetailInfo {
	private String itemId;
	private String itemName;
	private String characterId;
	private String characterName;
	private String rarityCode;
	private String rarityName;
	private String slotCode;
	private String slotName;
	private String seasonCode;
	private String seasonName;
	private String explain;
	private String explainDetail;
}
