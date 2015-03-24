package com.cab404.tan;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Sorry for no comments!
 * Created at 3:50 on 25.01.15
 *
 * @author cab404
 */
public class PutDatIntoDaObject {
    @SuppressWarnings("unchecked")
    public static void put(JSONObject object, Object key, Object value){
        object.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public static void put(JSONArray object, Object value){
        object.add(value);
    }

}
