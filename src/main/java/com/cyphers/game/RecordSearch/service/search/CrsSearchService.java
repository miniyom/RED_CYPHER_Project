package com.cyphers.game.RecordSearch.service.search;

import java.time.Duration;
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
import com.cyphers.game.RecordSearch.model.search.RecentlyPlayCypherInfoResponse;
import com.cyphers.game.RecordSearch.model.search.SearchDetailDTO;
import com.cyphers.game.RecordSearch.model.search.WinAndLoseCountHistoryResponse;
import com.cyphers.game.RecordSearch.model.search.entity.CrsDetailSearch;
import com.cyphers.game.RecordSearch.model.search.entity.CrsMostCypherInfos;
import com.cyphers.game.RecordSearch.model.search.entity.CrsRecentlyPlayCypherInfos;
import com.cyphers.game.RecordSearch.model.search.entity.CrsWinAndLoseCountHistory;
import com.cyphers.game.RecordSearch.service.search.repository.CrsDetailSearchRepository;

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
//		CrsDetailSearch temp = crsDetailSearchRepository.findByPlayerId(detailResponse.getPlayerId()).get();
		
//		CrsDetailSearch response = em.find(CrsDetailSearch.class, temp.getPlayerId());
		CrsDetailSearch response = crsDetailSearchRepository.findByPlayerId(detailResponse.getPlayerId()).get();
		
		if (response == null) {
			insert(detailResponse);
			return;
		}
		
		response.setPlayerId(detailResponse.getPlayerId());
		response.setProfileCharacterId(detailResponse.getProfileCharacterId());
		response.setNickname(detailResponse.getNickname());
		response.setRecentlyUpdatedDate(LocalDateTime.now());
		response.setTankerUseRate(detailResponse.getMostPositionInfos().getTankerUseRate());
		response.setRangeDealerUseRate(detailResponse.getMostPositionInfos().getRangeDealerUseRate());
		response.setSupporterUseRate(detailResponse.getMostPositionInfos().getSupporterUseRate());
		response.setMeleeDealerUseRate(detailResponse.getMostPositionInfos().getMeleeDealerUseRate());
		response.setRatingGameTier(detailResponse.getRatingGameTier());
		response.setRatingWinCount(detailResponse.getRatingWinCount());
		response.setRatingLoseCount(detailResponse.getRatingLoseCount());
		response.setRatingStopCount(detailResponse.getRatingStopCount());
		response.setRatingWinRate(detailResponse.getRatingWinRate());
		response.setNormalWinCount(detailResponse.getNormalWinCount());
		response.setNormalLoseCount(detailResponse.getNormalLoseCount());
		response.setNormalStopCount(detailResponse.getNormalStopCount());
		response.setNormalWinRate(detailResponse.getNormalWinRate());
		response.setRecentlyPlayCount(detailResponse.getRecentlyPlayCount());
		response.setRecentlyWinRate(detailResponse.getRecentlyWinRate());
		response.setRecentlyKda(detailResponse.getRecentlyKda());
		response.setRecentlyAverageSurvivalRate(detailResponse.getRecentlyAverageSurvivalRate());
		
//		response = CrsDetailSearch.builder()
//				.playerId(detailResponse.getPlayerId()) 
//				.profileCharacterId(detailResponse.getProfileCharacterId())
//				.nickname(detailResponse.getNickname())
//				.recentlyUpdatedDate(LocalDateTime.now())
//				.mostCypherInfos(null)
//				.mostPositionInfos(null)
//				.ratingGameTier(detailResponse.getRatingGameTier())
//				.ratingWinCount(detailResponse.getRatingWinCount())
//				.ratingLoseCount(detailResponse.getRatingLoseCount())
//				.ratingStopCount(detailResponse.getRatingStopCount())
//				.ratingWinRate(detailResponse.getRatingWinRate())
//				.normalWinCount(detailResponse.getNormalWinCount())
//				.normalLoseCount(detailResponse.getNormalLoseCount())
//				.normalStopCount(detailResponse.getNormalStopCount())
//				.normalWinRate(detailResponse.getNormalWinRate())
//				.winAndLoseCountHistory(null)
//				.recentlyPlayCount(detailResponse.getRecentlyPlayCount())
//				.recentlyWinRate(detailResponse.getRecentlyWinRate())
//				.recentlyKda(detailResponse.getRecentlyKda())
//				.recentlyAverageSurvivalRate(detailResponse.getRecentlyAverageSurvivalRate())
//				.recentlyPlayCyphersInfos(null)
//				.build();
		
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
	
	public void insert(IoSearchDetailResponse detailResponse) {
		
		CrsDetailSearch response = CrsDetailSearch.builder()
								.playerId(detailResponse.getPlayerId()) 
								.profileCharacterId(detailResponse.getProfileCharacterId())
								.nickname(detailResponse.getNickname())
								.recentlyUpdatedDate(LocalDateTime.now())
								.mostCypherInfos(null)
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
		
		String renewalTime = getRenewalTime(cds.getRecentlyUpdatedDate());
		
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
    		.renewalTime(renewalTime)
			.playerId(cds.getPlayerId())
			.profileCharacterId(cds.getProfileCharacterId())
			.nickname(cds.getNickname())
			.recentlyUpdatedDate(cds.getRecentlyUpdatedDate())
			.mostCypherInfos(mostCypherResponse)
			.tankerUseRate(cds.getTankerUseRate())
	    	.rangeDealerUseRate(cds.getRangeDealerUseRate())
	    	.supporterUseRate(cds.getSupporterUseRate())
	    	.meleeDealerUseRate(cds.getMeleeDealerUseRate())
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
	
	public String getRenewalTime(LocalDateTime recentTime) {
		LocalDateTime now = LocalDateTime.now();
		Duration duration = Duration.between(recentTime, now);
		long term = duration.getSeconds();
		if (term < 60) {
			return term + "초 전";
		}
		if (term > 60 && term < 60 * 60) {
			return term / 60 + "분 전";
		}
		if (term > 60 * 60 && term < 60 * 60 * 24) {
			return term / (60 * 60) + "시간 전";
		}
		if (term > 60 * 60 * 24 && term < 60 * 60 * 24 * 30) {
			return term / (60 * 60 * 24) + "일 전";
		}
		if (term > 60 * 60 * 24 * 30 && term < 60 * 60 * 24 * 30 * 12) {
			return term / (60 * 60 * 24 * 30) + "개월 전";
		}
		if (term > 60 * 60 * 24 * 30 * 12) {
			return term / (60 * 60 * 24 * 30 * 12) + "년 전";		
		}
		
		return "닉네임 정보가 없습니다";
	}
}
