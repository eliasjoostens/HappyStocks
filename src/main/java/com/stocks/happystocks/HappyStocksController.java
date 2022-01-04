package com.stocks.happystocks;

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
import javafx.scene.control.*;
import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

public class HappyStocksController {

    @FXML
    private TextField Tickr_code;

    @FXML
    private DatePicker StartDate;

    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private LineChart<Number, Number> LineChartTop;

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
            //Retrieve start date from datepicker
            Calendar today;
            today = Calendar.getInstance();
            LocalDate ld = StartDate.getValue();
            if (ld == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid date");
                alert.setHeaderText("Attention!");
                alert.setContentText("Please enter a valid date");
                alert.showAndWait();
                throw new Exception("Invalid date");
            }

            Calendar from = (Calendar) today.clone();
            from.set(Calendar.YEAR, ld.getYear());
            from.set(Calendar.MONTH, ld.getMonthValue() - 1);
            from.set(Calendar.DATE, ld.getDayOfMonth());

            //Invoke Yahoo API
            Stock stock = YahooFinance.get(Tickr_code.getText(), from, today, Interval.DAILY);
            if (stock == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Unexisting stock");
                alert.setHeaderText("Attention!");
                alert.setContentText("Please enter a valid TICKR code");
                alert.showAndWait();
                throw new Exception("Invalid tickr");
            }

            List<HistoricalQuote> stockHistory = stock.getHistory();

            //Show closing price chart
            XYChart.Series dataSeries1 = new XYChart.Series();
            dataSeries1.setName("Closing Price");

            for (int i = 0; i < stockHistory.size(); ++i) {
                dataSeries1.getData().add(new XYChart.Data(i, stockHistory.get(i).getClose()));
            }

            LineChartTop.getData().clear();
            LineChartTop.getData().add(dataSeries1);
            LineChartTop.setCreateSymbols(false);

            double[] closePrice = new double[stockHistory.size()];
            double[] out = new double[stockHistory.size()];
            MInteger begin = new MInteger();
            MInteger length = new MInteger();

            for (int i = 0; i < stockHistory.size(); i++) {
                closePrice[i] = (double) stockHistory.get(i).getClose().doubleValue();
            }

            int movingAveragePeriod = Integer.parseInt(comboBox.getValue());

            //Invoke talib for moving average calculation
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
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Look, an Error Dialog");
                alert.setContentText("Ooops, there was an error!");

                alert.showAndWait();


                System.out.println("Error");
            }

            //Invoke talib for rsi calculation
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

            //Show moving average chart
            XYChart.Series dataSeries2 = new XYChart.Series();
            dataSeries2.setName("Moving Average");

            for (int i = begin.value; i < stockHistory.size(); ++i) {
                dataSeries2.getData().add(new XYChart.Data(i, out[i - movingAveragePeriod + 1]));
            }
            LineChartTop.getData().add(dataSeries2);

            //Show RSI chart
            XYChart.Series dataSeries3 = new XYChart.Series();
            dataSeries3.setName("RSI");

            for (int i = movingAveragePeriod; i < stockHistory.size(); ++i) {
                dataSeries3.getData().add(new XYChart.Data(i, outrsi[i]));
            }

            //Add horizontal markers for 30 and 70 on RSI chart
            XYChart.Series dataSeriesRSI30 = new XYChart.Series();
            dataSeriesRSI30.getData().add(new XYChart.Data(0, 30));
            dataSeriesRSI30.getData().add(new XYChart.Data(stockHistory.size()-1, 30));

            XYChart.Series dataSeriesRSI70 = new XYChart.Series();
            dataSeriesRSI70.getData().add(new XYChart.Data(0, 70));
            dataSeriesRSI70.getData().add(new XYChart.Data(stockHistory.size()-1, 70));

            LineChartBottom.getData().clear();
            LineChartBottom.getData().add(dataSeries3);
            LineChartBottom.getData().add(dataSeriesRSI30);
            LineChartBottom.getData().add(dataSeriesRSI70);
            LineChartBottom.setCreateSymbols(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
