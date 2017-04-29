package com.company;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;

public class Main {

    public static void main(String[] args) throws Exception {

        int startingPageNumber = 1;

        System.out.println(getRevenue(startingPageNumber));

    }

    public static double getRevenue(int pageNumber) throws Exception {

        URL shopifyURL;
        URLConnection shopifyConnection;

        shopifyURL = new URL("https://shopicruit.myshopify.com/admin/orders.json?page=" + pageNumber + "&access_token=c32313df0d0ef512ca64d5b336a0d7c6");
        shopifyConnection = shopifyURL.openConnection();
        InputStreamReader reader = new InputStreamReader(shopifyConnection.getInputStream());
        BufferedReader data = new BufferedReader(reader);

        StringBuilder jsonString = new StringBuilder();

        String inputLine;

        while ((inputLine = data.readLine()) != null) {
            jsonString.append(inputLine);
        }

        //prevent memory leakage
        reader.close();
        data.close();

        JSONObject jsonData = new JSONObject(jsonString.toString());
        JSONArray orders = jsonData.getJSONArray("orders");
        Iterator orderIterator = orders.iterator();

        if (!orderIterator.hasNext()) {
            return 0.0;
        }

        double revenue = 0.0;

        while (orderIterator.hasNext()) {

            JSONObject currentOrder = (JSONObject) orderIterator.next();
            double orderPrice = currentOrder.getDouble("total_price");

            revenue += orderPrice;
        }

        return revenue + getRevenue(++pageNumber);
    }
}
