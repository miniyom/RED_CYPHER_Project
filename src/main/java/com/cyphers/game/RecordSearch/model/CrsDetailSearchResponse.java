package com.cyphers.game.RecordSearch.model;

import java.time.LocalDateTime;
import java.util.List;

import com.cyphers.game.RecordSearch.controller.search.model.IoSearchDetailGameRecord;
import com.cyphers.game.RecordSearch.controller.search.model.IoSearchDetailMostCypherInfo;
import com.cyphers.game.RecordSearch.controller.search.model.IoSearchDetailMostPositionInfo;
import com.cyphers.game.RecordSearch.controller.search.model.IoSearchDetailRecentlyPlayCyphersInfo;
import com.cyphers.game.RecordSearch.controller.search.model.IoSearchDetailWinAndLoseCountHistoryInfo;

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
	
	@OneToMany(mappedBy = "crsDetailSearchResponse")
	private List<CrsMostCypherInfos> mostCypherInfos;
	@OneToOne
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
    
    @OneToMany(mappedBy = "crsDetailSearchResponse")
    private List<CrsWinAndLoseCountHistory> winAndLoseCountHistory;

    private Integer recentlyPlayCount;
    private Integer recentlyWinRate;
    private Float recentlyKda;
    private Integer recentlyAverageSurvivalRate;	
    @OneToMany(mappedBy = "crsDetailSearchResponse")
    private List<CrsRecentlyPlayCypherInfos> recentlyPlayCyphersInfos;

    @OneToMany(mappedBy = "crsDetailSearchResponse")
    private List<CrsGameRecord> gameRecords;
}
