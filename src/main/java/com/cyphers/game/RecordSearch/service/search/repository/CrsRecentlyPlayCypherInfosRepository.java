package com.cyphers.game.RecordSearch.service.search.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cyphers.game.RecordSearch.model.search.entity.CrsRecentlyPlayCypherInfos;

public interface CrsRecentlyPlayCypherInfosRepository extends JpaRepository<CrsRecentlyPlayCypherInfos, Integer> {

}
