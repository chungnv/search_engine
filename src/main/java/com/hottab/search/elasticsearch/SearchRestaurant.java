/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hottab.search.elasticsearch;

import com.hottab.search.restaurant.request.OrderByKey;
import com.hottab.search.restaurant.request.RequestByKey;
import com.hottab.search.restaurant.request.SearchRestaurantRequest;
import java.util.List;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.QueryBuilder;
import static org.elasticsearch.index.query.QueryBuilders.geoDistanceQuery;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author ubuntu
 */
public class SearchRestaurant {

    private final String host = "localhost";
    private final String cluster = "my-application";
    private final int port = 9300;
    private final String index = "index_test";
    private final String type = "type_test";
    private final String field1 = "field";
    private final String field2 = "name";

    public String searchProduct(List<RequestByKey> requesetByKey, boolean isLikeSearch) {

        Client client = ESManager.getClient(host, port, cluster);

        SearchRequestBuilder searchRequest = client.prepareSearch(index).setTypes(type);

        for (RequestByKey keyWord : requesetByKey) {
            if (keyWord != null && keyWord.getKey() != null && keyWord.getValue() != null) {
                if (isLikeSearch) {
                    searchRequest.setQuery(QueryBuilders.boolQuery()
                            .should(QueryBuilders.wildcardQuery(keyWord.getKey(), "*" + keyWord.getValue() + "*")));
                } else {
                    searchRequest.setQuery(QueryBuilders.boolQuery()
                            .should(QueryBuilders.wildcardQuery(keyWord.getKey(), keyWord.getValue())));
                }
            }
        }

        SearchResponse searchResponse = searchRequest.execute().actionGet();
        return searchResponse.toString();
    }

    public String searchProduct(List<RequestByKey> requesetByKey, boolean isLikeSearch, List<OrderByKey> lstOrder, boolean orderByLocation) {

        Client client = ESManager.getClient(host, port, cluster);

        SearchRequestBuilder searchRequest = client.prepareSearch(index).setTypes(type);

        if (requesetByKey != null) {
            for (RequestByKey keyWord : requesetByKey) {
                if (keyWord != null && keyWord.getKey() != null && keyWord.getValue() != null) {
                    if (isLikeSearch) {
                        searchRequest.setQuery(QueryBuilders.boolQuery()
                                .should(QueryBuilders.wildcardQuery(keyWord.getKey(), "*" + keyWord.getValue() + "*")));
                    } else {
                        searchRequest.setQuery(QueryBuilders.boolQuery()
                                .should(QueryBuilders.wildcardQuery(keyWord.getKey(), keyWord.getValue())));
                    }
                }
            }
        }

        if (lstOrder != null && lstOrder.size() > 0) {
            for (OrderByKey order : lstOrder) {
                if (order.getOrderType() == 1) {
                    searchRequest.addSort(order.getColumn(), SortOrder.ASC);
                } else {
                    searchRequest.addSort(order.getColumn(), SortOrder.DESC);
                }
            }
        }

        if (orderByLocation) {
            GeoPoint geoPoint = new GeoPoint(21.0224418, 105.8255965);
            searchRequest.addSort(SortBuilders.geoDistanceSort("location", geoPoint).order(SortOrder.ASC).unit(DistanceUnit.KILOMETERS));
        }

        SearchResponse searchResponse = searchRequest.execute().actionGet();
        return searchResponse.toString();
    }

    public String searchRestaurant(SearchRestaurantRequest searchRestaurantRequest, boolean orderByDistance) throws Exception{

        Client client = ESManager.getClient(host, port, cluster);

        SearchRequestBuilder searchRequest = client.prepareSearch(index).setTypes(type);

        boolean hasCondition = false;
        //order method
        if (searchRestaurantRequest.getOrdering_method() != null) {
            if (searchRestaurantRequest.getOrdering_method().equals("2")) { //takeaway
                searchRequest.setQuery(QueryBuilders.boolQuery()
                        .must(QueryBuilders.matchQuery("online_order_setting.do_takeaway", "1")));
            }
            if (searchRestaurantRequest.getOrdering_method().equals("3")) { //delivery
                searchRequest.setQuery(QueryBuilders.boolQuery()
                        .must(QueryBuilders.matchQuery("online_order_setting.do_delivery", "1")));
            }
        }

        //delivery_fee
        if (searchRestaurantRequest.getDelivery_fee() != null) {
            searchRequest.setQuery(QueryBuilders.boolQuery()
                    .must(QueryBuilders.matchQuery("delivery_fee", searchRestaurantRequest.getDelivery_fee())));
        }

        //minimum_order
        //not found field and description
        //tags
        if (searchRestaurantRequest.getTags() != null) {
            String arrTag[] = searchRestaurantRequest.getTags().split(",");
            for (String uuid : arrTag) {
                if (uuid != null && uuid.trim().length() > 0) {
                    searchRequest.setQuery(QueryBuilders.boolQuery()
                            .must(QueryBuilders.matchQuery("tags.tag_uuid", uuid.trim())));
                }
            }
        }

        //city city_uuid
        if (searchRestaurantRequest.getCity() != null) {
            String citys[] = searchRestaurantRequest.getCity().split(",");
            for (String cityUuid : citys) {
                if (cityUuid != null && cityUuid.trim().length() > 0) {
                    searchRequest.setQuery(QueryBuilders.boolQuery()
                            .must(QueryBuilders.matchQuery("city_uuid", cityUuid.trim())));
                }
            }
        }

        //district district_uuid
        if (searchRestaurantRequest.getDistrict() != null) {
            String districts[] = searchRestaurantRequest.getDistrict().split(",");
            for (String districtUuid : districts) {
                if (districtUuid != null && districtUuid.trim().length() > 0) {
                    searchRequest.setQuery(QueryBuilders.boolQuery()
                            .must(QueryBuilders.matchQuery("district_uuid", districtUuid.trim())));
                }
            }
        }

        //lang
        if (searchRestaurantRequest.getLang() != null) {
            searchRequest.setQuery(QueryBuilders.boolQuery()
                    .must(QueryBuilders.matchQuery("translations.lang_iso_code", searchRestaurantRequest.getLang())));
        }

        //business not found
        //cuisines not found
        if (orderByDistance) {
            GeoPoint geoPoint = new GeoPoint(searchRestaurantRequest.getLat(), searchRestaurantRequest.getLon());
            searchRequest.addSort(SortBuilders.geoDistanceSort("location", geoPoint).order(SortOrder.ASC).unit(DistanceUnit.KILOMETERS));
        }

        SearchResponse searchResponse = searchRequest.execute().actionGet();
        JSONParser parser = new JSONParser();
        JSONObject jObject = (JSONObject) parser.parse(searchResponse.toString());
        JSONObject hits =  ((JSONObject) jObject.get("hits"));
        JSONArray arrHit = (JSONArray) hits.get("hits");
        return ((JSONObject) arrHit.get(0)).get("_source").toString();
//        return data.toString();
        
    }

    public String searchProduct(String[] lstKeyWord) {

        Client client = ESManager.getClient(host, port, cluster);

        SearchRequestBuilder searchRequest = client.prepareSearch(index).setTypes(type);

        for (String keyWord : lstKeyWord) {
            if (keyWord != null && keyWord.trim().length() > 0) {
                searchRequest.setQuery(QueryBuilders.boolQuery()
                        .should(QueryBuilders.wildcardQuery(field1, "*" + keyWord + "*")));
            }
        }
        SearchResponse searchResponse = searchRequest.execute().actionGet();
        return searchResponse.toString();
    }

    public String geoSearch() {

        Client client = ESManager.getClient(host, port, cluster);

        QueryBuilder distanceQuery = geoDistanceQuery("location")
                .point(21.0224418, 105.8255965)
                .distance(1, DistanceUnit.KILOMETERS);

        SearchRequestBuilder searchRequest = client.prepareSearch(index).setTypes(type);
//        SearchRequestBuilder searchRequest = client.prepareSearch(index).setTypes(type).setQuery(QueryBuilders.boolQuery().filter(QueryBuilders.wildcardQuery("phone", "*56*")))
//                .setQuery(QueryBuilders.boolQuery().must(distanceQuery));

        GeoPoint geoPoint = new GeoPoint(21.0224418, 105.8255965);

        searchRequest.addSort(SortBuilders.geoDistanceSort("location", geoPoint).order(SortOrder.DESC).unit(DistanceUnit.KILOMETERS));

        SearchResponse searchResponse = searchRequest.execute().actionGet();
        return searchResponse.toString();

    }

    public String insertData(String json) {

        Client client = ESManager.getClient(host, port, cluster);

        IndexResponse response = client.prepareIndex(index, type)
                .setSource(json, XContentType.JSON)
                .get();

        return response.getId();

    }

    public static void main(String[] args) throws Exception {

        SearchRestaurant searchProduct = new SearchRestaurant();
//        String result = searchProduct.searchProduct(new String[] {"value"});
//        System.out.println(result);

//        String json = "{"
//                + "\"field\":\"kimchy\""
//                + "}";
//        RequestByKey r1 = new RequestByKey();
//        r1.setKey("code");
//        r1.setValue("H");
//        r1.setKey("tags.tag_uuid");
//        r1.setValue("777baab0");
//        RequestByKey r2 = new RequestByKey();
//        r2.setKey("phone");
//        r2.setValue("56");
//        List<RequestByKey> lstRequest = new ArrayList<>();
//        lstRequest.add(r1);
//        lstRequest.add(r2);
//        System.out.println(searchProduct.searchProduct(lstRequest, true));  QsQnH3Trq7 LDNCR30Chf  6jxPOnb0cr
//        String response = searchProduct.searchProduct(lstRequest, true);
        SearchRestaurantRequest searchRestaurantRequest = new SearchRestaurantRequest();
        searchRestaurantRequest.setTags("777baab0-8d3f-11e7-aa03-3130c04e0747");
        searchRestaurantRequest.setLat(21.0224418);
        searchRestaurantRequest.setLon(105.8255965);

        String response = searchProduct.searchRestaurant(searchRestaurantRequest, true);
//        String response = searchProduct.geoSearch();

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(response);

        JSONArray hits = (JSONArray) ((JSONObject) jsonObject.get("hits")).get("hits");

        for (Object o : hits) {
            JSONObject object = (JSONObject) o;
            System.out.println(object.toString());
        }

//        System.out.println(searchProduct.geoSearch());
//        System.out.println(searchProduct.insertData(json));
    }

}
