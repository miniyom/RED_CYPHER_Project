package com.cyphers.game.RecordSearch.service.search.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cyphers.game.RecordSearch.model.search.entity.CrsDetailSearch;
import com.cyphers.game.RecordSearch.openapi.model.enumuration.CyphersGameType;

public interface CrsDetailSearchRepository extends JpaRepository<CrsDetailSearch, String> {

	List<CrsDetailSearch> findByPlayerId(String playerId);
	Optional<CrsDetailSearch> findByNickname(String nickname);
	Optional<CrsDetailSearch> findByPlayerIdAndGameType(String playerId, CyphersGameType gameType);
	Optional<CrsDetailSearch> findByNicknameAndGameType(String playerId, CyphersGameType gameType);
}
