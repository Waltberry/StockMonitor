package com.example.stockmonitor;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.util.Duration;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

public class App extends Application {

    private static final int MAX_DATA_POINTS = 50;
    private Queue<Number> timeQueue = new LinkedList<>();
    private Queue<Number> priceQueue = new LinkedList<>();
    private XYChart.Series<Number, Number> series = new XYChart.Series<>();
    private int xSeriesData = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time");
        yAxis.setLabel("Stock Price");

        final LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setAnimated(false);
        lineChart.setTitle("Dow Jones Industrial Average Stock Price");

        series.setName("Stock Price Data");
        lineChart.getData().add(series);

        Scene scene = new Scene(lineChart, 800, 600);
        stage.setScene(scene);
        stage.show();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            try {
                addDataToSeries();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void addDataToSeries() throws IOException {
        Stock stock = YahooFinance.get("^DJI");
        double price = stock.getQuote().getPrice().doubleValue();
        Date date = new Date();
        String formattedDate = new SimpleDateFormat("HH:mm:ss").format(date);

        timeQueue.add(xSeriesData);
        priceQueue.add(price);

        series.getData().add(new XYChart.Data<>(xSeriesData, price));
        if (series.getData().size() > MAX_DATA_POINTS) {
            series.getData().remove(0);
        }
        xSeriesData++;
    }
}
