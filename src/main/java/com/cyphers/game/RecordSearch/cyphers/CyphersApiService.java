package com.cyphers.game.RecordSearch.cyphers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cyphers.game.RecordSearch.cyphers.annotation.Required;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersCharacterRanking;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersItemDetailInfo;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersItemSearch;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersMatchingDetails;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersMatchingHistory;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersPlayerInfo;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersPlayerRanking;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersPlayerResponse;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersTsjRanking;
import com.cyphers.game.RecordSearch.cyphers.model.enumuration.CyphersItemWordType;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CyphersApiService {

    @Value("${cyphers.openapi.url}")
    private String uri;
    @Value("${cyphers.openapi.apikey}")
    private String apikey;
    @Value("${cyphers.openapi.testnickname}")
    private String testnickname;
    @Value("${cyphers.openapi.testplayerId}")
    private String testplayerId;

    @Autowired
    private ObjectMapper objectMapper;

    public static String getTest(String url, Map<String, String> params) throws URISyntaxException, IOException {
        URI uri = new URI("https://api.neople.co.kr" + url);
        URIBuilder uriBuilder = new URIBuilder(uri);
        uriBuilder.addParameter("apikey", "3QcQMjJPpgQr4EOyshsN7Ss3ctYDoFEb");
        for (String key : params.keySet()) {
            uriBuilder.addParameter(key, params.get(key));
        }
        uri = uriBuilder.build();

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(new HttpGet(uri)); // post 요청은 HttpPost()를 사용하면 된다.
        HttpEntity entity = response.getEntity();
        return EntityUtils.toString(entity);
    }

    private static final String testPlayerId = "27717b771c01560f2b64a31aecc5dbcf";
    private static final String testNickname = "땅팜";
    private static final String imageUrl = "https://img-api.neople.co.kr/cy/characters/<characterId>?zoom=<zoom>";
    private static final String testMatchId = "2edeaec3bf1f85dbc2dd97a439980bbee50165f7a05c537c310a49b545994b01";
    private static final String testRankingType = "winCount";
    private static final String testCharacterId = "6d7db858c4f20adcf4fc2eee21c2c03f";
    private static final String testTsjType = "melee";
    private static final String testItemName = "파이어 포르테";
    private static final String testWordType = "full";
    private static final Integer testLimit = 1000;
    private static final String testItemId = "19f0134c20a835546c760c38293ce67a";
    

    public static void main(String[] args) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("nickname", testNickname);
        params.put("matchId", testMatchId);
        params.put("playerId", testPlayerId);
        params.put("itemName", testItemName);
        params.put("wordType", testWordType);
        params.put("limit", testLimit.toString());
        
        String cyPlayer = getTest("/cy/players", params);
        System.out.println("cyPlayer = " + cyPlayer);
        
        String cyPlayerInfo = getTest("/cy/players/" + testPlayerId, params);
//        System.out.println("cyPlayerInfo = " + cyPlayerInfo);

        String cyMatchingHistory = getTest("/cy/players/" + testPlayerId + "/matches", params);
//        System.out.println("cyMatchingHistory = " + cyMatchingHistory);

        String cyMatchingDetail = getTest("/cy/matches/" + testMatchId, params);
//        System.out.println("cyMatchingDetail = " + cyMatchingDetail);
        
        String cyPlayerRanking = getTest("/cy/ranking/ratingPoint", params);
//        System.out.println("cyPlayerRanking = " + cyPlayerRanking);
        
        String cyCharacterRanking = getTest("/cy/ranking/characters/" + testCharacterId +"/" + testRankingType, params);
//        System.out.println("cyCharacterRanking = " + cyCharacterRanking);
        
        String cyTsjRanking = getTest("/cy/ranking/tsj/" + testTsjType , params);
//        System.out.println("cyTsjRanking = " + cyTsjRanking);
        
        String cyItemSearch = getTest("/cy/battleitems", params);
//        System.out.println("cyItemSearch = " + cyItemSearch);
        
        String cyItemDetail = getTest("/cy/battleitems/" + testItemId, params);
//        System.out.println("cyItemDetail = " + cyItemDetail);
        
        String cyCharacters = getTest("/cy/characters", params);
//        System.out.println("cyCharacters = " + cyCharacters);
        
//        String a = CyphersWordType.FULL.value();
//        System.out.println(a);
        
    }

    // get요청 보내기
    private String get(String url, Map<String, String> params) throws URISyntaxException, IOException {
        URI uri = new URI(this.uri + url);
        URIBuilder uriBuilder = new URIBuilder(uri);
        uriBuilder.addParameter("apikey", this.apikey);
        for (String key : params.keySet()) {
            uriBuilder.addParameter(key, params.get(key));
        }
        uri = uriBuilder.build();

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(new HttpGet(uri)); // post 요청은 HttpPost()를 사용하면 된다.
        HttpEntity entity = response.getEntity();
        return EntityUtils.toString(entity);
        
    }
    
    //플레이어 검색
    public CyphersPlayerResponse searchPlayers(@Required String nickname, CyphersItemWordType wordType, Integer limit) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("nickname", nickname);

        if (wordType != null) {
        	if (wordType == CyphersItemWordType.FULL && (nickname.length() > 8 || nickname.length() < 2) ) {
                throw new IllegalArgumentException("full Search 에서 닉네임 길이는 2자 이상, 8자 이하 여야 합니다.");
            }
            params.put("wordType", wordType.getValue());
        }
        if (limit != null) {
            if (limit < 10 || limit > 200) {
                throw new IllegalArgumentException("limit은 10 이상, 200 이하까지만 사용할 수 있습니다.");
            }
            params.put("limit", limit.toString());
        }

        return objectMapper.readValue(get("/cy/players", params), CyphersPlayerResponse.class);
    }

    //플레이어 정보 조회
    public CyphersPlayerInfo searchPlayerInfo(@Required String playerId) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("playerId", playerId);
        return objectMapper.readValue(get("/cy/players/" + playerId, params), CyphersPlayerInfo.class);
    }

    //플레이어 매칭 기록 조회
    public CyphersMatchingHistory searchMatchingHistory(@Required String playerId, String gameTypeId, String startDate, String endDate, Integer limit) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("gameTypeId", gameTypeId);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        if (limit != null) {
            if (limit < 10 || limit > 100) {
                throw new IllegalArgumentException("limit은 10 이상, 100 이하까지만 사용할 수 있습니다.");
            }
            params.put("limit", limit.toString());
        }
        
        return objectMapper.readValue(get("/cy/players/" + playerId + "/matches", params), CyphersMatchingHistory.class);
    }
    
    //매칭 상세정보 조회
    public CyphersMatchingDetails searchMatchingDetail(@Required String matchId) throws Exception {
        Map<String, String> params = new HashMap<>();
        return objectMapper.readValue(get("/cy/matches/" + matchId, params), CyphersMatchingDetails.class);
    }
    
    //통합 랭킹 조회
    //playerId가 있으면 해당 플레이어의 랭킹, 없으면 1위부터 limit까지 나열
    public CyphersPlayerRanking searchPlayerRanking(@Required String playerId) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("playerId", playerId);
        return objectMapper.readValue(get("/cy/ranking/ratingPoint", params), CyphersPlayerRanking.class);
    }
    public CyphersPlayerRanking searchRanking(Integer limit) throws Exception {
        Map<String, String> params = new HashMap<>();
        if (limit != null) {
            if (limit < 10 || limit > 1000) {
                throw new IllegalArgumentException("limit은 10 이상, 1000 이하까지만 사용할 수 있습니다.");
            }
            params.put("limit", limit.toString());
        }
        return objectMapper.readValue(get("/cy/ranking/ratingPoint", params), CyphersPlayerRanking.class);
    }
    
    //캐릭터 랭킹 조회
    public CyphersCharacterRanking searchCharacterRanking(@Required String characterId, @Required String rankingType, String playerId, Integer limit) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("playerId", playerId);
        params.put("rankingType", rankingType);
        if (limit != null) {
            if (limit < 10 || limit > 1000) {
                throw new IllegalArgumentException("limit은 10 이상, 1000 이하까지만 사용할 수 있습니다.");
            }
            params.put("limit", limit.toString());
        }
        return objectMapper.readValue(get("/cy/ranking/characters/" + characterId +"/" + rankingType, params), CyphersCharacterRanking.class);
    }
    
    //투신전 랭킹 조회
    public CyphersTsjRanking searchTsjRanking(@Required String tsjType, String playerId, Integer limit) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("playerId", playerId);
        if (limit != null) {
            if (limit < 10 || limit > 1000) {
                throw new IllegalArgumentException("limit은 10 이상, 1000 이하까지만 사용할 수 있습니다.");
            }
            params.put("limit", limit.toString());
        }
        return objectMapper.readValue(get("/cy/ranking/tsj/" + tsjType, params), CyphersTsjRanking.class);
    }
    
    //아이템 검색
    public CyphersItemSearch searchItem(@Required String itemName, CyphersItemWordType wordType, Integer limit, String characterId, String slotCode, String rarityCode, String seasonCode) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("itemName", itemName);
        params.put("wordType", wordType.getValue());
        if (limit != null) {
            if (limit < 10 || limit > 100) {
                throw new IllegalArgumentException("limit은 10 이상, 100 이하까지만 사용할 수 있습니다.");
            }
            params.put("limit", limit.toString());
        }
        params.put("characterId", characterId);
        params.put("slotCode", slotCode);
        params.put("rarityCode", rarityCode);
        params.put("seasonCode", seasonCode);
        return objectMapper.readValue(get("/cy/battleitems", params), CyphersItemSearch.class);
    }
    
    //아이템 상세 정보 조회
    public CyphersItemDetailInfo searchItemDetail(@Required String itemId) throws Exception {
        Map<String, String> params = new HashMap<>();
        return objectMapper.readValue(get("/cy/battleitems/" + itemId, params), CyphersItemDetailInfo.class);
    }
    
}
