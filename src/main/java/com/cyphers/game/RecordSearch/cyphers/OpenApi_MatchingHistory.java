package com.cyphers.game.RecordSearch.cyphers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import com.cyphers.game.RecordSearch.openapi.model.CyphersPlayerInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class OpenApi_MatchingHistory {

	public static void main(String[] args) throws URISyntaxException, IOException {
		URI uri = new URI("https://api.neople.co.kr/cy/players/d0bafa87b7675759c60ee5282355f8c1/matches?");
        uri = new URIBuilder(uri)
        		.addParameter("gameTypeId", "normal")
        		.addParameter("startDate", "2023-04-01 00:00")
        		.addParameter("endDate", "2023-06-01 23:59")
                .addParameter("apikey", "3QcQMjJPpgQr4EOyshsN7Ss3ctYDoFEb").build();

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(new HttpGet(uri)); // post 요청은 HttpPost()를 사용하면 된다.
        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);
        System.out.println(content);

        ObjectMapper mapper = new ObjectMapper();
        CyphersPlayerInfo info = mapper.readValue(content, CyphersPlayerInfo.class);
        System.out.println("playerId = " + info.getPlayerId());
	}

}
