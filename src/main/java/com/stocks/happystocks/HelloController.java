package com.stocks.happystocks;

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

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;



public class HelloController {

    @FXML
    private TextField Tickr_code;

    @FXML
    private DatePicker StartDate;

    @FXML
    private Button GoButton;

    @FXML
    private ComboBox<String> comboBox;


    @FXML
    private LineChart<Number, Number > lineChart;

    @FXML
    void getResult(ActionEvent event) {
        try {
            LocalDate ld = StartDate.getValue();
            Calendar from =  Calendar.getInstance();
            from.set(ld.getYear(), ld.getMonthValue(), ld.getDayOfMonth());

            Calendar today;
            today = Calendar.getInstance();
            today.set(Calendar.YEAR, 2021);
            today.set(Calendar.MONTH, 12);
            today.set(Calendar.DATE, 24);



            from = (Calendar) today.clone();
            from.add(Calendar.YEAR, -1);


            Stock stock = YahooFinance.get(Tickr_code.getText(), from, today, Interval.DAILY);

            List<HistoricalQuote> stockHistory = stock.getHistory();

            XYChart.Series dataSeries1 = new XYChart.Series();
            dataSeries1.setName("Closing Price");

            for (int i=0; i<stockHistory.size(); ++i) {
                dataSeries1.getData().add(new XYChart.Data(i, stockHistory.get(i).getClose()));
            }

            lineChart.getData().clear();
            lineChart.getData().add(dataSeries1);

            double[] closePrice = new double[stockHistory.size()];
            double[] out = new double[stockHistory.size()];
            MInteger begin = new MInteger();
            MInteger length = new MInteger();

            for (int i = 0; i < stockHistory.size(); i++) {
                closePrice[i] = (double) stockHistory.get(i).getClose().doubleValue();
            }

            Core c = new Core();
            RetCode retCode = c.sma(0, closePrice.length - 1, closePrice, Integer.parseInt(comboBox.getValue()), begin, length, out);

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
                    System.out.println(line.toString());
                }
            } else {
                System.out.println("Error");
            }


            XYChart.Series dataSeries2 = new XYChart.Series();
            dataSeries2.setName("Moving Average");

            for (int i=0; i<stockHistory.size(); ++i) {
                dataSeries2.getData().add(new XYChart.Data(i, out[i]));
            }

            lineChart.getData().add(dataSeries2);

            /*Calendar today;
            Calendar from;
            today = Calendar.getInstance();
            today.set(Calendar.YEAR, 2021);
            today.set(Calendar.MONTH, 12);
            today.set(Calendar.DATE, 24);

            from = (Calendar) today.clone();
            from.add(Calendar.YEAR, -1);

            Stock tsla = YahooFinance.get(, from, today, Interval.DAILY);

            System.out.println(tsla.getHistory());

            for (HistoricalQuote histQuote : tsla.getHistory()) {

                System.out.println(histQuote.getClose());
                System.out.println("Date: " + histQuote.getDate().get(Calendar.DATE));
                System.out.println("Month: " + histQuote.getDate().get(Calendar.MONTH));
                System.out.println("Year: " + histQuote.getDate().get(Calendar.YEAR));

            }*/

        } catch (IOException e) {
            e.printStackTrace();
        }



        /*System.out.println("Hallo");

        double[] closePrice = new double[100];
        double[] out = new double[100];
        MInteger begin = new MInteger();
        MInteger length = new MInteger();

        for (int i = 0; i < closePrice.length; i++) {
            closePrice[i] = (double) i;
        }

        Core c = new Core();
        RetCode retCode = c.sma(0, closePrice.length - 1, closePrice, 30, begin, length, out);


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
                System.out.println(line.toString());
            }
        } else {
            System.out.println("Error");
        }*/
    }
}
