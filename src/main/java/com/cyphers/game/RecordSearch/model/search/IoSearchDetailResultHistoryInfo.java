package com.cyphers.game.RecordSearch.model.search;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IoSearchDetailResultHistoryInfo {
    private Integer historyDate;
    private Integer winCount;
    private Integer loseCount;
}
