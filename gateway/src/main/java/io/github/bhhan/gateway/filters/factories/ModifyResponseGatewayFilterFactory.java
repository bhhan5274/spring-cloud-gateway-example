package io.github.bhhan.gateway.filters.factories;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Optional;

/**
 * Created by hbh5274@gmail.com on 2021-02-23
 * Github : http://github.com/bhhan5274
 */

@Slf4j
@Component
public class ModifyResponseGatewayFilterFactory extends AbstractGatewayFilterFactory<ModifyResponseGatewayFilterFactory.Config> {

    public ModifyResponseGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    ServerHttpResponse response = exchange.getResponse();

                    Optional.ofNullable(exchange.getRequest()
                        .getQueryParams()
                        .getFirst("locale"))
                    .ifPresent(qp -> {
                        String responseContentLanguage = response.getHeaders()
                                .getContentLanguage()
                                .getLanguage();

                        response.getHeaders()
                                .add("Bael-Custom-Language-Header", responseContentLanguage);

                        log.info("Added custom header to Response");
                    });
                }));
    }

    public static class Config {
    }
}
