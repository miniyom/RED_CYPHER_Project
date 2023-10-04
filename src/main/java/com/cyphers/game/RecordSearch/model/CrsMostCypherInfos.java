package com.cyphers.game.RecordSearch.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@Table(name = "crs_most_cypher_infos")
public class CrsMostCypherInfos {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MCI_ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "PLAYER_ID")
	private CrsDetailSearchResponse crsDetailSearchResponse;
	
	private String characterId;
    private String characterName;
    private Integer winRate;
    private Integer playCount;
    private Float kda;
}