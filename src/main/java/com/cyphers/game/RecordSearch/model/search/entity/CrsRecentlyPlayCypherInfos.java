package com.cyphers.game.RecordSearch.model.search.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "crs_recent_cypher_infos")
public class CrsRecentlyPlayCypherInfos {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RCI_ID")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
		@JoinColumn(name = "PLAYER_ID", referencedColumnName = "playerId"),
		@JoinColumn(name = "GAME_TYPE", referencedColumnName = "gameType")
	})
	private CrsDetailSearch crsDetailSearch;
	
	private String characterId;
    private String characterName;
    private Integer winCount;
    private Integer loseCount;
    private Float killCount;
    private Float deathCount;
    private Float assistCount;
}
