package com.stocks.happystocks;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
    void getResult(ActionEvent event) {
        try {
            Stock stock = YahooFinance.get(Tickr_code.getText());
            stock.print();

            Calendar today;
            Calendar from;
            today = Calendar.getInstance();
            today.set(Calendar.YEAR, 2021);
            today.set(Calendar.MONTH, 12);
            today.set(Calendar.DATE, 24);

            from = (Calendar) today.clone();
            from.add(Calendar.YEAR, -1);

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
