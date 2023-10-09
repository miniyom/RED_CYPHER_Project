package com.cyphers.game.RecordSearch.model.gameRecord;

import java.util.List;

import com.cyphers.game.RecordSearch.model.CrsDetailSearch;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "crs_game_records")
public class CrsGameRecord {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "GR_ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "PLAYER_ID")
	private CrsDetailSearch crsDetailSearch;

	private String gameType;
    private String playCharacterId;
    private String positionName;
    @OneToMany(mappedBy = "crsGameRecord", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CrsAttribute> attributeIds;

    private Integer killCount;
    private Integer deathCount;
    private Integer assistCount;
    private Integer killParticipation;	
    private Float kda;
    private Integer csCount;

    @OneToMany(mappedBy = "crsGameRecord", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CrsItem> itemIds;

    private Integer healAmount;
    private Integer attackPoint;
    private Integer damagePoint;
    private Integer getCoin;
    private Integer battlePoint;
    private Integer sightPoint;

    @OneToMany(mappedBy = "crsGameRecord", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<CrsPlayerNickname> playerNicknames;
}
