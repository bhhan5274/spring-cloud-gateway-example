package io.github.bhhan.gateway.filters.factories;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by hbh5274@gmail.com on 2021-02-23
 * Github : http://github.com/bhhan5274
 */

@Slf4j
@Component
public class ModifyRequestGatewayFilterFactory extends AbstractGatewayFilterFactory<ModifyRequestGatewayFilterFactory.Config> {

    public ModifyRequestGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("defaultLocale");
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if(exchange.getRequest()
                .getHeaders()
                .getAcceptLanguage()
                .isEmpty()){

                String queryParamLocale = exchange.getRequest()
                        .getQueryParams()
                        .getFirst("locale");

                Locale requestLocale = Optional.ofNullable(queryParamLocale)
                        .map(Locale::forLanguageTag)
                        .orElse(config.getDefaultLocale());

                exchange.getRequest()
                        .mutate()
                        .headers(h -> h.setAcceptLanguageAsLocales(Collections.singletonList(requestLocale)));
            }

            String allOutgoingRequestLanguages = exchange.getRequest()
                    .getHeaders()
                    .getAcceptLanguage()
                    .stream()
                    .map(Locale.LanguageRange::getRange)
                    .collect(Collectors.joining(","));

            log.info("Modify request output - Request contains Accept-Language header: {}", allOutgoingRequestLanguages);

            ServerWebExchange modifiedExchange = exchange.mutate()
                    .request(originalRequest -> originalRequest.uri(UriComponentsBuilder.fromUri(exchange.getRequest()
                            .getURI())
                            .replaceQueryParams(new LinkedMultiValueMap<>())
                            .build()
                            .toUri()))
                    .build();

            log.info("Removed all query params: {}", modifiedExchange.getRequest()
                    .getURI());

            return chain.filter(modifiedExchange);
        };
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Config {
        private Locale defaultLocale;
    }
}
