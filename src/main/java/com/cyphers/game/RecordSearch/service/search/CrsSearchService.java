package com.cyphers.game.RecordSearch.service.search;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cyphers.game.RecordSearch.model.search.IoSearchDetailMostCypherInfo;
import com.cyphers.game.RecordSearch.model.search.IoSearchDetailResultHistoryInfo;
import com.cyphers.game.RecordSearch.model.search.IoSearchDetailRecentlyPlayCyphersInfo;
import com.cyphers.game.RecordSearch.model.search.IoSearchDetailResponse;
import com.cyphers.game.RecordSearch.model.search.MostCypherInfoResponse;
import com.cyphers.game.RecordSearch.model.search.ResultHistoryResponse;
import com.cyphers.game.RecordSearch.model.search.RecentlyPlayCypherInfoResponse;
import com.cyphers.game.RecordSearch.model.search.SearchDetailResponse;
import com.cyphers.game.RecordSearch.model.search.entity.CrsDetailSearch;
import com.cyphers.game.RecordSearch.model.search.entity.CrsMostCypherInfos;
import com.cyphers.game.RecordSearch.model.search.entity.CrsResultHistory;
import com.cyphers.game.RecordSearch.model.search.entity.CrsRecentlyPlayCypherInfos;
import com.cyphers.game.RecordSearch.service.search.repository.CrsDetailSearchRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class CrsSearchService {

	private final CrsDetailSearchRepository crsDetailSearchRepository;
	
	public void upsert(IoSearchDetailResponse detailResponse) {
		
		Optional<CrsDetailSearch> cds = crsDetailSearchRepository.findByPlayerId(detailResponse.getPlayerId());
		
		if (!cds.isPresent()) {
			insert(detailResponse);
			return;
		}

		CrsDetailSearch response = cds.get();
		
		response.setPlayerId(detailResponse.getPlayerId());
		response.setProfileCharacterId(detailResponse.getCharacterId());
		response.setNickname(detailResponse.getNickname());
		response.setRecentlyUpdatedDate(LocalDateTime.now());
		
		response.setTankerUseRate(detailResponse.getTankerUseRate());
		response.setRangeDealerUseRate(detailResponse.getRangeDealerUseRate());
		response.setSupporterUseRate(detailResponse.getSupporterUseRate());
		response.setMeleeDealerUseRate(detailResponse.getMeleeDealerUseRate());
		
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
		
		List<CrsMostCypherInfos> mostCypherInfos = response.getMostCypherInfos();
		List<IoSearchDetailMostCypherInfo> ioMostCypherInfos = detailResponse.getMostCypherInfos();
		
        for (int i = 0; i < mostCypherInfos.size(); i++) {
            CrsMostCypherInfos crsMostCypher = mostCypherInfos.get(i);
            IoSearchDetailMostCypherInfo ioMostCypherInfo = ioMostCypherInfos.get(i);
            
            crsMostCypher.setCharacterImage(ioMostCypherInfo.getCharacterImage());
            crsMostCypher.setCharacterName(ioMostCypherInfo.getCharacterName());
            crsMostCypher.setWinRate(ioMostCypherInfo.getWinRate());
            crsMostCypher.setPlayCount(ioMostCypherInfo.getPlayCount());
            crsMostCypher.setKda(ioMostCypherInfo.getKda());
        }
		
		List<CrsResultHistory> resultHistory = response.getResultHistory();
		List<IoSearchDetailResultHistoryInfo> ioRPHistory = detailResponse.getResultHistory();
		
		for (int i = 0; i < mostCypherInfos.size(); i++) {
            CrsResultHistory crsResultHistory = resultHistory.get(i);
            IoSearchDetailResultHistoryInfo ioResultInfo = ioRPHistory.get(i);
            
            crsResultHistory.setHistoryDate(ioResultInfo.getHistoryDate());
            crsResultHistory.setWinCount(ioResultInfo.getWinCount());
            crsResultHistory.setLoseCount(ioResultInfo.getLoseCount());
        }
		
		List<CrsRecentlyPlayCypherInfos> recentCypherInfos = response.getRecentlyPlayCyphersInfos();
		List<IoSearchDetailRecentlyPlayCyphersInfo> ioRecentlyPlayCypher = detailResponse.getRecentlyPlayCyphersInfos();
		
		for (int i = 0; i < recentCypherInfos.size(); i++) {
			CrsRecentlyPlayCypherInfos crsRecentCypher = recentCypherInfos.get(i);
			IoSearchDetailRecentlyPlayCyphersInfo ioRecentCypherInfo = ioRecentlyPlayCypher.get(i);
            
            crsRecentCypher.setCharacterId(ioRecentCypherInfo.getCharacterId());
            crsRecentCypher.setCharacterName(ioRecentCypherInfo.getCharacterName());
            crsRecentCypher.setWinCount(ioRecentCypherInfo.getWinCount());
            crsRecentCypher.setLoseCount(ioRecentCypherInfo.getLoseCount());
            crsRecentCypher.setKillCount(ioRecentCypherInfo.getKillCount());
            crsRecentCypher.setDeathCount(ioRecentCypherInfo.getDeathCount());
            crsRecentCypher.setAssistCount(ioRecentCypherInfo.getAssistCount());
        }
		
		crsDetailSearchRepository.save(response);
		
	}
	
	public void insert(IoSearchDetailResponse detailResponse) {
		
		CrsDetailSearch response = CrsDetailSearch.builder()
								.playerId(detailResponse.getPlayerId()) 
								.profileCharacterId(detailResponse.getCharacterId())
								.nickname(detailResponse.getNickname())
								.recentlyUpdatedDate(LocalDateTime.now())
								.mostCypherInfos(null)
								.tankerUseRate(detailResponse.getTankerUseRate())
								.rangeDealerUseRate(detailResponse.getRangeDealerUseRate())
								.supporterUseRate(detailResponse.getSupporterUseRate())
								.meleeDealerUseRate(detailResponse.getMeleeDealerUseRate())
								.ratingGameTier(detailResponse.getRatingGameTier())
								.ratingWinCount(detailResponse.getRatingWinCount())
								.ratingLoseCount(detailResponse.getRatingLoseCount())
								.ratingStopCount(detailResponse.getRatingStopCount())
								.ratingWinRate(detailResponse.getRatingWinRate())
								.normalWinCount(detailResponse.getNormalWinCount())
								.normalLoseCount(detailResponse.getNormalLoseCount())
								.normalStopCount(detailResponse.getNormalStopCount())
								.normalWinRate(detailResponse.getNormalWinRate())
								.resultHistory(null)
								.recentlyPlayCount(detailResponse.getRecentlyPlayCount())
								.recentlyWinRate(detailResponse.getRecentlyWinRate())
								.recentlyKda(detailResponse.getRecentlyKda())
								.recentlyAverageSurvivalRate(detailResponse.getRecentlyAverageSurvivalRate())
								.recentlyPlayCyphersInfos(null)
								.build();
										
		crsDetailSearchRepository.save(response);
		
		List<CrsMostCypherInfos> mostCypherInfos = new ArrayList<>();
		for (IoSearchDetailMostCypherInfo ioMostCypherInfo : detailResponse.getMostCypherInfos()) {
			CrsMostCypherInfos crsCypherInfos = CrsMostCypherInfos.builder()
										.crsDetailSearch(response)
										.characterImage(ioMostCypherInfo.getCharacterImage())
										.characterName(ioMostCypherInfo.getCharacterName())
										.winRate(ioMostCypherInfo.getWinRate())
										.playCount(ioMostCypherInfo.getPlayCount())
										.kda(ioMostCypherInfo.getKda())
										.build();
			mostCypherInfos.add(crsCypherInfos);
		}
		response.setMostCypherInfos(mostCypherInfos);
		
		List<CrsResultHistory> resultHistory = new ArrayList<>();
		for (IoSearchDetailResultHistoryInfo resultInfo : detailResponse.getResultHistory()) {
			CrsResultHistory crsResultHistory = CrsResultHistory.builder()
											.crsDetailSearch(response)
											.historyDate(resultInfo.getHistoryDate())
											.winCount(resultInfo.getWinCount())
											.loseCount(resultInfo.getLoseCount())
											.build();
			resultHistory.add(crsResultHistory);
		}
		response.setResultHistory(resultHistory);
		
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
	
	public SearchDetailResponse getDetailSearch(String nickname) throws Exception {
		Optional<CrsDetailSearch> crsDetailSearch = crsDetailSearchRepository.findByNickname(nickname);
		if (crsDetailSearch.isEmpty()) {
			throw new Exception("닉네임이 존재하지 않습니다.");
		}
		CrsDetailSearch cds = crsDetailSearch.get();
		
		String renewalTime = getRenewalTime(cds.getRecentlyUpdatedDate());
		
    	List<MostCypherInfoResponse> mostCypherResponse = new ArrayList<>();
    	for (CrsMostCypherInfos crsMostCypher : cds.getMostCypherInfos()) {
    		MostCypherInfoResponse mres = new MostCypherInfoResponse();
    		mres.setCharacterImage(crsMostCypher.getCharacterImage());
    		mres.setCharacterName(crsMostCypher.getCharacterName());
    		mres.setWinRate(crsMostCypher.getWinRate());
    		mres.setPlayCount(crsMostCypher.getPlayCount());
    		mres.setKda(crsMostCypher.getKda());
    		mostCypherResponse.add(mres);
		}
    	List<ResultHistoryResponse> resultHistoryRes = new ArrayList<>();
    	for (CrsResultHistory crsResult : cds.getResultHistory()) {
    		ResultHistoryResponse resultRes = new ResultHistoryResponse();
    		resultRes.setHistoryDate(crsResult.getHistoryDate());
    		resultRes.setWinCount(crsResult.getWinCount());
    		resultRes.setLoseCount(crsResult.getLoseCount());
    		resultHistoryRes.add(resultRes);
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
    	SearchDetailResponse sdDTO = SearchDetailResponse.builder()
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
			.resultHistory(resultHistoryRes)
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
