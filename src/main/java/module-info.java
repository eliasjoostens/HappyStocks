module com.stocks.happystocks {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires YahooFinanceAPI;
    requires ta.lib;

    opens com.stocks.happystocks to javafx.fxml;
    exports com.stocks.happystocks;
}