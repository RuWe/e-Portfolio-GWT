package com.example.stockwatcher.client;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Stockwatcher implements EntryPoint {

	//Time in which the table is refreshed
	private static final int REFRESH_TIME = 10000;
	
	// needed panels
	// contains the stockTable
	private VerticalPanel vertPanel = new VerticalPanel();
	//contains the input widgets
	private HorizontalPanel horzPanel = new HorizontalPanel();
	
	// needed widgets
	private FlexTable stockTable = new FlexTable();
	private Button addStock = new Button("Add stock");
	private TextBox stockInput = new TextBox();
	private Label update = new Label();

	// Data structure
	private ArrayList<String> stocks = new ArrayList<String>();
	
	public void onModuleLoad() {
		// create Table 
		stockTable.setText(0, 0, "Stock symbol");
		stockTable.setText(0, 1, "Current price");
		stockTable.setText(0, 2, "Change (in percent)");
		stockTable.setText(0, 3, "Remove");
		
		stockTable.getRowFormatter().addStyleName(0, "watchListHeader");
		
		// Horizontal panel textbox and button on panel
		horzPanel.add(stockInput);
		horzPanel.add(addStock);
		
		// Vertical panel for table
		vertPanel.add(stockTable);
		vertPanel.add(horzPanel);
		vertPanel.add(update);
		
		// connect the HTML host page with the vertical panel
		RootPanel.get("stockList").add(vertPanel);
		
		// Set the focus in the input field
		stockInput.setFocus(true);
		
		Timer refresh = new Timer() {
			
			@Override
			public void run() {
				refreshStockTable();
			}
		};
		
		refresh.scheduleRepeating(REFRESH_TIME);
		
		
		// If the button is clicked or enter is pressed 
		// stock should be added
		addStock.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				addStock();
			}

		});
		
		stockInput.addKeyDownHandler(new KeyDownHandler() {
			
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {					
					addStock();
				}
			}
		});
	}
	
	private void refreshStockTable() {
		final double MAX_PRICE = 100.0;
		final double MAX_CHANGE = 0.02;
	
		StockPrice[] prices = new StockPrice[stocks.size()];
		for (int i = 0; i < stocks.size(); i++) {
	       double price = Random.nextDouble() * MAX_PRICE;
	       double change = price * MAX_CHANGE * (Random.nextDouble() * 2.0 - 1.0);

	       prices[i] = new StockPrice(stocks.get(i), price, change);
	     }

	     updateTable(prices);
	  }

	private void updateTable(StockPrice[] prices) {
		for (int i = 0; i < prices.length; i++) {
			updateTable(prices[i]);
		}
		
		// display timestamp for last updated
		DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat(
				DateTimeFormat.PredefinedFormat.DATE_TIME_MEDIUM);
		update.setText("Last updated: " + dateTimeFormat.format(new Date()));
	}

	private void updateTable(StockPrice stockPrice) {
		
		// if the stock doesn't exist do nothing
		if (!stocks.contains(stockPrice.getStockSymbol())) {
		       return;
		}

		// if it exists, show it in the format
		int row = stocks.indexOf(stockPrice.getStockSymbol()) + 1;
		
		String priceText = NumberFormat.getFormat("#,##00.0").format(stockPrice.getStockPrice());
		NumberFormat changePriceFormat = NumberFormat.getFormat("+#,##0.00; -#,##0.00");
		String changePriceText = changePriceFormat.format(stockPrice.getStockPriceChange());
		String changePercentText = changePriceFormat.format(stockPrice.getStockPriceChangePercent());

		stockTable.setText(row, 1, priceText);
		stockTable.setText(row, 2, changePriceText + " (" + changePercentText + "%)"); 	
	}

	private void addStock() {
		// Complete text from input field all uppercase and blancs are deleted
		final String stockSymbol = stockInput.getText().toUpperCase().trim();
		
		if (!stockSymbol.matches("^[0-9a-zA-Z\\.]{1,10}$")) {
			if (stockSymbol == "") {
				Window.alert("Symbol cannot be empty.");
				return;
			}
			Window.alert(stockSymbol + " is not a valid input.");
			stockInput.selectAll();
			return;
		}
		stockInput.setText("");
		
	
		if (stocks.contains(stockSymbol)){
			return;
		}
			
		int row = stockTable.getRowCount();
		stocks.add(stockSymbol);
		stockTable.setText(row, 0, stockSymbol);
		
		Button removeStock = new Button("x");
		removeStock.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				int removeStockIndex = stocks.indexOf(stockSymbol);
				stocks.remove(removeStockIndex);
				stockTable.removeRow(removeStockIndex + 1);
			}
		});
		stockTable.setWidget(row, 3, removeStock);
		
		//update of the price
		refreshStockTable();
	}
}
