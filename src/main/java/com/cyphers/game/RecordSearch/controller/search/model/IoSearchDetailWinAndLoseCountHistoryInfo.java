package com.cyphers.game.RecordSearch.controller.search.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IoSearchDetailWinAndLoseCountHistoryInfo {
    private Integer historyDate;
    private Integer winCount;
    private Integer loseCount;
}
