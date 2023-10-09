package com.cyphers.game.RecordSearch.service.search;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cyphers.game.RecordSearch.controller.search.model.IoSearchDetailGameRecord;
import com.cyphers.game.RecordSearch.controller.search.model.IoSearchDetailMostCypherInfo;
import com.cyphers.game.RecordSearch.controller.search.model.IoSearchDetailRecentlyPlayCyphersInfo;
import com.cyphers.game.RecordSearch.controller.search.model.IoSearchDetailResponse;
import com.cyphers.game.RecordSearch.controller.search.model.IoSearchDetailWinAndLoseCountHistoryInfo;
import com.cyphers.game.RecordSearch.model.CrsDetailSearch;
import com.cyphers.game.RecordSearch.model.CrsMostCypherInfos;
import com.cyphers.game.RecordSearch.model.CrsMostPositionInfos;
import com.cyphers.game.RecordSearch.model.CrsRecentlyPlayCypherInfos;
import com.cyphers.game.RecordSearch.model.CrsWinAndLoseCountHistory;
import com.cyphers.game.RecordSearch.model.gameRecord.CrsAttribute;
import com.cyphers.game.RecordSearch.model.gameRecord.CrsGameRecord;
import com.cyphers.game.RecordSearch.model.gameRecord.CrsItem;
import com.cyphers.game.RecordSearch.model.gameRecord.CrsPlayerNickname;
import com.cyphers.game.RecordSearch.service.search.repository.CrsDetailSearchRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CrsSearchService {

	private final CrsDetailSearchRepository crsDetailSearchRepository;
	
	//데이터 입력
	public void input(IoSearchDetailResponse detailResponse) {
		
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
		
//		List<CrsGameRecord> gameRecords = new ArrayList<>();
//		for (IoSearchDetailGameRecord gameRecord : detailResponse.getGameRecords()) {
//			
//			CrsGameRecord crsGameRecord = CrsGameRecord.builder()
//									.crsDetailSearchResponse(response)
//									.gameType(gameRecord.getGameType().getValue())
//									.playCharacterId(gameRecord.getPlayCharacterId())
//									.positionName(gameRecord.getPositionName())
//									.attributeIds(null)
//									.killCount(gameRecord.getKillCount())
//									.deathCount(gameRecord.getDeathCount())
//									.assistCount(gameRecord.getAssistCount())
//									.killParticipation(gameRecord.getKillParticipation())
//									.kda(gameRecord.getKda())
//									.csCount(gameRecord.getCsCount())
//									.itemIds(null)
//									.healAmount(gameRecord.getHealAmount())
//									.attackPoint(gameRecord.getAttackPoint())
//									.damagePoint(gameRecord.getDamagePoint())
//									.getCoin(gameRecord.getGetCoin())
//									.battlePoint(gameRecord.getBattlePoint())
//									.sightPoint(gameRecord.getSightPoint())
//									.playerNicknames(null)
//									.build();
//			
//			List<CrsAttribute> attributeIds = new ArrayList<>();
//			for (String attributeId : gameRecord.getAttributeIds()) {
//				CrsAttribute crsAttribute = CrsAttribute.builder()
//										.attributeId(attributeId)
//										.crsGameRecord(crsGameRecord)
//										.build();
//				attributeIds.add(crsAttribute);
//			}
//			List<CrsItem> items = new ArrayList<>();
//			for (String item : gameRecord.getItemIds()) {
//				CrsItem crsItem = CrsItem.builder()
//								.itemId(item)
//								.build();
//				items.add(crsItem);
//			}
//			List<CrsPlayerNickname> nicknames = new ArrayList<>();
//			for (String nickname : gameRecord.getPlayerNicknames()) {
//				CrsPlayerNickname crsNickname = CrsPlayerNickname.builder()
//											.playerNickname(nickname)
//											.crsGameRecord(crsGameRecord)
//											.build();
//				nicknames.add(crsNickname);
//			}
//			
//			crsGameRecord.setAttributeIds(attributeIds);
//			crsGameRecord.setItemIds(items);
//			crsGameRecord.setPlayerNicknames(nicknames);
//			
//			gameRecords.add(crsGameRecord);
//		}
//		response.setGameRecords(gameRecords);
		
		crsDetailSearchRepository.save(response);
		
	}
	
	public void upsert(CrsDetailSearch crsDetailResponse) {
		crsDetailResponse.setRecentlyUpdatedDate(LocalDateTime.now());
		crsDetailSearchRepository.save(crsDetailResponse);
	}
}
