package project.emergencyApplication.auth.config;

import feign.Client;
import feign.Feign;
import feign.Logger;
import feign.Retryer;
import feign.httpclient.ApacheHttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    public Client feignClient() {
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build();
        return new ApacheHttpClient(httpClient);
    }

    @Bean
    public Feign.Builder feignBuilder() {
        return Feign.builder()
                .client(feignClient())
                .logger(new Logger.ErrorLogger())
                .logLevel(Logger.Level.BASIC)
                .retryer(new Retryer.Default());
    }
}
