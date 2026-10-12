package com.austinlantz.papertrader.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * A single practice trade saved to the database. Each trade records the
 * ticker symbol, the number of shares, the price per share at the time of
 * the trade, and when it was executed.
 *
 * @author Austin Lantz
 */
@Entity
public class Trade {

    /** Database-generated unique identifier. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Ticker symbol of the stock, such as AAPL. */
    private String symbol;

    /** Number of shares bought. */
    private int shares;

    /** Price per share at the moment the trade was placed. */
    private BigDecimal price;

    /** Date and time the trade was executed. */
    private LocalDateTime executedAt;

    /** No-argument constructor required by JPA. Not intended for direct use. */
    protected Trade() {}

    /**
     * Creates a new trade executed at the current time.
     *
     * @param symbol the ticker symbol of the stock
     * @param shares the number of shares bought
     * @param price  the price per share
     */
    public Trade(String symbol, int shares, BigDecimal price) {
        this.symbol = symbol;
        this.shares = shares;
        this.price = price;
        this.executedAt = LocalDateTime.now();
    }

    /**
     * Calculates the total cost of this trade.
     *
     * @return the price per share multiplied by the number of shares
     */
    public BigDecimal getCost() {
        return price.multiply(BigDecimal.valueOf(shares));
    }

    /**
     * Returns the database identifier of this trade.
     *
     * @return the trade id, or {@code null} if it has not been saved yet
     */
    public Long getId() { return id; }

    /**
     * Returns the ticker symbol.
     *
     * @return the ticker symbol
     */
    public String getSymbol() { return symbol; }

    /**
     * Returns the number of shares.
     *
     * @return the number of shares bought
     */
    public int getShares() { return shares; }

    /**
     * Returns the price per share.
     *
     * @return the price per share at the time of the trade
     */
    public BigDecimal getPrice() { return price; }

    /**
     * Returns when the trade was executed.
     *
     * @return the execution date and time
     */
    public LocalDateTime getExecutedAt() { return executedAt; }
}
