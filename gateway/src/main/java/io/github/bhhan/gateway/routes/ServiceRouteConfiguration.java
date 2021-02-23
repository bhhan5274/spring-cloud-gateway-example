package io.github.bhhan.gateway.routes;

import io.github.bhhan.gateway.filters.factories.LoggingGatewayFilterFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by hbh5274@gmail.com on 2021-02-23
 * Github : http://github.com/bhhan5274
 */

@Configuration
public class ServiceRouteConfiguration {
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder, LoggingGatewayFilterFactory loggingGatewayFilterFactory){
        return builder.routes()
                .route("google_route_config", r -> r.path("/service/**")
                .filters(f -> f.rewritePath("/service(?<segment>/?.*)", "$\\{segment}")
                .filter(loggingGatewayFilterFactory.apply(new LoggingGatewayFilterFactory.Config("My Custom Message", true, true))))
                .uri("https://naver.com"))
                .build();
    }
}
