package com.cyphers.game.RecordSearch.service.search.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cyphers.game.RecordSearch.model.search.entity.CrsDetailSearch;

public interface CrsDetailSearchRepository extends JpaRepository<CrsDetailSearch, String> {

	Optional<CrsDetailSearch> findByPlayerId(String playerId);
	Optional<CrsDetailSearch> findByNickname(String nickname);
}
