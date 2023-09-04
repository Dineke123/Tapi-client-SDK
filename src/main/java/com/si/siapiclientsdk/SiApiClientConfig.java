package com.si.siapiclientsdk;

import com.si.siapiclientsdk.client.siApiClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ComponentScan
public class SiApiClientConfig {

    private String accessKey;

    private String secretKey;

    @Bean
    public siApiClient siApiClient() {
        return new siApiClient(accessKey, secretKey);
    }

}