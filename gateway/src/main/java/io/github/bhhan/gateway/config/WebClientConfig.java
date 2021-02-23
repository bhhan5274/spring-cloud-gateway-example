package io.github.bhhan.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Created by hbh5274@gmail.com on 2021-02-23
 * Github : http://github.com/bhhan5274
 */

@Configuration
public class WebClientConfig {
    @Bean
    public WebClient client(){
        return WebClient.builder()
                .build();
    }
}
