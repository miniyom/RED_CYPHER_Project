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
public class CrsDetailResponse {
	
	private String renewalTime;

	private String playerId;
	private String nickname;
	
	private LocalDateTime recentlyUpdatedDate;
	
	private List<MostCypherInfoResponse> mostCypherInfos;
	
	private Integer tankerUseRate;
	private Integer rangeDealerUseRate;
	private Integer supporterUseRate;
	private Integer meleeDealerUseRate;

    
    private List<ResultHistoryResponse> resultHistory;

    private Integer recentlyPlayCount;
    private Integer recentlyWinRate;
    private Float recentlyKda;
    private Integer recentlyAverageSurvivalRate;	
    
    private List<RecentlyPlayCypherInfoResponse> recentlyPlayCyphersInfos;
    
}
