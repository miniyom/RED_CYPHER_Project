package com.cyphers.game.RecordSearch.cyphers;

import com.cyphers.game.RecordSearch.cyphers.model.CyphersPlayerResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


@Component
public class OpenApi {

    public static void main (String[] args) throws URISyntaxException, IOException {
        URI uri = new URI("https://api.neople.co.kr/cy/players");
        uri = new URIBuilder(uri)
                .addParameter("nickname", "실은내")
                .addParameter("apikey", "3QcQMjJPpgQr4EOyshsN7Ss3ctYDoFEb").build();

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = httpClient.execute(new HttpGet(uri)); // post 요청은 HttpPost()를 사용하면 된다.
        HttpEntity entity = response.getEntity();
        String content = EntityUtils.toString(entity);
        System.out.println(content);

        ObjectMapper mapper = new ObjectMapper();
        CyphersPlayerResponse res = mapper.readValue(content, CyphersPlayerResponse.class);
        System.out.println("playerId = " + res.getRows().get(0).getPlayerId());
        System.out.println("nickname = " + res.getRows().get(0).getNickname());
        System.out.println("characterId = " + res.getRows().get(0).getRepresent().getCharacterId());
        System.out.println("characterName = " + res.getRows().get(0).getRepresent().getCharacterName());
    }

}
