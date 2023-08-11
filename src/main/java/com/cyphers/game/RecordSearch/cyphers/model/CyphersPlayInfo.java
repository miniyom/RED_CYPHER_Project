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
public class CyphersPlayInfo {
	private String result;
	private Boolean random;
	private Integer partyUserCount;
	private List<CyphersPartyInfo> partyInfo; 
	private String playTypeName;
	private String characterId;
	private String characterName;
	private Integer level; 
	private Integer killCount; 
	private Integer deathCount; 
	private Integer assistCount; 
	private Integer attackCount; 
	private Integer damagePoint; 
	private Integer battlePoint; 
	private Integer sightPoint; 
	private Integer towerAttackPoint; 
	private Integer backAttackCount; 
	private Integer comboCount; 
	private Integer spellCount; 
	private Integer healAmount; 
	private Integer sentinelKillCount; 
	private Integer demolisherKillCount; 
	private Integer trooperKillCount; 
	private Integer guardianKillCount; 
	private Integer guardTowerKillCount; 
	private Integer getCoin; 
	private Integer spendCoin; 
	private Integer spendConsumablesCoin; 
	private Integer playTime; 
	private Integer responseTime; 
	private Integer minLifeTime; 
	private Integer maxLifeTime; 
}
