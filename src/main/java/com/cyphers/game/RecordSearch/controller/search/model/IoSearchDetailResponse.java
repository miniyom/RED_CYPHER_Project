package com.cyphers.game.RecordSearch.controller.search.model;

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
    private String profileCharacterId;
    private String nickname;
    //db 설계, 생성 후 그 기록 가져와야함
    //jpa활용 필요
    private LocalDateTime recentlyUpdatedDate;
    
    private List<IoSearchDetailMostCypherInfo> mostCypherInfos;
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
    
    private List<IoSearchDetailWinAndLoseCountHistoryInfo> winAndLoseCountHistoryInfos;

    private Integer recentlyPlayCount;
    private Integer recentlyWinRate;
    private Float recentlyKda;
    private Integer recentlyAverageSurvivalRate;	
    private List<IoSearchDetailRecentlyPlayCyphersInfo> recentlyPlayCyphersInfos;

    private List<IoSearchDetailGameRecord> gameRecords;
}
