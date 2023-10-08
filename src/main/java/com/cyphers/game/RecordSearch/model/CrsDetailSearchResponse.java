package com.cyphers.game.RecordSearch.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@Table(name = "crs_detail_search_response")
public class CrsDetailSearchResponse {
	@Id
	@Column(name = "PLAYER_ID")
	private String playerId;

	private String profileCharacterId;
	private String nickname;
	
	private LocalDateTime recentlyUpdatedDate;
	
	@OneToMany(mappedBy = "crsDetailSearchResponse", cascade = CascadeType.ALL)
	private List<CrsMostCypherInfos> mostCypherInfos;
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "MPI_ID")
    private CrsMostPositionInfos mostPositionInfos;

    private String ratingGameTier;
    private Integer ratingWinCount;
    private Integer ratingLoseCount;
    private Integer ratingStopCount;
    private Integer ratingWinRate;

    private Integer normalWinCount;
    private Integer normalLoseCount;
    private Integer normalStopCount;
    private Integer normalWinRate;
    
    @OneToMany(mappedBy = "crsDetailSearchResponse", cascade = CascadeType.ALL)
    private List<CrsWinAndLoseCountHistory> winAndLoseCountHistory;

    private Integer recentlyPlayCount;
    private Integer recentlyWinRate;
    private Float recentlyKda;
    private Integer recentlyAverageSurvivalRate;	
    @OneToMany(mappedBy = "crsDetailSearchResponse", cascade = CascadeType.ALL)
    private List<CrsRecentlyPlayCypherInfos> recentlyPlayCyphersInfos;

    @OneToMany(mappedBy = "crsDetailSearchResponse", cascade = CascadeType.ALL)
    private List<CrsGameRecord> gameRecords;
}
