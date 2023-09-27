package com.cyphers.game.RecordSearch.model;

import java.time.LocalDateTime;

import com.cyphers.game.RecordSearch.controller.search.model.IoSearchDetailMostPositionInfo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
	private String playerId;

	private String profileCharacterId;
	private String nickname;
	
	private LocalDateTime recentlyUpdatedDate;
	
	
//	private List<IoSearchDetailMostCypherInfo> mostCypherInfos;
	@OneToOne
	@JoinColumn(name = "MPI_ID")
    private IoSearchDetailMostPositionInfo mostPositionInfos;

    private String ratingGameTier;
    private Integer ratingWinCount;
    private Integer ratingLoseCount;
    private Integer ratingStopCount;
    private Integer ratingWinRate;

    private Integer normalWinCount;
    private Integer normalLoseCount;
    private Integer normalStopCount;
    private Integer normalWinRate;
    
//    private List<IoSearchDetailWinAndLoseCountHistoryInfo> winAndLoseCountHistoryInfos;

    private Integer recentlyPlayCount;
    private Integer recentlyWinRate;
    private Float recentlyKda;
    private Integer recentlyAverageSurvivalRate;	
//    private List<IoSearchDetailRecentlyPlayCyphersInfo> recentlyPlayCyphersInfos;

//    private List<IoSearchDetailGameRecord> gameRecords;
}
