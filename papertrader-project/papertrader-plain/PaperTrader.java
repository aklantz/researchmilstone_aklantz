import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A console-based practice trading app. The user enters a ticker symbol and
 * a number of shares, the program looks up the current price from the Finnhub
 * API, saves the trade to a CSV file, and can display the whole portfolio.
 *
 * @author Austin Lantz
 */
public class PaperTrader {

    /** File where trades are saved, one per line as symbol,shares,price. */
    static final Path FILE = Path.of("trades.csv");

    /**
     * Runs the menu loop. Requires the FINNHUB_API_KEY environment variable.
     *
     * @param args command-line arguments (not used)
     * @throws Exception if an unexpected error occurs
     */
    public static void main(String[] args) throws Exception {
        String key = System.getenv("FINNHUB_API_KEY");
        if (key == null || key.isBlank()) {
            System.out.println("Set the FINNHUB_API_KEY environment variable first.");
            return;
        }
        HttpClient client = HttpClient.newHttpClient();
        Scanner in = new Scanner(System.in);

        while (true) {
            System.out.println("\n1) Buy  2) Portfolio  3) Quit");
            System.out.print("> ");
            switch (in.nextLine().trim()) {
                case "1" -> buy(in, client, key);
                case "2" -> showPortfolio();
                case "3" -> { return; }
                default -> System.out.println("Pick 1, 2 or 3.");
            }
        }
    }

    /**
     * Prompts for a ticker and share count, validates them, looks up the
     * price, and saves the trade.
     *
     * @param in     the scanner reading user input
     * @param client the HTTP client used to call the price API
     * @param key    the Finnhub API key
     */
    static void buy(Scanner in, HttpClient client, String key) {
        System.out.print("Ticker: ");
        String symbol = in.nextLine().trim().toUpperCase();
        if (!symbol.matches("[A-Z.]{1,6}")) {
            System.out.println("Tickers are 1-6 letters.");
            return;
        }

        System.out.print("Shares: ");
        int shares;
        try {
            shares = Integer.parseInt(in.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Enter a whole number.");
            return;
        }
        if (shares < 1 || shares > 10000) {
            System.out.println("Shares must be between 1 and 10,000.");
            return;
        }

        try {
            BigDecimal price = getPrice(client, symbol, key);
            Files.writeString(FILE, symbol + "," + shares + "," + price + "\n",
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            System.out.printf("Bought %d %s at $%.2f = $%.2f%n",
                    shares, symbol, price, price.multiply(BigDecimal.valueOf(shares)));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Fetches the current price of a stock from the Finnhub API.
     *
     * @param client the HTTP client to send the request with
     * @param symbol the ticker symbol to look up
     * @param key    the Finnhub API key
     * @return the current price per share
     * @throws IOException          if the request fails or no price is found
     * @throws InterruptedException if the request is interrupted
     */
    static BigDecimal getPrice(HttpClient client, String symbol, String key)
            throws IOException, InterruptedException {
        HttpRequest req = HttpRequest.newBuilder(URI.create(
                "https://finnhub.io/api/v1/quote?symbol=" + symbol + "&token=" + key)).build();
        HttpResponse<String> res = client.send(req, HttpResponse.BodyHandlers.ofString());
        if (res.statusCode() != 200) {
            throw new IOException("Price service returned status " + res.statusCode());
        }
        // Finnhub's JSON looks like {"c":261.74,...}; "c" is the current price.
        Matcher m = Pattern.compile("\"c\":\\s*([0-9.]+)").matcher(res.body());
        if (!m.find()) throw new IOException("Unexpected response from price service");
        BigDecimal price = new BigDecimal(m.group(1));
        if (price.signum() == 0) throw new IOException("No price found for " + symbol);
        return price;
    }

    /**
     * Prints every saved trade and the total amount spent.
     *
     * @throws IOException if the trades file cannot be read
     */
    static void showPortfolio() throws IOException {
        if (!Files.exists(FILE)) {
            System.out.println("No trades yet.");
            return;
        }
        BigDecimal total = BigDecimal.ZERO;
        System.out.printf("%-8s %6s %10s %12s%n", "Ticker", "Shares", "Price", "Cost");
        for (String line : Files.readAllLines(FILE)) {
            String[] p = line.split(",");
            int shares = Integer.parseInt(p[1]);
            BigDecimal price = new BigDecimal(p[2]);
            BigDecimal cost = price.multiply(BigDecimal.valueOf(shares));
            total = total.add(cost);
            System.out.printf("%-8s %6d %10.2f %12.2f%n", p[0], shares, price, cost);
        }
        System.out.printf("Total spent: $%.2f%n", total);
    }
}
