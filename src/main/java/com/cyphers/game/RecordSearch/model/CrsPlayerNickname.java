package com.cyphers.game.RecordSearch.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
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
@Table(name = "crs_player_nicknames")
public class CrsPlayerNickname {
	@Id @GeneratedValue
	@Column(name = "NICKNAME_ID")
	private Long id;
	
	@Column(name = "PLAYER_NICKNAME")
	private String playerNickname;
}
