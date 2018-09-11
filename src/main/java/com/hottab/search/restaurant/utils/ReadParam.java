/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hottab.search.restaurant.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ubuntu
 */
public class ReadParam {

    private static Properties properties = null;

    private static ReadParam readParam = null;

    public static ReadParam getInstance() {
        if (readParam == null) {
            readParam = new ReadParam();
            readParam.loadConfig();
        }
        return readParam;
    }

    public void loadConfig() {
        if (properties == null) {
            try {
                properties = new Properties();
                properties.load(new FileInputStream("../conf/config.properties"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public String getString(String key) {
        return properties.getProperty(key);
    }

    public int getInt(String key) {
        return Integer.parseInt(properties.getProperty(key));
    }

}
