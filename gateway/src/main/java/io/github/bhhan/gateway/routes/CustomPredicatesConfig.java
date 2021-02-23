package io.github.bhhan.gateway.routes;

import io.github.bhhan.gateway.predicates.factories.GoldenCustomerRoutePredicateFactory;
import io.github.bhhan.gateway.service.GoldenCustomerService;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by hbh5274@gmail.com on 2021-02-23
 * Github : http://github.com/bhhan5274
 */

@Configuration
public class CustomPredicatesConfig {
    @Bean
    public GoldenCustomerRoutePredicateFactory goldenCustomer(GoldenCustomerService goldenCustomerService){
        return new GoldenCustomerRoutePredicateFactory(goldenCustomerService);
    }

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder, GoldenCustomerRoutePredicateFactory gf){
        return builder.routes()
                .route("dsl_golden_route", r -> r.path("/dsl_api/**")
                    .filters(f -> f.stripPrefix(1))
                    .uri("https://httpbin.org")
                    .predicate(gf.apply(new GoldenCustomerRoutePredicateFactory.Config(true, "customerId"))))
                .route("dsl_common_route", r -> r.path("/dsl_api/**")
                    .filters(f -> f.stripPrefix(1))
                    .uri("https://httpbin.org")
                    .predicate(gf.apply(new GoldenCustomerRoutePredicateFactory.Config(false, "customerId"))))
                .build();
    }
}
