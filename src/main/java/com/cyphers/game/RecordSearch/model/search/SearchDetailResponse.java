package com.cyphers.game.RecordSearch.model.search;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchDetailResponse {
	
	private String renewalTime;

	private String playerId;
	private String profileCharacterId;
	private String nickname;
	
	private LocalDateTime recentlyUpdatedDate;
	
	private List<MostCypherInfoResponse> mostCypherInfos;
	
	private Integer tankerUseRate;
	private Integer rangeDealerUseRate;
	private Integer supporterUseRate;
	private Integer meleeDealerUseRate;

    private String ratingGameTier;
    private Integer ratingWinCount;
    private Integer ratingLoseCount;
    private Integer ratingStopCount;
    private Integer ratingWinRate;

    private Integer normalWinCount;
    private Integer normalLoseCount;
    private Integer normalStopCount;
    private Integer normalWinRate;
    
    private List<ResultHistoryResponse> winAndLoseCountHistory;

    private Integer recentlyPlayCount;
    private Integer recentlyWinRate;
    private Float recentlyKda;
    private Integer recentlyAverageSurvivalRate;	
    
    private List<RecentlyPlayCypherInfoResponse> recentlyPlayCyphersInfos;
    
}
