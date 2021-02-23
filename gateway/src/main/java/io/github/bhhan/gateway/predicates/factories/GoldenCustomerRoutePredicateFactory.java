package io.github.bhhan.gateway.predicates.factories;

import io.github.bhhan.gateway.service.GoldenCustomerService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.http.HttpCookie;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ServerWebExchange;

import javax.validation.constraints.NotEmpty;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Created by hbh5274@gmail.com on 2021-02-23
 * Github : http://github.com/bhhan5274
 */

public class GoldenCustomerRoutePredicateFactory extends AbstractRoutePredicateFactory<GoldenCustomerRoutePredicateFactory.Config> {

    private final GoldenCustomerService goldenCustomerService;

    public GoldenCustomerRoutePredicateFactory(GoldenCustomerService goldenCustomerService) {
        super(Config.class);
        this.goldenCustomerService = goldenCustomerService;
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("isGolden","customerIdCookie");
    }

    @Override
    public Predicate<ServerWebExchange> apply(Config config) {
        return (ServerWebExchange t) -> {
            List<HttpCookie> cookies = t.getRequest()
                    .getCookies()
                    .get(config.getCustomerIdCookie());

            boolean isGolden;

            if(Objects.isNull(cookies) || cookies.isEmpty()){
                isGolden = false;
            }else {
                String customerId = cookies.get(0).getValue();
                isGolden = goldenCustomerService.isGoldenCustomer(customerId);
            }

            return config.isGolden() == isGolden;
        };
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Validated
    public static class Config {
        boolean isGolden = true;

        @NotEmpty
        String customerIdCookie = "customerId";
    }
}
