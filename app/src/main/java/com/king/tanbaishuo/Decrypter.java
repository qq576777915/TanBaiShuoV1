package com.king.tanbaishuo;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class Decrypter {
    private static final String[] words = new String[]
            {"oe", "n", "z",
                    "oK", "6", "5",
                    "ow", "-", "A",
                    "oi", "o", "i",
                    "7e", "v", "P",
                    "7K", "4", "k",
                    "7w", "C", "s",
                    "7i", "S", "l",
                    "Ne", "c", "F",
                    "NK", "E", "q"};

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

    private String DecryptFromEncodeUin(String str) {
        Log.e("Key为",""+str);
        StringBuilder sb = new StringBuilder();
        char[] chars = str.substring(4, str.length()).toCharArray();
        int flag = 0;
        for (int i = 0; i < chars.length; i++) {
            String key;
            if (flag == 0) {
                key = String.valueOf(chars[i]) + chars[i + 1];
                i++;
            } else {
                key = String.valueOf(chars[i]);
            }

            if (maps[flag].containsKey(key)) {
                sb.append(maps[flag].get(key));
            } else {
                sb.append("?");
            }
            flag = (flag < 2) ? flag + 1 : 0;
        }
        return sb.toString();
    }
}