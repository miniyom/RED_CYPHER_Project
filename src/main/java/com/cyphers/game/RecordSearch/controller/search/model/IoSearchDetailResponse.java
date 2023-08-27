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
    private String profileCharacterId;
    private String nickname;
    private LocalDateTime recentlyUpdatedDate;
    private List<IoSearchDetailMostCypherInfo> mostCypherInfos;

    private String ratingGameTier;
    private Integer ratingWinCount;
    private Integer ratingLoseCount;
    private Integer ratingStopCount;
    private Integer ratingWinRate;

    private Integer normalWinCount;
    private Integer normalLoseCount;
    private Integer normalStopCount;
    private Integer normalWinRate;
    private List<IoSearchDetailRatingPointHistoryInfo> ratingPointHistoryInfos;

    private Integer recentlyPlayCount;
    private Integer recentlyWinRate;
    private Float recentlyKda;
    private Integer recentlyGamePoint;
    private List<IoSearchDetailRecentlyPlayCyphersInfo> recentlyPlayCyphersInfos;

    private List<IoSearchDetailGameRecord> gameRecords;
}
