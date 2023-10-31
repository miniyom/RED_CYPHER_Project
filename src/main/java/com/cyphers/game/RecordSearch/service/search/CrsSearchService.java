package com.cyphers.game.RecordSearch.service.search;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cyphers.game.RecordSearch.model.search.IoSearchDetailMostCypherInfo;
import com.cyphers.game.RecordSearch.model.search.IoSearchDetailRecentlyPlayCyphersInfo;
import com.cyphers.game.RecordSearch.model.search.IoSearchDetailResponse;
import com.cyphers.game.RecordSearch.model.search.IoSearchDetailWinAndLoseCountHistoryInfo;
import com.cyphers.game.RecordSearch.model.search.entity.CrsDetailSearch;
import com.cyphers.game.RecordSearch.model.search.entity.CrsMostCypherInfos;
import com.cyphers.game.RecordSearch.model.search.entity.CrsMostPositionInfos;
import com.cyphers.game.RecordSearch.model.search.entity.CrsRecentlyPlayCypherInfos;
import com.cyphers.game.RecordSearch.model.search.entity.CrsWinAndLoseCountHistory;
import com.cyphers.game.RecordSearch.service.search.repository.CrsDetailSearchRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CrsSearchService {

	private final CrsDetailSearchRepository crsDetailSearchRepository;
	
	//데이터 입력
	public void insert(IoSearchDetailResponse detailResponse) {
		
		CrsDetailSearch response = CrsDetailSearch.builder()
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
//										.gameRecords(null)
										.build();
										
		crsDetailSearchRepository.save(response);
		
		CrsMostPositionInfos positionInfos = CrsMostPositionInfos.builder()
										.crsDetailSearch(response)
										.tankerUseRate(detailResponse.getMostPositionInfos().getTankerUseRate())
										.rangeDealerUseRate(detailResponse.getMostPositionInfos().getRangeDealerUseRate())
										.supporterUseRate(detailResponse.getMostPositionInfos().getSupporterUseRate())
										.meleeDealerUseRate(detailResponse.getMostPositionInfos().getMeleeDealerUseRate())
										.build();
		response.setMostPositionInfos(positionInfos);
		
		List<CrsMostCypherInfos> mostCypherInfos = new ArrayList<>();
		for (IoSearchDetailMostCypherInfo ioMostCypherInfo : detailResponse.getMostCypherInfos()) {
			CrsMostCypherInfos crsCypherInfos = CrsMostCypherInfos.builder()
										.crsDetailSearch(response)
										.characterId(ioMostCypherInfo.getCharacterId())
										.characterName(ioMostCypherInfo.getCharacterName())
										.winRate(ioMostCypherInfo.getWinRate())
										.playCount(ioMostCypherInfo.getPlayCount())
										.kda(ioMostCypherInfo.getKda())
										.build();
			mostCypherInfos.add(crsCypherInfos);
		}
		response.setMostCypherInfos(mostCypherInfos);
		
		List<CrsWinAndLoseCountHistory> outcomeHistory = new ArrayList<>();
		for (IoSearchDetailWinAndLoseCountHistoryInfo outcomeHistoryInfo : detailResponse.getWinAndLoseCountHistoryInfos()) {
			CrsWinAndLoseCountHistory crsOutcomeHistory = CrsWinAndLoseCountHistory.builder()
													.crsDetailSearch(response)
													.historyDate(outcomeHistoryInfo.getHistoryDate())
													.winCount(outcomeHistoryInfo.getWinCount())
													.loseCount(outcomeHistoryInfo.getLoseCount())
													.build();
			outcomeHistory.add(crsOutcomeHistory);
		}
		response.setWinAndLoseCountHistory(outcomeHistory);
		
		List<CrsRecentlyPlayCypherInfos> recentCypherinfos = new ArrayList<>();
		for (IoSearchDetailRecentlyPlayCyphersInfo recentCypherInfo : detailResponse.getRecentlyPlayCyphersInfos()) {
			CrsRecentlyPlayCypherInfos crsRecentCypherInfo = CrsRecentlyPlayCypherInfos.builder()
														.crsDetailSearch(response)
														.characterId(recentCypherInfo.getCharacterId())
														.characterName(recentCypherInfo.getCharacterName())
														.winCount(recentCypherInfo.getWinCount())
														.loseCount(recentCypherInfo.getLoseCount())
														.killCount(recentCypherInfo.getKillCount())
														.deathCount(recentCypherInfo.getKillCount())
														.assistCount(recentCypherInfo.getAssistCount())
														.build();
			recentCypherinfos.add(crsRecentCypherInfo);
		}
		response.setRecentlyPlayCyphersInfos(recentCypherinfos);
		
		crsDetailSearchRepository.save(response);
		
	}
	
	public Optional<CrsDetailSearch> getDetailSearch(String nickname) {
		return crsDetailSearchRepository.findByNickname(nickname);
	}
	
	public void renewal(CrsDetailSearch crsDetailSearch) {
		crsDetailSearch.setRecentlyUpdatedDate(LocalDateTime.now());
		crsDetailSearchRepository.save(crsDetailSearch);
	}
}
