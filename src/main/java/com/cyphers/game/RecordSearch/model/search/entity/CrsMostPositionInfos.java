package com.cyphers.game.RecordSearch.model.search.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
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
@Table(name = "crs_most_position_infos")
public class CrsMostPositionInfos {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MPI_ID")
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "PLAYER_ID")
	private CrsDetailSearch crsDetailSearch;
	
	private Integer tankerUseRate;
	private Integer rangeDealerUseRate;
	private Integer supporterUseRate;
	private Integer meleeDealerUseRate;

}
