import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class App {
    private static final String SYMBOL = "^DJI";
    private static final Queue<StockData> stockDataQueue = new LinkedList<>();

    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    Stock stock = YahooFinance.get(SYMBOL);
                    double price = stock.getQuote().getPrice().doubleValue();
                    long timestamp = System.currentTimeMillis();
                    stockDataQueue.add(new StockData(price, timestamp));

                    System.out.println("Stock price: " + price + " at " + timestamp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 5000); // 0 delay, 5000ms period (5 seconds)
    }
}

class StockData {
    private final double price;
    private final long timestamp;

    public StockData(double price, long timestamp) {
        this.price = price;
        this.timestamp = timestamp;
    }

    public double getPrice() {
        return price;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
