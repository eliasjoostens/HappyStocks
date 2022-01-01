package com.stocks.happystocks;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import java.io.IOException;
import java.util.Calendar;

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
    private MenuButton NumberOfDays;

    @FXML
    private LineChart<Number, Number > lineChart;

    @FXML
    void getResult(ActionEvent event) {
        try {
            Stock stock = YahooFinance.get(Tickr_code.getText());
            stock.print();

            /*NumberAxis xAxis = new NumberAxis();
            xAxis.setLabel("No of employees");

            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel("Revenue per employee");

            lineChart(xAxis, yAxis);*/

            XYChart.Series dataSeries1 = new XYChart.Series();
            dataSeries1.setName("2014");

            dataSeries1.getData().add(new XYChart.Data( 1, 567));
            dataSeries1.getData().add(new XYChart.Data( 5, 612));
            dataSeries1.getData().add(new XYChart.Data(10, 800));
            dataSeries1.getData().add(new XYChart.Data(20, 780));
            dataSeries1.getData().add(new XYChart.Data(40, 810));
            dataSeries1.getData().add(new XYChart.Data(80, 850));

            lineChart.getData().add(dataSeries1);

            /*Calendar today;
            Calendar from;
            today = Calendar.getInstance();
            today.set(Calendar.YEAR, 2021);
            today.set(Calendar.MONTH, 12);
            today.set(Calendar.DATE, 24);

            from = (Calendar) today.clone();
            from.add(Calendar.YEAR, -1);*/

            /*Stock tsla = YahooFinance.get("TSLA", from, today, Interval.DAILY);

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
