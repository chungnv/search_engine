/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hottab.search.distance.controller;

import com.hottab.search.distance.response.BaseResponse;
import com.hottab.search.distance.response.DistanceResponse;
import com.hottab.search.distance.response.MessageResponse;
import com.hottab.search.elasticsearch.SearchRestaurant;
import com.hottab.search.restaurant.utils.Constant;
import com.hottab.search.restaurant.request.SearchRestaurantRequest;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Chung
 */
@RestController
@RequestMapping("/hottab/restaurant")
public class RestaurantController {

    private Logger logger = Logger.getLogger(RestaurantController.class);

    @RequestMapping(value = "/search_nearest_list", method = RequestMethod.GET)
    public DistanceResponse searchNearestList(@RequestParam(value = "secret_key") String secretKey,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "ordering_method", required = false) String orderingMethod,
            @RequestParam(value = "delivery_fee", required = false) String deliveryFee,
            @RequestParam(value = "minimum_order", required = false) String minimumOrder,
            @RequestParam(value = "tags", required = false) String tags,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "district", required = false) String district,
            @RequestParam(value = "lang", required = false) String lang,
            @RequestParam(value = "businesses", required = false) String businesses,
            @RequestParam(value = "cuisines", required = false) String cuisines,
            @RequestParam(value = "extra_services", required = false) String extraServices,
            @RequestParam(value = "dietary_restrictions", required = false) String dietaryRestrictions,
            @RequestParam(value = "pagination", required = false) String pagination,
            @RequestParam(value = "address_components", required = false) String addressComponents,
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "lat", required = false) String lat,
            @RequestParam(value = "radius", required = false) String radius,
            @RequestParam(value = "lon", required = false) String lon) {

        BaseResponse baseResponse = new BaseResponse();
        DistanceResponse response = new DistanceResponse();
        MessageResponse messageResponse = new MessageResponse();

        //validate secret key
        logger.info("Start process for request: " + secretKey);
        long startTime = System.currentTimeMillis();

        if (!"ND496YYCugIrxYxpybekY33PKha53jk".equals(secretKey)) {
            baseResponse.setCode(Constant.ERROR_CODE_ECRETE_EY_IE);
            baseResponse.setMessage(Constant.ERROR_CODE_ECRETE_EY_IE_MSG);
            messageResponse.setGeneral(baseResponse);
            response.setMessage(messageResponse);
            response.setError(Boolean.TRUE);
            return response;
        }

        SearchRestaurantRequest searchRestaurantRequest = new SearchRestaurantRequest();
        searchRestaurantRequest.setKeyword(keyword);
        searchRestaurantRequest.setOrdering_method(orderingMethod);
        searchRestaurantRequest.setDelivery_fee(deliveryFee);
        searchRestaurantRequest.setMinimum_order(minimumOrder);
        searchRestaurantRequest.setTags(tags);
        searchRestaurantRequest.setCity(city);
        searchRestaurantRequest.setDistrict(district);
        searchRestaurantRequest.setLang(lang);
        searchRestaurantRequest.setBusinesses(businesses);
        searchRestaurantRequest.setCuisines(cuisines);
        searchRestaurantRequest.setExtra_services(extraServices);
        searchRestaurantRequest.setDietary_restrictions(dietaryRestrictions);
        
        if (pagination != null) {
            searchRestaurantRequest.setPagination(Integer.parseInt(pagination));
        }
        searchRestaurantRequest.setAddress_components(addressComponents);
        if (sort != null) {
            searchRestaurantRequest.setSort(Integer.parseInt(sort));
        }
        if (lat != null) {
            searchRestaurantRequest.setLat(Double.valueOf(lat));
        }
        if (lon != null) {
            searchRestaurantRequest.setLon(Double.valueOf(lon));
        }
        
        if (radius != null) {
            searchRestaurantRequest.setRadius(Double.valueOf(radius));
        }
        
        try {
            SearchRestaurant searchRestaurant = new SearchRestaurant();
            JSONArray data = searchRestaurant.searchRestaurant(searchRestaurantRequest, true);
            response.setData(data);
            baseResponse.setCode(Constant.ERROR_CODE_SUCESS_CODE);
            baseResponse.setMessage(Constant.ERROR_CODE_SUCESS_MSG);
            messageResponse.setGeneral(baseResponse);
            response.setMessage(messageResponse);
            response.setError(Boolean.FALSE);
//            System.out.println("Finish processing request: " + secretKey);
            return response;
        } catch (Exception exception) {
            exception.printStackTrace();
            baseResponse.setMessage(exception.getMessage());
            messageResponse.setGeneral(baseResponse);
            response.setMessage(messageResponse);
            response.setError(Boolean.TRUE);
            return response;
        } finally {
            logger.info("Finish processing request: " + secretKey + ". Process time: " + (System.currentTimeMillis() - startTime));
        }
    }

}
