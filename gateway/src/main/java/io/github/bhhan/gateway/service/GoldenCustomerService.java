package io.github.bhhan.gateway.service;

import org.springframework.stereotype.Service;

/**
 * Created by hbh5274@gmail.com on 2021-02-23
 * Github : http://github.com/bhhan5274
 */

@Service
public class GoldenCustomerService {
    public boolean isGoldenCustomer(String customerId){
        return "baeldung".equalsIgnoreCase(customerId);
    }
}
