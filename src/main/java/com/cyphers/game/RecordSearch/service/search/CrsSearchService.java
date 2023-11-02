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
import com.cyphers.game.RecordSearch.model.search.MostCypherInfoResponse;
import com.cyphers.game.RecordSearch.model.search.MostPositionInfoResponse;
import com.cyphers.game.RecordSearch.model.search.RecentlyPlayCypherInfoResponse;
import com.cyphers.game.RecordSearch.model.search.SearchDetailDTO;
import com.cyphers.game.RecordSearch.model.search.WinAndLoseCountHistoryResponse;
import com.cyphers.game.RecordSearch.model.search.entity.CrsDetailSearch;
import com.cyphers.game.RecordSearch.model.search.entity.CrsMostCypherInfos;
import com.cyphers.game.RecordSearch.model.search.entity.CrsMostPositionInfos;
import com.cyphers.game.RecordSearch.model.search.entity.CrsRecentlyPlayCypherInfos;
import com.cyphers.game.RecordSearch.model.search.entity.CrsWinAndLoseCountHistory;
import com.cyphers.game.RecordSearch.service.search.repository.CrsDetailSearchRepository;
import com.cyphers.game.RecordSearch.service.search.repository.CrsMostPositionInfosRepository;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class CrsSearchService {

	private final CrsDetailSearchRepository crsDetailSearchRepository;
	private final EntityManager em;
	
	public void upsert(IoSearchDetailResponse detailResponse) {
		CrsDetailSearch temp = crsDetailSearchRepository.findByPlayerId(detailResponse.getPlayerId()).get();
		
		CrsDetailSearch response = em.find(CrsDetailSearch.class, temp.getPlayerId());
		
		if (response == null) {
			insert(detailResponse);
			return;
		}
		
		response = CrsDetailSearch.builder()
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
				.build();
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
		
	}
	
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
	
	public SearchDetailDTO getDetailSearch(String nickname) throws Exception {
		Optional<CrsDetailSearch> crsDetailSearch = crsDetailSearchRepository.findByNickname(nickname);
		if (crsDetailSearch.isEmpty()) {
			throw new Exception("닉네임이 존재하지 않습니다.");
		}
		CrsDetailSearch cds = crsDetailSearch.get();
		
    	List<MostCypherInfoResponse> mostCypherResponse = new ArrayList<>();
    	for (CrsMostCypherInfos crsMostCypher : cds.getMostCypherInfos()) {
    		MostCypherInfoResponse mres = new MostCypherInfoResponse();
    		mres.setCharacterId(crsMostCypher.getCharacterId());
    		mres.setCharacterName(crsMostCypher.getCharacterName());
    		mres.setWinRate(crsMostCypher.getWinRate());
    		mres.setPlayCount(crsMostCypher.getPlayCount());
    		mres.setKda(crsMostCypher.getKda());
    		mostCypherResponse.add(mres);
		}
    	MostPositionInfoResponse mostPositionResponse = new MostPositionInfoResponse();
    	CrsMostPositionInfos crsMostPosition = cds.getMostPositionInfos();
    	mostPositionResponse.setTankerUseRate(crsMostPosition.getTankerUseRate());
    	mostPositionResponse.setRangeDealerUseRate(crsMostPosition.getRangeDealerUseRate());
    	mostPositionResponse.setSupporterUseRate(crsMostPosition.getSupporterUseRate());
    	mostPositionResponse.setMeleeDealerUseRate(crsMostPosition.getMeleeDealerUseRate());
    	List<WinAndLoseCountHistoryResponse> outcomeResponse = new ArrayList<>();
    	for (CrsWinAndLoseCountHistory crsOutcome : cds.getWinAndLoseCountHistory()) {
    		WinAndLoseCountHistoryResponse wres = new WinAndLoseCountHistoryResponse();
    		wres.setHistoryDate(crsOutcome.getHistoryDate());
    		wres.setWinCount(crsOutcome.getWinCount());
    		wres.setLoseCount(crsOutcome.getLoseCount());
    		outcomeResponse.add(wres);
		}
    	List<RecentlyPlayCypherInfoResponse> recentCypherResponse = new ArrayList<>();
    	for (CrsRecentlyPlayCypherInfos crsRecentCypher : cds.getRecentlyPlayCyphersInfos()) {
    		RecentlyPlayCypherInfoResponse rcres = new RecentlyPlayCypherInfoResponse();
    		rcres.setCharacterId(crsRecentCypher.getCharacterId());
    		rcres.setCharacterName(crsRecentCypher.getCharacterName());
    		rcres.setWinCount(crsRecentCypher.getWinCount());
    		rcres.setLoseCount(crsRecentCypher.getLoseCount());
    		rcres.setKillCount(crsRecentCypher.getKillCount());
    		rcres.setDeathCount(crsRecentCypher.getDeathCount());
    		rcres.setAssistCount(crsRecentCypher.getAssistCount());
    		recentCypherResponse.add(rcres);
		}
    	SearchDetailDTO sdDTO = SearchDetailDTO.builder()
			.playerId(cds.getPlayerId())
			.profileCharacterId(cds.getProfileCharacterId())
			.nickname(cds.getNickname())
			.recentlyUpdatedDate(cds.getRecentlyUpdatedDate())
			.mostCypherInfos(mostCypherResponse)
			.mostPositionInfos(mostPositionResponse)
			.ratingGameTier(cds.getRatingGameTier())
			.ratingWinCount(cds.getRatingWinCount())
			.ratingLoseCount(cds.getRatingLoseCount())
			.ratingStopCount(cds.getRatingStopCount())
			.ratingWinRate(cds.getRatingWinRate())
			.normalWinCount(cds.getNormalWinCount())
			.normalLoseCount(cds.getNormalLoseCount())
			.normalStopCount(cds.getNormalStopCount())
			.normalWinRate(cds.getNormalWinRate())
			.winAndLoseCountHistory(outcomeResponse)
			.recentlyPlayCount(cds.getRecentlyPlayCount())
			.recentlyWinRate(cds.getRecentlyWinRate())
			.recentlyKda(cds.getRecentlyKda())
			.recentlyAverageSurvivalRate(cds.getRecentlyAverageSurvivalRate())
			.recentlyPlayCyphersInfos(recentCypherResponse)
			.build();
    	
		return sdDTO;
	}
	
	
}
