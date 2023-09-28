package com.cyphers.game.RecordSearch.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "crs_game_records")
public class CrsGameRecord {
	@Id @GeneratedValue
	@Column(name = "GR_ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "PLAYER_ID")
	private CrsDetailSearchResponse crsDetailSearchResponse;

	private String gameType;
    private String playCharacterId;
    private String positionName;
    @OneToMany
    @JoinColumn(name = "ATTR_ID")
    private List<CrsAttribute> attributeIds;

    private Integer killCount;
    private Integer deathCount;
    private Integer assistCount;
    private Integer killParticipation;	
    private Float kda;
    private Integer csCount;

    @OneToMany
    @JoinColumn(name = "ATTR_ID")
    private List<CrsItem> itemIds;

    private Integer healAmount;
    private Integer attackPoint;
    private Integer damagePoint;
    private Integer getCoin;
    private Integer battlePoint;
    private Integer sightPoint;

    @OneToMany
    @JoinColumn(name = "NICKNAME_ID")
    private List<CrsPlayerNickname> playerNicknames;
}
