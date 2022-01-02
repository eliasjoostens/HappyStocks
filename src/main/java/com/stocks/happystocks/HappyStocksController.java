package com.stocks.happystocks;

import java.io.IOException;
import java.lang.String;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;


public class HappyStocksController {

    @FXML
    private AnchorPane AnchorPaneLeft;

    @FXML
    private TextField Tickr_code;

    @FXML
    private DatePicker StartDate;

    @FXML
    private Button GoButton;

    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private AnchorPane AnchorPaneRightTop;

    @FXML
    private LineChart<Number, Number> LineChartTop;

    @FXML
    private NumberAxis YAxisTop;

    @FXML
    private AnchorPane AnchorPaneRightBottom;

    @FXML
    private LineChart<Number, Number> LineChartBottom;

    @FXML
    private NumberAxis YAxisBottom;

    public void initialize() {

        YAxisBottom.setAutoRanging(false);
        YAxisBottom.setLowerBound(0);
        YAxisBottom.setUpperBound(100);
        YAxisBottom.setTickUnit(25);

    }

    @FXML
    void refreshCharts(ActionEvent event) {
        try {
            initialize();
            Calendar today;
            today = Calendar.getInstance();

            LocalDate ld = StartDate.getValue();

            Calendar from = (Calendar) today.clone();
            from.set(Calendar.YEAR, ld.getYear());
            from.set(Calendar.MONTH, ld.getMonthValue() - 1);
            from.set(Calendar.DATE, ld.getDayOfMonth());

            int y = today.get(Calendar.YEAR);
            int m = today.get(Calendar.MONTH);
            int d = today.get(Calendar.DAY_OF_MONTH);

            System.out.println("year: " + y + "month: " + m + "day: " + d);

            Stock stock = YahooFinance.get(Tickr_code.getText(), from, today, Interval.DAILY);

            List<HistoricalQuote> stockHistory = stock.getHistory();

            System.out.println(stockHistory);

            XYChart.Series dataSeries1 = new XYChart.Series();
            dataSeries1.setName("Closing Price");

            for (int i = 0; i < stockHistory.size(); ++i) {
                dataSeries1.getData().add(new XYChart.Data(i, stockHistory.get(i).getClose()));
            }

            LineChartTop.getData().clear();
            LineChartTop.getData().add(dataSeries1);

            double[] closePrice = new double[stockHistory.size()];
            double[] out = new double[stockHistory.size()];
            MInteger begin = new MInteger();
            MInteger length = new MInteger();

            for (int i = 0; i < stockHistory.size(); i++) {
                closePrice[i] = (double) stockHistory.get(i).getClose().doubleValue();
            }

            int movingAveragePeriod = Integer.parseInt(comboBox.getValue());

            Core c = new Core();
            RetCode retCode = c.sma(0, closePrice.length - 1, closePrice, movingAveragePeriod, begin, length, out);

            if (retCode == RetCode.Success) {
                System.out.println("Output Start Period: " + begin.value);
                System.out.println("Output End Period: " + (begin.value + length.value - 1));

                for (int i = begin.value; i < begin.value + length.value; i++) {
                    StringBuilder line = new StringBuilder();
                    line.append("Period #");
                    line.append(i);
                    line.append(" close=");
                    line.append(closePrice[i]);
                    line.append(" mov_avg=");
                    line.append(out[i - begin.value]);
                    System.out.println(line);
                }
            } else {
                System.out.println("Error");
            }

            double[] outrsi = new double[closePrice.length];
            double[] tempOutPut = new double[closePrice.length];
            begin.value = -1;
            length.value = -1;
            retCode = c.rsi(0, closePrice.length - 1, closePrice, movingAveragePeriod, begin, length, tempOutPut);
            for (int i = 0; i < movingAveragePeriod; i++) {
                outrsi[i] = 0;
            }
            for (int i = movingAveragePeriod; 0 < i && i < (closePrice.length); i++) {
                outrsi[i] = tempOutPut[i - movingAveragePeriod];
            }

            XYChart.Series dataSeries2 = new XYChart.Series();
            dataSeries2.setName("Moving Average");

            for (int i = begin.value; i < stockHistory.size(); ++i) {
                System.out.println(i);
                dataSeries2.getData().add(new XYChart.Data(i, out[i - movingAveragePeriod + 1]));
            }

            LineChartTop.getData().add(dataSeries2);

            XYChart.Series dataSeries3 = new XYChart.Series();
            dataSeries3.setName("RSI");

            for (int i = 0; i < stockHistory.size(); ++i) {
                System.out.println(i);
                dataSeries3.getData().add(new XYChart.Data(i, outrsi[i]));
            }
            LineChartBottom.getData().clear();
            LineChartBottom.getData().add(dataSeries3);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
