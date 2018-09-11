/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hottab.search.restaurant.request;

/**
 *
 * @author ubuntu
 */
public class SearchRestaurantRequest {

    private String secret_key;
    private String keyword;
    private String ordering_method;
    private String delivery_fee;
    private String minimum_order;
    private String tags;
    private String city;
    private String district;
    private String lang;
    private String businesses;
    private String cuisines;
    private String extra_services;
    private String dietary_restrictions;
    private int pagination;
    private String address_components;
    private int sort;
    private double lat;
    private double lon;
    private double radius;

    public String getSecret_key() {
        return secret_key;
    }

    public void setSecret_key(String secret_key) {
        this.secret_key = secret_key;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getOrdering_method() {
        return ordering_method;
    }

    public void setOrdering_method(String ordering_method) {
        this.ordering_method = ordering_method;
    }

    public String getDelivery_fee() {
        return delivery_fee;
    }

    public void setDelivery_fee(String delivery_fee) {
        this.delivery_fee = delivery_fee;
    }

    public String getMinimum_order() {
        return minimum_order;
    }

    public void setMinimum_order(String minimum_order) {
        this.minimum_order = minimum_order;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getBusinesses() {
        return businesses;
    }

    public void setBusinesses(String businesses) {
        this.businesses = businesses;
    }

    public String getCuisines() {
        return cuisines;
    }

    public void setCuisines(String cuisines) {
        this.cuisines = cuisines;
    }

    public String getExtra_services() {
        return extra_services;
    }

    public void setExtra_services(String extra_services) {
        this.extra_services = extra_services;
    }

    public String getDietary_restrictions() {
        return dietary_restrictions;
    }

    public void setDietary_restrictions(String dietary_restrictions) {
        this.dietary_restrictions = dietary_restrictions;
    }

    public int getPagination() {
        return pagination;
    }

    public void setPagination(int pagination) {
        this.pagination = pagination;
    }

    public String getAddress_components() {
        return address_components;
    }

    public void setAddress_components(String address_components) {
        this.address_components = address_components;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
