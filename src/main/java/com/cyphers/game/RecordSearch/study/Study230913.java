package com.cyphers.game.RecordSearch.study;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.cyphers.game.RecordSearch.openapi.model.CyphersMatchedInfo;
import com.cyphers.game.RecordSearch.openapi.model.CyphersMatchingHistory;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Study230913 {

    private static final String testPlayerId = "27717b771c01560f2b64a31aecc5dbcf";

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

    public static void main(String[] args) throws Exception {
        Map<String, String> params = new HashMap<>();
        params.put("gameTypeId", "rating");
        params.put("startDate", "2023-08-30 00:00");
        params.put("endDate", "2023-09-02 23:59");
        params.put("limit", "100");

        ObjectMapper objectMapper = new ObjectMapper();

        List<CyphersMatchedInfo> matchedInfos = new ArrayList<>();
        String next = "";

        int limit = 100;
        while (next != null) {
            System.out.println("===========================================");

            if (!next.equals("")) {
                params.put("next", next);
            }
            String cyMatchingHistory = getTest("/cy/players/" + testPlayerId + "/matches", params);
            System.out.println("cyMatchingHistory = " + cyMatchingHistory);
            CyphersMatchingHistory cyphersMatchingHistory = objectMapper.readValue(cyMatchingHistory, CyphersMatchingHistory.class);

            for (CyphersMatchedInfo row : cyphersMatchingHistory.getMatches().getRows()) {
            	matchedInfos.add(row);
                if (matchedInfos.size() >= limit) {
                    break;
                }
            }
            if (matchedInfos.size() >= limit) {
                break;
            }
//            mathedInfos.addAll(cyphersMatchingHistory.getMatches().getRows());


            next = cyphersMatchingHistory.getMatches().getNext();
            System.out.println("next = " + next);
        }

    }
}
