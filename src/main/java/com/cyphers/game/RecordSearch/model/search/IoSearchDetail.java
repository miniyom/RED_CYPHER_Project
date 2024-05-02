package com.cyphers.game.RecordSearch.model.search;

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
public class IoSearchDetail {
    private String playerId;
    private String nickname;
    
    private List<IoSearchDetailMostCypherInfo> mostCypherInfos;
    private Integer tankerUseRate;
    private Integer rangeDealerUseRate;
    private Integer supporterUseRate;
    private Integer meleeDealerUseRate;

    private List<IoSearchDetailResultHistoryInfo> resultHistory;

    private Integer recentlyPlayCount;
    private Integer recentlyWinRate;
    private Float recentlyKda;
    private Integer recentlyAverageSurvivalRate;	
    private List<IoSearchDetailRecentlyPlayCyphersInfo> recentlyPlayCyphersInfos;

}
