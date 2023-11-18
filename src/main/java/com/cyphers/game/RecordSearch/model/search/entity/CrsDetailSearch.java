package com.cyphers.game.RecordSearch.model.search.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Table(name = "crs_detail_search")
public class CrsDetailSearch {
	@Id
	@Column(name = "PLAYER_ID")
	private String playerId;

	private String profileCharacterId;
	private String nickname;
	
	private LocalDateTime recentlyUpdatedDate;
	
	private Integer tankerUseRate;
	private Integer rangeDealerUseRate;
	private Integer supporterUseRate;
	private Integer meleeDealerUseRate;
	
	@OneToMany(mappedBy = "crsDetailSearch", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<CrsMostCypherInfos> mostCypherInfos;

    private String ratingGameTier;
    private Integer ratingWinCount;
    private Integer ratingLoseCount;
    private Integer ratingStopCount;
    private Integer ratingWinRate;

    private Integer normalWinCount;
    private Integer normalLoseCount;
    private Integer normalStopCount;
    private Integer normalWinRate;
    
    @OneToMany(mappedBy = "crsDetailSearch", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CrsWinAndLoseCountHistory> winAndLoseCountHistory;

    private Integer recentlyPlayCount;
    private Integer recentlyWinRate;
    private Float recentlyKda;
    private Integer recentlyAverageSurvivalRate;	
    @OneToMany(mappedBy = "crsDetailSearch", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CrsRecentlyPlayCypherInfos> recentlyPlayCyphersInfos;

//    @OneToMany(mappedBy = "crsDetailSearchResponse", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    private List<CrsGameRecord> gameRecords;
}
