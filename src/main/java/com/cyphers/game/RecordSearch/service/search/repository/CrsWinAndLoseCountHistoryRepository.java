package com.cyphers.game.RecordSearch.service.search.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cyphers.game.RecordSearch.model.search.entity.CrsDetailSearch;
import com.cyphers.game.RecordSearch.model.search.entity.CrsWinAndLoseCountHistory;

public interface CrsWinAndLoseCountHistoryRepository extends JpaRepository<CrsWinAndLoseCountHistory, Integer> {
	Optional<CrsWinAndLoseCountHistory> findByCrsDetailSearch(CrsDetailSearch cds);
}
