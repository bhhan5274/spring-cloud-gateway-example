package io.github.bhhan.gateway.filters.global;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Created by hbh5274@gmail.com on 2021-02-23
 * Github : http://github.com/bhhan5274
 */

@Slf4j
@Component
public class FirstPreLastPostGlobalFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("First Pre Global Filter");
        return chain.filter(exchange)
                .then(Mono.fromRunnable(() -> {
                    log.info("Last Post Global Filter");
                }));
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
