package com.cyphers.game.RecordSearch.model.search;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IoSearchDetailResponse {
    private String playerId;
    private String characterId;
    private String nickname;
    private LocalDateTime recentlyUpdatedDate;
    
    private List<IoSearchDetailMostCypherInfo> mostCypherInfos;
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
    
    private List<IoSearchDetailWinAndLoseCountHistoryInfo> winAndLoseCountHistoryInfos;

    private Integer recentlyPlayCount;
    private Integer recentlyWinRate;
    private Float recentlyKda;
    private Integer recentlyAverageSurvivalRate;	
    private List<IoSearchDetailRecentlyPlayCyphersInfo> recentlyPlayCyphersInfos;

//    private List<IoSearchDetailGameRecord> gameRecords;
}
