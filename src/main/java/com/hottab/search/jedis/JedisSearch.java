/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hottab.search.jedis;

import com.hottab.search.distance.response.RestaurantResponse;
import java.util.ArrayList;
import java.util.List;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.geo.GeoRadiusParam;

/**
 *
 * @author Chung
 */
public class JedisSearch {

    private final double DISTANCE_STEP = 2; //km

    private Jedis jedis = null;

    private String ip;
    private int port;

    public JedisSearch(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public synchronized Jedis getJedis() {
        if (jedis == null) {
            jedis = new Jedis(ip, port);
        }
        return jedis;
    }

    public RestaurantResponse getNearestLocation(String key, long lon, long lat) {
        RestaurantResponse response = new RestaurantResponse();
        for (int i = 1; i <= 10; i++) { //tim trong ban kinh toi da 20km
            List<GeoRadiusResponse> listResponse = getJedis().georadius(key, lon, lat,
                    i * DISTANCE_STEP, GeoUnit.MI, GeoRadiusParam.geoRadiusParam().sortAscending());
            if (listResponse != null && listResponse.size() > 0) {
                GeoRadiusResponse nearest = listResponse.get(0);
                response.setCode(nearest.getMemberByString());
                response.setLat(nearest.getCoordinate().getLatitude());
                response.setLat(nearest.getCoordinate().getLongitude());
                response.setDistance(nearest.getDistance());
                return response;
            }
        }
        return response;
    }

    public List<RestaurantResponse> getListNearestLocation(String key, double lon, double lat, int total) throws Exception {
        List<RestaurantResponse> ListNearestLocation = new ArrayList<>();
//        for (int i = 1; i <= 10; i++) { //tim trong ban kinh toi da 20km
        int count = 0;
        List<GeoRadiusResponse> listResponse = getJedis().georadius(key, lon, lat,
                10 * DISTANCE_STEP, GeoUnit.MI, GeoRadiusParam.geoRadiusParam().sortAscending());
        if (listResponse != null && listResponse.size() > 0) {
            for (GeoRadiusResponse geoRadiusResponse : listResponse) {
                count++;
                RestaurantResponse response = new RestaurantResponse();
                response.setCode(geoRadiusResponse.getMemberByString());
                if (geoRadiusResponse.getCoordinate() != null) {
                    response.setLat(geoRadiusResponse.getCoordinate().getLatitude());
                    response.setLat(geoRadiusResponse.getCoordinate().getLongitude());
                }
                response.setDistance(geoRadiusResponse.getDistance());
                ListNearestLocation.add(response);
                if (count == total) {
                    break;
                }
            }
        }
        return ListNearestLocation;
    }

    public void addGeo(String key, double lon, double lat, String name) {
        getJedis().geoadd(key, lon, lat, name);
    }

}
