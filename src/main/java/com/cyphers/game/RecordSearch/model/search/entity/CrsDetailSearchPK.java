package com.cyphers.game.RecordSearch.model.search.entity;

import java.io.Serializable;

import com.cyphers.game.RecordSearch.openapi.model.enumuration.CyphersGameType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.Data;

@Data
public class CrsDetailSearchPK implements Serializable {

	@Id
	private String playerId;
	
	@Id
	@Enumerated(EnumType.STRING)
	private CyphersGameType gameType;
}
