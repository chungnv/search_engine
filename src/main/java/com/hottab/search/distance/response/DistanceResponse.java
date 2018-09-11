/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hottab.search.distance.response;

import org.json.simple.JSONArray;

/**
 *
 * @author Chung
 */
public class DistanceResponse {
    
    private MessageResponse message;
    private Boolean error;
//    private List<RestaurantResponse> data;
    private JSONArray data;

    public MessageResponse getMessage() {
        return message;
    }

    public void setMessage(MessageResponse message) {
        this.message = message;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }
    
    public JSONArray getData() {
        return data;
    }

    public void setData(JSONArray data) {
        this.data = data;
    }
    
}
