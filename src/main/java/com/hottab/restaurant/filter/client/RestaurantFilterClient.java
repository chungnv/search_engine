/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hottab.restaurant.filter.client;

import com.hottab.search.elasticsearch.SearchRestaurant;
import com.hottab.search.jedis.JedisSearch;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Chung
 */
public class RestaurantFilterClient {

    public static final String URL_RESTAURANT_FILTER = "https://donut-dot-hottab-in.appspot.com/restaurant/filter?secret_key=ND496YYCugIrxYxpybekY33PKha53jk";

    public static void main(String[] args) throws Exception {

        RestTemplate restTemplate = new RestTemplate();

        String result = restTemplate.getForObject(URL_RESTAURANT_FILTER, String.class);
        
        System.out.println(result);

        JSONParser parser = new JSONParser();

        JSONObject json = (JSONObject) parser.parse(result);

        JSONObject responseData = (JSONObject) json.get("data");
        
        SearchRestaurant searchProduct = new SearchRestaurant();
        
//        searchProduct.insertData(result);
        
        JSONArray arrRestaurant = (JSONArray) responseData.get("data");
//
//        JedisSearch jedis = new JedisSearch("localhost", 6379);
        int totalLoadToRedis = 0;
        for (Object o : arrRestaurant) {
            JSONObject eachRestaurant = (JSONObject) o;
            try {
                Double lon = Double.valueOf(eachRestaurant.get("long").toString());
                Double lat = Double.valueOf(eachRestaurant.get("lat").toString());
                
                JSONObject a = new JSONObject();
                a.put("lat", lat);
                a.put("lon", lon);
//                a.put("type", "geo_point");
//                a.put("lat_lon", true);
                
                JSONObject b = new JSONObject();
                b.put("location", a);
                
                eachRestaurant.put("location", a);
//                String code = eachRestaurant.get("code").toString();
//                jedis.addGeo("RestaurantLocation", lon, lat, code);
                
                try {
                    String id = searchProduct.insertData(eachRestaurant.toJSONString());
//                    eachRestaurant.toJSONString();
                    System.out.println("ID CREATED: " + id);
                } catch (Exception exception) {
//                    exception.printStackTrace();
                    
                    System.out.println(eachRestaurant.toJSONString());
                    
                }
                
                totalLoadToRedis++;
            } catch (NumberFormatException ex) {
                continue;
            }
        }
        
        System.out.println("Total load to redis: " + totalLoadToRedis);

    }

}
