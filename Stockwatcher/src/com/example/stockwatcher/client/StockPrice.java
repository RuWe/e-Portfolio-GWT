package com.example.stockwatcher.client;


public class StockPrice {
	
	private String stockSymbol;
	private double stockPrice;
	private double stockPriceChange;
	  
	public StockPrice() {
		  
	}
	  
	public StockPrice(String stockSymbol, double stockPrice, double stockPriceChange) {
		super();
		this.stockSymbol = stockSymbol;
		this.stockPrice = stockPrice;
		this.stockPriceChange = stockPriceChange;
	}

	public String getStockSymbol() {
		return stockSymbol;
	}

	public void setStockSymbol(String stockSymbol) {
		this.stockSymbol = stockSymbol;
	}

	public double getStockPrice() {
		return stockPrice;
	}

	public void setStockPrice(double stockPrice) {
		this.stockPrice = stockPrice;
	}

	public double getStockPriceChange() {
		return stockPriceChange;
	}

	public void setStockPriceChange(double stockPriceChange) {
		this.stockPriceChange = stockPriceChange;
	}
	
	public double getStockPriceChangePercent() {
		return 100.0 * this.stockPriceChange / this.stockPrice;
	}
	  	 
}
