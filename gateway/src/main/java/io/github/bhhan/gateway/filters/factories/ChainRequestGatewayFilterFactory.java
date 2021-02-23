package io.github.bhhan.gateway.filters.factories;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Created by hbh5274@gmail.com on 2021-02-23
 * Github : http://github.com/bhhan5274
 */

@Slf4j
@Component
public class ChainRequestGatewayFilterFactory extends AbstractGatewayFilterFactory<ChainRequestGatewayFilterFactory.Config> {

    private final WebClient client;

    public ChainRequestGatewayFilterFactory(WebClient client) {
        super(Config.class);
        this.client = client;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> client.get()
                .uri(config.getLanguageServiceEndpoint())
                .exchange()
                .flatMap(response -> (response.statusCode().is2xxSuccessful() ? response.bodyToMono(String.class) : Mono.just(config.getDefaultLanguage())))
                .map(Locale.LanguageRange::parse)
                .map(range -> {
                    exchange.getRequest()
                            .mutate()
                            .headers(h -> h.setAcceptLanguage(range));

                    String allOutgoingRequestLanguages = exchange.getRequest()
                            .getHeaders()
                            .getAcceptLanguage()
                            .stream()
                            .map(Locale.LanguageRange::getRange)
                            .collect(Collectors.joining(","));

                    log.info(String.format("Chain Request output - Request contains Accept-Language header: %s", allOutgoingRequestLanguages));

                    return exchange;
                })
                .flatMap(chain::filter);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("languageServiceEndpoint", "defaultLanguage");
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Config {
        private String languageServiceEndpoint;
        private String defaultLanguage;
    }
}
