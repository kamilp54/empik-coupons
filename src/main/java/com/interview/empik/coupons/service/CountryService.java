package com.interview.empik.coupons.service;

import com.interview.empik.coupons.model.CountryByIp;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class CountryService {

    private final RestClient restClient;

    public CountryService() {
        this.restClient = RestClient.builder().baseUrl("http://ip-api.com/json/").build();
    }

    public String getCountryByIP(String ip) {
        String url = ip + "?fields=status,country";
        CountryByIp response = restClient.get().uri(url).retrieve().body(CountryByIp.class);
        return (response != null && "success".equals(response.getStatus()))
                ? response.getCountry() : "Unknown";
    }
}
