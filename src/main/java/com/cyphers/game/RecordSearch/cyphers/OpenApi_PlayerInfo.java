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

import com.cyphers.game.RecordSearch.cyphers.model.CyphersPlayerInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class OpenApi_PlayerInfo {
	
	public static void main (String[] args) throws URISyntaxException, IOException {
        URI uri = new URI("https://api.neople.co.kr/cy/players/d0bafa87b7675759c60ee5282355f8c1?");
        uri = new URIBuilder(uri)
                .addParameter("apikey", "3QcQMjJPpgQr4EOyshsN7Ss3ctYDoFEb").build();

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(new HttpGet(uri)); // post 요청은 HttpPost()를 사용하면 된다.
        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);
        System.out.println(content);

        ObjectMapper mapper = new ObjectMapper();
        CyphersPlayerInfo info = mapper.readValue(content, CyphersPlayerInfo.class);
        System.out.println("playerId = " + info.getPlayerId());
        System.out.println("nickname = " + info.getNickname());
        System.out.println("grade = " + info.getGrade());
        System.out.println("tierTest = " + info.getTierTest());
        System.out.println("characterId = " + info.getRepresent().getCharacterId());
        System.out.println("characterName = " + info.getRepresent().getCharacterName());
        System.out.println("clanName = " + info.getClanName());
        System.out.println("ratingPoint = " + info.getRatingPoint());
        System.out.println("maxRatingPoint = " + info.getMaxRatingPoint());
        System.out.println("tierName = " + info.getTierName());
        System.out.println("Records = " +"\n"	
        					+ "	gameTypeId = "	+ info.getRecords().get(0).getGameTypeId() +"\n"
        					+ "	winCount = "	+ info.getRecords().get(0).getWinCount() +"\n"
        					+ "	loseCount = "	+ info.getRecords().get(0).getLoseCount() +"\n"
        					+ "	stopCount = "	+ info.getRecords().get(0).getStopCount()  +"\n"
        					+ "	gameTypeId = "	+ info.getRecords().get(1).getGameTypeId() +"\n"
        					+ "	winCount = "	+ info.getRecords().get(1).getWinCount() +"\n"
        					+ "	loseCount = "	+ info.getRecords().get(1).getLoseCount() +"\n"
        					+ "	stopCount = "	+ info.getRecords().get(1).getStopCount());
    }
}
