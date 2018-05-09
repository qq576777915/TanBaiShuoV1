package com.king.tanbaishuo;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Decrypter {
    static Map<String, Integer> map = new HashMap<>();

    static {
        map.put("oe", 0);
        map.put("n", 0);
        map.put("z", 0);
        map.put("7c", 6);
        map.put("oK", 1);
        map.put("6", 1);
        map.put("5", 1);
        map.put("ow", 2);
        map.put("on", 0);
        map.put("-", 2);
        map.put("A", 2);
        map.put("oi", 3);
        map.put("o", 3);
        map.put("i", 3);
        map.put("7e", 4);
        map.put("v", 4);
        map.put("P", 4);
        map.put("7K", 5);
        map.put("4", 5);
        map.put("k", 5);
        map.put("7w", 6);
        map.put("C", 6);
        map.put("s", 6);
        map.put("7i", 7);
        map.put("S", 7);
        map.put("l", 7);
        map.put("Ne", 8);
        map.put("c", 8);
        map.put("F", 8);
        map.put("NK", 9);
        map.put("E", 9);
        map.put("q", 9);
    }

    private static final String[] words = new String[]
            {"oe", "n", "z",    //0
                    "oK", "6", "5",  //1
                    "ow", "-", "A",  //2
                    "oi", "o", "i",  //3
                    "7e", "v", "P",  //4
                    "7K", "4", "k",  //5
                    "7w", "C", "s",  //6
                    "7i", "S", "l",  //7
                    "Ne", "c", "F",  //8
                    "NK", "E", "q"};  //9

    private HashMap<String, Integer>[] maps = new HashMap[3];

    public Decrypter() {
        for (int Dici = 0; Dici < 3; Dici++) {
            maps[Dici] = new HashMap<String, Integer>();
            for (int Wordi = 0; Wordi < 10; Wordi++) {
                maps[Dici].put(words[Dici + 3 * Wordi], Wordi);
            }
        }
    }

    public String Decrypt(String json) throws Exception {
        StringBuilder result = new StringBuilder();
        JSONObject jObj = new JSONObject(json);
        JSONArray jArr = jObj.getJSONObject("data").getJSONArray("list");
        for (int i = 0; i < jArr.length(); i++) {
            JSONObject item = jArr.getJSONObject(i);
            result.append(item.getString("fromNick") + "|");
            result.append(item.getString("topicName") + "|");
            result.append(DecryptFromEncodeUin(item.getString("fromEncodeUin")) + "\n");
        }
        return result.toString();
    }

    public String Decrypt_1(String json) throws Exception {
        StringBuilder result = new StringBuilder();
        JSONObject jObj = new JSONObject(json);
        JSONArray jArr = jObj.getJSONObject("data").getJSONArray("confesses");
        for (int i = 0; i < jArr.length(); i++) {
            JSONObject item = jArr.getJSONObject(i);
            result.append(item.getString("toNick") + "|");
            result.append(item.getString("topicName") + "|");
            result.append(DecryptFromEncodeUin(item.getString("fromEncodeUin")) + "\n");
        }
        return result.toString();
    }

    private String DecryptFromEncodeUin(String str) {
        Log.e("Key为", "" + str);
        StringBuilder sb = new StringBuilder();
        char[] chars = str.substring(4, str.length()).toCharArray();
        for (int i = 0; i < chars.length; i++) {
            Integer word = map.get(a(chars[i], i + 1 == chars.length ? '*' : chars[i + 1])) !=
                    null ? map.get(a(chars[i], chars[++i])) : map.get(a(chars[i]));
            if (word == null){
                sb.append("*");
            } else {
                sb.append(word);
            }
        }
        return sb.toString();
    }

    private static String a(char... chars) {
        StringBuilder sb = new StringBuilder();
        for (char c : chars) {
            sb.append(c);
        }
        return sb.toString();
    }
}