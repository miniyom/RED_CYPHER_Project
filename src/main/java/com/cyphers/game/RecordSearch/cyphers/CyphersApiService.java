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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cyphers.game.RecordSearch.cyphers.annotation.Required;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersMatchingInfo;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersPlayerInfo;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersPlayerResponse;
import com.cyphers.game.RecordSearch.cyphers.model.enumuration.CyphersWordType;
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

    private static final String testPlayerId = "b74c1979fbc7432102a6e524ba9a467b";
    private static final String testNickname = "땅팜";
    private static final String imageUrl = "https://img-api.neople.co.kr/cy/characters/<characterId>?zoom=<zoom>";
    private static final String testMatchId = "2edeaec3bf1f85dbc2dd97a439980bbee50165f7a05c537c310a49b545994b01";
    private static final String testRankingType = "winCount";
    

    public static void main(String[] args) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("nickname", testNickname);
        params.put("matchId", testMatchId);
        params.put("playerId", testPlayerId);
        String cyPlayer = getTest("/cy/players", params);
//        System.out.println("cyPlayer = " + cyPlayer);

        String cyCharacters = getTest("/cy/characters", params);
//        System.out.println("cyCharacters = " + cyCharacters);

        String cyMatchingHistory = getTest("/cy/players/" + testPlayerId + "/matches", params);
//        System.out.println("cyMatchingHistory = " + cyMatchingHistory);

        String cyMatchingDetail = getTest("/cy/matches/" + testMatchId, params);
//        System.out.println("cyMatchingDetail = " + cyMatchingDetail);
        
        String cyPlayerRanking = getTest("/cy/ranking/ratingPoint", params);
//        System.out.println("cyPlayerRanking = " + cyPlayerRanking);
        
        String cyCharacterRanking = getTest("/cy/ranking/characters/" + "testRankingType", params);
        System.out.println("cyCharacterRanking = " + cyCharacters);
        
//        String cyCharacters = getTest("/cy/characters", params);
//        System.out.println("cyCharacters = " + cyCharacters);
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

    public CyphersPlayerResponse searchPlayers(@Required String nickname, CyphersWordType wordType, Integer limit) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("nickname", nickname);

        if (wordType != null) {
            params.put("wordType", wordType.getValue());
        }
        if (limit != null) {
            if (limit < 10 || limit > 200) {
                throw new IllegalArgumentException("limit은 10 이상, 200 이하까지만 사용할 수 있습니다.");
            }
            params.put("limit", limit.toString());
        }

        if (wordType == CyphersWordType.FULL && (nickname.length() > 8 || nickname.length() < 2) ) {
            throw new IllegalArgumentException("full Search 에서 닉네임 길이는 2자 이상, 8자 이하 여야 합니다.");
        }

        return objectMapper.readValue(get("/cy/players", params), CyphersPlayerResponse.class);
    }

    public CyphersPlayerInfo searchPlayerInfo(@Required String playerId) throws Exception {
        Map<String, String> params = new HashMap<>();
        return objectMapper.readValue(get("/cy/players/" + playerId, params), CyphersPlayerInfo.class);
    }

    public CyphersPlayerInfo searchMatchingHistory(@Required String playerId) throws Exception {
        Map<String, String> params = new HashMap<>();
        return objectMapper.readValue(getTest("/cy/players/" + playerId + "/matches", params), CyphersPlayerInfo.class);
    }
    
    public CyphersMatchingInfo searchMatchingDetail(@Required String matchId) throws Exception {
        Map<String, String> params = new HashMap<>();
        return objectMapper.readValue(getTest("/cy/matches/" + matchId, params), CyphersMatchingInfo.class);
    }
    
    //playerId가 있으면 해당 플레이어의 랭킹, 없으면 1위부터 limit까지 나열
    public CyphersPlayerInfo searchPlayerRanking(@Required String playerId) throws Exception {
        Map<String, String> params = new HashMap<>();
        return objectMapper.readValue(getTest("/cy/ranking/ratingPoint", params), CyphersPlayerInfo.class);
    }
    public CyphersPlayerInfo searchRanking() throws Exception {
        Map<String, String> params = new HashMap<>();
        return objectMapper.readValue(getTest("/cy/ranking/ratingPoint", params), CyphersPlayerInfo.class);
    }
}
