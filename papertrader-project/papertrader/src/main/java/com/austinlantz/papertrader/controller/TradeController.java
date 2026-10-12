package com.austinlantz.papertrader.controller;

import com.austinlantz.papertrader.model.Trade;
import com.austinlantz.papertrader.model.TradeForm;
import com.austinlantz.papertrader.repository.TradeRepository;
import com.austinlantz.papertrader.service.QuoteService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Handles web requests for the trading page: showing the form and
 * portfolio, and processing submitted trades.
 *
 * @author Austin Lantz
 */
@Controller
public class TradeController {

    /** Service used to look up current stock prices. */
    private final QuoteService quoteService;

    /** Repository used to save and load trades. */
    private final TradeRepository repo;

    /**
     * Creates the controller with its dependencies.
     *
     * @param quoteService the service that fetches stock prices
     * @param repo         the repository that stores trades
     */
    public TradeController(QuoteService quoteService, TradeRepository repo) {
        this.quoteService = quoteService;
        this.repo = repo;
    }

    /**
     * Shows the home page with an empty trade form and the current portfolio.
     *
     * @param model the model used to pass data to the page
     * @return the name of the template to render
     */
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("tradeForm", new TradeForm());
        addPortfolio(model);
        return "index";
    }

    /**
     * Validates a submitted trade, looks up the price, and saves the trade.
     * On success the browser is redirected to the home page so refreshing
     * does not submit the trade twice. On failure the form is shown again
     * with error messages.
     *
     * @param form   the data entered by the user
     * @param result the validation results, including any errors
     * @param model  the model used to pass data to the page
     * @return a redirect on success, or the template name to redisplay the form
     */
    @PostMapping("/trade")
    public String placeTrade(@Valid @ModelAttribute("tradeForm") TradeForm form,
                             BindingResult result, Model model) {
        if (!result.hasErrors()) {
            try {
                String symbol = form.getSymbol().trim().toUpperCase();
                BigDecimal price = quoteService.getPrice(symbol);
                repo.save(new Trade(symbol, form.getShares(), price));
                return "redirect:/";
            } catch (IllegalArgumentException e) {
                result.reject("quote.notfound", e.getMessage());
            } catch (RestClientException e) {
                result.reject("quote.unavailable",
                        "Price service unavailable. Check your API key or try again shortly.");
            }
        }
        addPortfolio(model);
        return "index";
    }

    /**
     * Adds the list of trades and the total amount spent to the model.
     *
     * @param model the model to add the portfolio data to
     */
    private void addPortfolio(Model model) {
        List<Trade> trades = repo.findAllByOrderByIdDesc();
        BigDecimal total = trades.stream()
                .map(Trade::getCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        model.addAttribute("trades", trades);
        model.addAttribute("total", total);
    }
}
