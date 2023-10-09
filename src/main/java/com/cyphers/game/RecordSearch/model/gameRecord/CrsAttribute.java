package com.cyphers.game.RecordSearch.model.gameRecord;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "crs_attributes")
public class CrsAttribute {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ATTR_ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "GR_ID")
	private CrsGameRecord crsGameRecord;
	
	@Column(name = "ATTRIBUTE")
	private String attributeId;
}
