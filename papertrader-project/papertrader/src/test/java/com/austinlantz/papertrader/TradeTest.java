package com.austinlantz.papertrader;

import com.austinlantz.papertrader.model.Trade;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the {@link Trade} class.
 *
 * @author Austin Lantz
 */
class TradeTest {

    /**
     * Verifies that a trade's cost equals its price multiplied by its shares.
     */
    @Test
    void costIsPriceTimesShares() {
        Trade t = new Trade("AAPL", 3, new BigDecimal("10.50"));
        assertEquals(new BigDecimal("31.50"), t.getCost());
    }
}
