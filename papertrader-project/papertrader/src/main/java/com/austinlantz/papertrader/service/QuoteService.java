package com.austinlantz.papertrader.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.math.BigDecimal;

/**
 * Looks up current stock prices from the Finnhub market data API.
 *
 * @author Austin Lantz
 */
@Service
public class QuoteService {

    /** HTTP client pointed at the Finnhub API. */
    private final RestClient client = RestClient.create("https://finnhub.io/api/v1");

    /** API key read from the {@code finnhub.api-key} property. */
    private final String apiKey;

    /**
     * Creates the service with the configured API key.
     *
     * @param apiKey the Finnhub API key, supplied by Spring from application.properties
     */
    public QuoteService(@Value("${finnhub.api-key}") String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Fetches the current price of a stock.
     *
     * @param symbol the ticker symbol to look up
     * @return the current price per share
     * @throws IllegalArgumentException if no price is found for the symbol
     */
    public BigDecimal getPrice(String symbol) {
        Quote quote = client.get()
                .uri("/quote?symbol={s}&token={t}", symbol, apiKey)
                .retrieve()
                .body(Quote.class);

        // Finnhub returns a price of 0 for symbols it doesn't recognize
        if (quote == null || quote.c() == null || quote.c().signum() == 0) {
            throw new IllegalArgumentException("Couldn't find a price for " + symbol);
        }
        return quote.c();
    }

    /**
     * The part of Finnhub's quote response this app uses.
     *
     * @param c the current price (named "c" in the response JSON)
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    record Quote(BigDecimal c) {}
}
