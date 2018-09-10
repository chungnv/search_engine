/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hottab.search.elasticsearch;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

/**
 *
 * @author ubuntu
 */
public class ESManager {

    private static Client client = null;

    public static Client getClient(String host, int port, String cluster) {
        if (client == null) {
            try {
                Settings settings = Settings.builder()
                        .put("cluster.name", cluster)
                        /* .put("client.transport.ignore_cluster_name",true) */
                        //  .put("client.transport.sniff", true) // auto
                        .build();
                TransportClient transClient = new PreBuiltTransportClient(settings)
                        .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
                client = transClient;
            } catch (Exception e) {
                e.printStackTrace();
                client = null;
            }
        }
        return client;
    }

    public static Client getClient(String hosts, String cluster) {
        if (client == null) {
            try {
                String arrHosts[] = hosts.split(",");
                Settings settings = Settings.builder()
                        .put("cluster.name", cluster)
                        .build();
                TransportClient transClient = new PreBuiltTransportClient(settings);

                for (String host : arrHosts) {
                    String ipPort[] = host.split(":");
                    transClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(ipPort[0]), Integer.parseInt(ipPort[1])));
                }

                client = transClient;
            } catch (NumberFormatException | UnknownHostException e) {
                e.printStackTrace();
                client = null;
            }
        }
        return client;
    }

}
