package com.austinlantz.papertrader.repository;

import com.austinlantz.papertrader.model.Trade;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Database access for {@link Trade} objects. Spring Data generates the
 * implementation at runtime, so no SQL needs to be written.
 *
 * @author Austin Lantz
 */
public interface TradeRepository extends JpaRepository<Trade, Long> {

    /**
     * Retrieves every saved trade, newest first.
     *
     * @return all trades ordered by id in descending order
     */
    List<Trade> findAllByOrderByIdDesc();
}
