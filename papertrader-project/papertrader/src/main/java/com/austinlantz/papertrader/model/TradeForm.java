package com.austinlantz.papertrader.model;

import jakarta.validation.constraints.*;

/**
 * Holds the data a user types into the trade form and defines the
 * validation rules applied before a trade is placed.
 *
 * @author Austin Lantz
 */
public class TradeForm {

    /** Ticker symbol entered by the user. Must be 1-6 letters. */
    @NotBlank(message = "Enter a ticker symbol")
    @Pattern(regexp = "^[A-Za-z.]{1,6}$", message = "Tickers are 1-6 letters")
    private String symbol;

    /** Number of shares entered by the user. Must be between 1 and 10,000. */
    @NotNull(message = "Enter a number of shares")
    @Min(value = 1, message = "Buy at least 1 share")
    @Max(value = 10000, message = "Maximum is 10,000 shares")
    private Integer shares;

    /**
     * Returns the ticker symbol entered by the user.
     *
     * @return the ticker symbol
     */
    public String getSymbol() { return symbol; }

    /**
     * Sets the ticker symbol.
     *
     * @param symbol the ticker symbol to set
     */
    public void setSymbol(String symbol) { this.symbol = symbol; }

    /**
     * Returns the number of shares entered by the user.
     *
     * @return the number of shares
     */
    public Integer getShares() { return shares; }

    /**
     * Sets the number of shares.
     *
     * @param shares the number of shares to set
     */
    public void setShares(Integer shares) { this.shares = shares; }
}
