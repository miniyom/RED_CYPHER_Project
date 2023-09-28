package com.cyphers.game.RecordSearch.service.search.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cyphers.game.RecordSearch.model.CrsDetailSearchResponse;

public interface CrsDetailSearchRepository extends JpaRepository<CrsDetailSearchResponse, String> {

}
