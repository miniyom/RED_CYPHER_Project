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
public class ItemInfoResponse {
	private String itemId;
	private String itemName;
	private String rarity;
	private String rarityColor;
	private String slotName;
	private String seasonName;
	private String explainDetail;
}
