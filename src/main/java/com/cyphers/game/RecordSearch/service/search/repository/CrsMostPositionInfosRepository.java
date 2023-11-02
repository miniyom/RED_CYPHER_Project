package com.cyphers.game.RecordSearch.service.search.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cyphers.game.RecordSearch.model.search.entity.CrsDetailSearch;
import com.cyphers.game.RecordSearch.model.search.entity.CrsMostPositionInfos;

public interface CrsMostPositionInfosRepository extends JpaRepository<CrsMostPositionInfos, Integer> {
	Optional<CrsMostPositionInfos> findByCrsDetailSearch(CrsDetailSearch cds);
}
