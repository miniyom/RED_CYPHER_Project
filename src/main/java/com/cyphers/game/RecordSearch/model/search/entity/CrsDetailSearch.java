package com.cyphers.game.RecordSearch.model.search.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.cyphers.game.RecordSearch.openapi.model.enumuration.CyphersGameType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
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
@IdClass(CrsDetailSearchPK.class)
@Table(name = "crs_detail_search")
public class CrsDetailSearch {
	@Id
	@Column(name = "PLAYER_ID")
	private String playerId;
	
	@Id
	@Column(name = "GAME_TYPE")
	@Enumerated(EnumType.STRING)
	private CyphersGameType gameType;

	private String nickname;
	
	private LocalDateTime recentlyUpdatedDate;
	
	private Integer tankerUseRate;
	private Integer rangeDealerUseRate;
	private Integer supporterUseRate;
	private Integer meleeDealerUseRate;
	
	@OneToMany(mappedBy = "crsDetailSearch", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<CrsMostCypherInfos> mostCypherInfos;

    @OneToMany(mappedBy = "crsDetailSearch", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CrsResultHistory> resultHistory;

    private Integer recentlyPlayCount;
    private Integer recentlyWinRate;
    private Float recentlyKda;
    private Integer recentlyAverageSurvivalRate;	
    @OneToMany(mappedBy = "crsDetailSearch", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CrsRecentlyPlayCypherInfos> recentlyPlayCyphersInfos;

}
