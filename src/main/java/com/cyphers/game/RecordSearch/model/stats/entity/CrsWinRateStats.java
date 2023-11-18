package com.cyphers.game.RecordSearch.model.stats.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "crs_winrate_stats")
public class CrsWinRateStats {
	
	@Id
	@Column(name = "CHARACTER_NAME")
	private String characterName;
	
	@Column(name = "WINRATE")
	private Float winRate;

}
