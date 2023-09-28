package com.cyphers.game.RecordSearch.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "crs_outcome_history")
public class CrsWinAndLoseCountHistory {
	@Id @GeneratedValue
	@Column(name = "OH_ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "PLAYER_ID")
	private CrsDetailSearchResponse crsDetailSearchResponse;
	
	private Integer historyDate;
    private Integer winCount;
    private Integer loseCount;
}
