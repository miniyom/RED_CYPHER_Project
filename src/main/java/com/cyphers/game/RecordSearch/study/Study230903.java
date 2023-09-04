package com.cyphers.game.RecordSearch.study;

import org.springframework.data.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

public class Study230903 {
    public static void main(String[] args) throws Exception {
        Map<String, Pair<Integer, Integer>> map = new HashMap<>();
        map.put("a", Pair.of(100, 60));
        map.put("b", Pair.of(67, 67));
        map.put("c", Pair.of(86, 43));
        map.put("d", Pair.of(2, 1));
        map.put("e", Pair.of(55, 37));
        map.put("f", Pair.of(92, 1));
        map.put("g", Pair.of(3, 0));


        // Stream 을 쓰는 경우
        // 정렬한거
        List<Pair<Integer, Integer>> collect = map.values().stream()
                .sorted(Comparator.comparing(Pair::getFirst, (a, b) -> b - a)).limit(3).collect(Collectors.toList());
        for (Pair<Integer, Integer> res : collect) {
//            System.out.println(res.getFirst() + " : " + res.getSecond());
        }

        // ID를 찾는거
        List<String> ids = map.keySet().stream()
                .sorted(Comparator.comparing(s -> s, (a, b) -> map.get(b).getFirst() - map.get(a).getFirst()))
                .limit(3).collect(Collectors.toList());
        for (String id : ids) {
            System.out.println(id);
        }

        System.out.println("=========================");

        // Stream 을 쓰지 않는 경우
        // 최대값 3개만 찾기
        String[] maxIdArr = {"", "", ""};
        Integer[] maxIntegerArr = {0, 0, 0};
        for (String s : map.keySet()) {
            System.out.println("s :: " + map.get(s).getFirst());
            System.out.println("curr :: " + maxIntegerArr[0] +" " + maxIntegerArr[1] +" " + maxIntegerArr[2] );
            if (maxIntegerArr[0] < map.get(s).getFirst()) {
                System.out.println("0번째가 바뀌었다");
                maxIntegerArr[2] = maxIntegerArr[1];
                maxIdArr[2] = maxIdArr[1];
                maxIntegerArr[1] = maxIntegerArr[0];
                maxIdArr[1] = maxIdArr[0];
                maxIntegerArr[0] = map.get(s).getFirst();
                maxIdArr[0] = s;
            } else if (maxIntegerArr[1] < map.get(s).getFirst()) {
                System.out.println("1번째가 바뀌었다");
                maxIntegerArr[2] = maxIntegerArr[1];
                maxIdArr[2] = maxIdArr[1];
                maxIntegerArr[1] = map.get(s).getFirst();
                maxIdArr[1] = s;
            } else if (maxIntegerArr[2] < map.get(s).getFirst()) {
                System.out.println("2번째가 바뀌었다");
                maxIntegerArr[2] = map.get(s).getFirst();
                maxIdArr[2] = s;
            }
            System.out.println("=======================");
        }
        for (Integer integer : maxIntegerArr) {
            System.out.println(integer);
        }
        for (String id : maxIdArr) {
            System.out.println(id);
        }

        System.out.println("=========================");

        // 전체 정렬해서 3개 찾기
        List<String> idList = new ArrayList<>();
        for (String s : map.keySet()) {
            idList.add(s);
        }
        Collections.sort(idList, Comparator.comparing(id -> id, (a, b) -> map.get(b).getFirst() - map.get(a).getFirst()));
        for (String s : idList) {
            System.out.println(s);
        }

        System.out.println("=========================");


        // a b c d e f g
        // 삽입정렬
        // 빅오 O(n^2) 시간복잡도
        for (int i = 0; i < idList.size(); i ++) {
            for (int j = i + 1; j < idList.size(); j ++) {
                if (map.get(idList.get(i)).getFirst() < map.get(idList.get(j)).getFirst()) {
                    String temp = idList.get(i);
                    idList.set(i, idList.get(j));
                    idList.set(j, temp);
                }
            }
        }
        for (String s : idList) {
            System.out.println(s);
        }

    }
}
