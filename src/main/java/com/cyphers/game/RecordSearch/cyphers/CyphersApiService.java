package com.cyphers.game.RecordSearch.cyphers;

import com.cyphers.game.RecordSearch.cyphers.annotation.Required;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersPlayerInfo;
import com.cyphers.game.RecordSearch.cyphers.model.CyphersPlayerResponse;
import com.cyphers.game.RecordSearch.cyphers.model.enumuration.CyphersWordType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.NotNull;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

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
            params.put("limit", limit.toString());
        }

        if (limit < 10 || limit > 200) {
            throw new IllegalArgumentException("limit은 10 이상, 200 이하까지만 사용할 수 있습니다.");
        }

        if (wordType == CyphersWordType.FULL && (nickname.length() > 8 || nickname.length() < 2) ) {
            throw new IllegalArgumentException("full Search 에서 닉네임 길이는 2자 이상, 8자 이하 여야 합니다.");
        }

        String resString = get("/cy/players", params);
        log.info("request result = {}", resString);
        ObjectMapper mapper = new ObjectMapper();
        CyphersPlayerResponse res = mapper.readValue(resString, CyphersPlayerResponse.class);
        return res;
    }

    public CyphersPlayerInfo searchPlayerInfo(@Required String playerId) throws Exception {
        Map<String, String> params = new HashMap<>();

        String resString = get("/cy/players/" + playerId, params);
        log.info("request result = {}", resString);
        ObjectMapper mapper = new ObjectMapper();
        CyphersPlayerInfo res = mapper.readValue(resString, CyphersPlayerInfo.class);

        return res;
    }
}
