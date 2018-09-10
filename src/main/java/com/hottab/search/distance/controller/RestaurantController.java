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

    @RequestMapping(value = "/search_nearest_list", method = RequestMethod.POST)
    public DistanceResponse searchNearestList(@RequestParam(value = "secret_key") String secretKey, @RequestBody SearchRestaurantRequest searchRestaurantRequest) {

        BaseResponse baseResponse = new BaseResponse();
        DistanceResponse response = new DistanceResponse();
        MessageResponse messageResponse = new MessageResponse();

        //validate secret key
        logger.info("Start process for request: " + secretKey);

        try {
            SearchRestaurant searchRestaurant = new SearchRestaurant();
            String data = searchRestaurant.searchRestaurant(searchRestaurantRequest, true);
            response.setData(data);
            baseResponse.setCode(Constant.ERROR_CODE_SUCESS_CODE);
            baseResponse.setMessage(Constant.ERROR_CODE_SUCESS_MSG);
            messageResponse.setGeneral(baseResponse);
            response.setMessage(messageResponse);
            response.setError(Boolean.FALSE);
//            System.out.println("Finish processing request: " + secretKey);
            logger.info("Finish processing request: " + secretKey);
            return response;
        } catch (Exception exception) {
            exception.printStackTrace();
            baseResponse.setMessage(exception.getMessage());
            messageResponse.setGeneral(baseResponse);
            response.setMessage(messageResponse);
            response.setError(Boolean.FALSE);

            return response;
        }
    }

}
