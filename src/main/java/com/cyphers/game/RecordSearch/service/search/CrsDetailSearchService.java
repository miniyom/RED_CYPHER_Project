package com.cyphers.game.RecordSearch.service.search;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cyphers.game.RecordSearch.controller.search.model.IoSearchDetailMostCypherInfo;
import com.cyphers.game.RecordSearch.controller.search.model.IoSearchDetailResponse;
import com.cyphers.game.RecordSearch.model.CrsDetailSearchResponse;
import com.cyphers.game.RecordSearch.model.CrsMostCypherInfos;
import com.cyphers.game.RecordSearch.service.search.repository.CrsDetailSearchRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CrsDetailSearchService {

	private final CrsDetailSearchRepository crsDetailSearchRepository;
	
	//데이터 입력
	public void input(IoSearchDetailResponse detailResponse) {
		
		CrsDetailSearchResponse response = CrsDetailSearchResponse.builder()
										.playerId(detailResponse.getPlayerId())
										.profileCharacterId(detailResponse.getProfileCharacterId())
										.nickname(detailResponse.getNickname())
										.recentlyUpdatedDate(LocalDateTime.now())
										.mostCypherInfos(null)
										.mostPositionInfos(null)
										.ratingGameTier(detailResponse.getRatingGameTier())
										.ratingWinCount(detailResponse.getRatingWinCount())
										.ratingLoseCount(detailResponse.getRatingLoseCount())
										.ratingStopCount(detailResponse.getRatingStopCount())
										.ratingWinRate(detailResponse.getRatingWinRate())
										.normalWinCount(detailResponse.getNormalWinCount())
										.normalLoseCount(detailResponse.getNormalLoseCount())
										.normalStopCount(detailResponse.getNormalStopCount())
										.normalWinRate(detailResponse.getNormalWinRate())
										.winAndLoseCountHistory(null)
										.recentlyPlayCount(detailResponse.getRecentlyPlayCount())
										.recentlyWinRate(detailResponse.getRecentlyWinRate())
										.recentlyKda(detailResponse.getRecentlyKda())
										.recentlyAverageSurvivalRate(detailResponse.getRecentlyAverageSurvivalRate())
										.recentlyPlayCyphersInfos(null)
										.gameRecords(null)
										.build();
										
		crsDetailSearchRepository.save(response);
		
		List<CrsMostCypherInfos> mostCypherInfos = new ArrayList<>();
		for (IoSearchDetailMostCypherInfo ioMostCypherInfo : detailResponse.getMostCypherInfos()) {
			CrsMostCypherInfos cypherInfos = CrsMostCypherInfos.builder()
										.crsDetailSearchResponse(response)
										.characterId(ioMostCypherInfo.getCharacterId())
										.characterName(ioMostCypherInfo.getCharacterName())
										.winRate(ioMostCypherInfo.getWinRate())
										.playCount(ioMostCypherInfo.getPlayCount())
										.kda(ioMostCypherInfo.getKda())
										.build();
			mostCypherInfos.add(cypherInfos);
		}
		response.setMostCypherInfos(mostCypherInfos);
		crsDetailSearchRepository.save(response);
		
	}
	
	public void update(CrsDetailSearchResponse crsDetailResponse) {
		crsDetailResponse.setRecentlyUpdatedDate(LocalDateTime.now());
		crsDetailSearchRepository.save(crsDetailResponse);
	}
}
