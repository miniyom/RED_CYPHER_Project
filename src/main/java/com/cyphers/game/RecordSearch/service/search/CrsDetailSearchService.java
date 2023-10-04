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
import com.cyphers.game.RecordSearch.model.CrsAttribute;
import com.cyphers.game.RecordSearch.model.CrsDetailSearchResponse;
import com.cyphers.game.RecordSearch.model.CrsGameRecord;
import com.cyphers.game.RecordSearch.model.CrsItem;
import com.cyphers.game.RecordSearch.model.CrsMostCypherInfos;
import com.cyphers.game.RecordSearch.model.CrsMostPositionInfos;
import com.cyphers.game.RecordSearch.model.CrsPlayerNickname;
import com.cyphers.game.RecordSearch.model.CrsRecentlyPlayCypherInfos;
import com.cyphers.game.RecordSearch.model.CrsWinAndLoseCountHistory;
import com.cyphers.game.RecordSearch.service.search.repository.CrsDetailSearchRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CrsDetailSearchService {

	private final CrsDetailSearchRepository crsDetailSearchRepository;
	
	//데이터 입력
	public void input(IoSearchDetailResponse detailResponse) {
		
		CrsMostPositionInfos positionInfos = CrsMostPositionInfos.builder()
										.playerId(detailResponse.getPlayerId())
										.tankerUseRate(detailResponse.getMostPositionInfos().getTankerUseRate())
										.rangeDealerUseRate(detailResponse.getMostPositionInfos().getRangeDealerUseRate())
										.supporterUseRate(detailResponse.getMostPositionInfos().getSupporterUseRate())
										.meleeDealerUseRate(detailResponse.getMostPositionInfos().getMeleeDealerUseRate())
										.build();
		
		CrsDetailSearchResponse response = CrsDetailSearchResponse.builder()
										.playerId(detailResponse.getPlayerId())
										.profileCharacterId(detailResponse.getProfileCharacterId())
										.nickname(detailResponse.getNickname())
										.recentlyUpdatedDate(LocalDateTime.now())
										.mostCypherInfos(null)
										.mostPositionInfos(positionInfos)
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
			CrsMostCypherInfos crsCypherInfos = CrsMostCypherInfos.builder()
										.crsDetailSearchResponse(response)
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
													.crsDetailSearchResponse(response)
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
														.crsDetailSearchResponse(response)
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
		
		List<CrsGameRecord> gameRecords = new ArrayList<>();
		for (IoSearchDetailGameRecord gameRecord : detailResponse.getGameRecords()) {
			List<CrsAttribute> attributeIds = new ArrayList<>();
			for (String attributeId : gameRecord.getAttributeIds()) {
				CrsAttribute crsAttribute = CrsAttribute.builder()
										.attributeId(attributeId)
										.build();
				attributeIds.add(crsAttribute);
			}
			List<CrsItem> items = new ArrayList<>();
			for (String item : gameRecord.getItemIds()) {
				CrsItem crsItem = CrsItem.builder()
								.itemId(item)
								.build();
				items.add(crsItem);
			}
			List<CrsPlayerNickname> nicknames = new ArrayList<>();
			for (String nickname : gameRecord.getPlayerNicknames()) {
				CrsPlayerNickname crsNickname = CrsPlayerNickname.builder()
											.playerNickname(nickname)
											.build();
				nicknames.add(crsNickname);
			}
			
			CrsGameRecord crsGameRecord = CrsGameRecord.builder()
									.crsDetailSearchResponse(response)
									.gameType(gameRecord.getGameType().getValue())
									.playCharacterId(gameRecord.getPlayCharacterId())
									.positionName(gameRecord.getPositionName())
									.attributeIds(attributeIds)
									.killCount(gameRecord.getKillCount())
									.deathCount(gameRecord.getDeathCount())
									.assistCount(gameRecord.getAssistCount())
									.killParticipation(gameRecord.getKillParticipation())
									.kda(gameRecord.getKda())
									.csCount(gameRecord.getCsCount())
									.itemIds(items)
									.healAmount(gameRecord.getHealAmount())
									.attackPoint(gameRecord.getAttackPoint())
									.damagePoint(gameRecord.getDamagePoint())
									.getCoin(gameRecord.getGetCoin())
									.battlePoint(gameRecord.getBattlePoint())
									.sightPoint(gameRecord.getSightPoint())
									.playerNicknames(nicknames)
									.build();
			gameRecords.add(crsGameRecord);
		}
		response.setGameRecords(gameRecords);
		
		crsDetailSearchRepository.save(response);
		
	}
	
	public void update(CrsDetailSearchResponse crsDetailResponse) {
		crsDetailResponse.setRecentlyUpdatedDate(LocalDateTime.now());
		crsDetailSearchRepository.save(crsDetailResponse);
	}
}
