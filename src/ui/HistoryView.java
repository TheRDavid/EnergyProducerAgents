package ui;
import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JFrame;

import agents.Community;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import main.Simulation;

public class HistoryView extends JFrame {
	private JFXPanel fxPanel = new JFXPanel();
	private XYChart.Series[] chartData;
	final LineChart<Number, Number> moneySpentChart = new LineChart<Number, Number>(new NumberAxis(), new NumberAxis());
	final LineChart<Number, Number> moneyEarnedChart = new LineChart<Number, Number>(new NumberAxis(),
			new NumberAxis());
	final LineChart<Number, Number> moneyBalanceChart = new LineChart<Number, Number>(new NumberAxis(),
			new NumberAxis());

	final LineChart<Number, Number> energyBoughtChart = new LineChart<Number, Number>(new NumberAxis(),
			new NumberAxis());
	final LineChart<Number, Number> energySoldChart = new LineChart<Number, Number>(new NumberAxis(), new NumberAxis());
	final LineChart<Number, Number> energyBalanceChart = new LineChart<Number, Number>(new NumberAxis(),
			new NumberAxis());

	private final List<double[]> data;

	public HistoryView(List<double[]> dataEntries) {
		data = dataEntries;
		add(fxPanel, BorderLayout.CENTER);
		setSize(1650, 960);
		setVisible(true);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				initFX(fxPanel);
				readData();
			}

		});
	}

	private void readData() {
		double currentTotalHour = 0;
		for (double[] dataRow : data) {
			for (int i = 0; i < Simulation.currentSimulation.getCommunities().length; i++) {
				energyBoughtChart.getData().get(i).getData()
						.add(new Data<Number, Number>(currentTotalHour, dataRow[i * 6 + History.W_BOUGHT]));
				energySoldChart.getData().get(i).getData()
						.add(new Data<Number, Number>(currentTotalHour, dataRow[i * 6 + History.W_SOLD]));
				moneySpentChart.getData().get(i).getData()
						.add(new Data<Number, Number>(currentTotalHour, dataRow[i * 6 + History.CT_SPENT]));
				moneyEarnedChart.getData().get(i).getData()
						.add(new Data<Number, Number>(currentTotalHour, dataRow[i * 6 + History.CT_EARNED]));

				moneyBalanceChart.getData().get(i).getData()
						.add(new Data<Number, Number>(currentTotalHour, dataRow[i * 6 + History.CT_BALANCE]));
				energyBalanceChart.getData().get(i).getData()
						.add(new Data<Number, Number>(currentTotalHour, dataRow[i * 6 + History.W_BALANCE]));
			}
			currentTotalHour++;
		}
	}

	private void initFX(JFXPanel p) {
		Scene scene = createScene();
		fxPanel.setScene(scene);
	}

	private Scene createScene() {
		GridPane grid = new GridPane();
		grid.setVgap(15);
		grid.setHgap(15);

		grid.setPadding(new Insets(10, 10, 10, 10));
		moneyBalanceChart.setTitle("€ Balance");
		moneyBalanceChart.setAnimated(true);
		moneyBalanceChart.setCreateSymbols(false);
		moneyEarnedChart.setTitle("€ Earned");
		moneyEarnedChart.setCreateSymbols(false);
		moneyEarnedChart.setAnimated(true);
		moneySpentChart.setTitle("€ Spent");
		moneySpentChart.setCreateSymbols(false);
		moneySpentChart.setAnimated(true);

		energyBalanceChart.setTitle("E Balance");
		energyBalanceChart.setCreateSymbols(false);
		energyBalanceChart.setAnimated(true);
		energyBoughtChart.setTitle("E Bought");
		energyBoughtChart.setCreateSymbols(false);
		energyBoughtChart.setAnimated(true);
		energySoldChart.setTitle("E Sold");
		energySoldChart.setCreateSymbols(false);
		energySoldChart.setAnimated(true);

		grid.add(energyBoughtChart, 0, 0);
		grid.add(energySoldChart, 1, 0);
		grid.add(energyBalanceChart, 2, 0);

		grid.add(moneySpentChart, 0, 1);
		grid.add(moneyEarnedChart, 1, 1);
		grid.add(moneyBalanceChart, 2, 1);

		int numCommunities = Simulation.currentSimulation.getCommunities().length;
		chartData = new XYChart.Series[numCommunities * 6];
		for (int i = 0; i < numCommunities; i++) {
			Community c = Simulation.currentSimulation.getCommunities()[i];
			XYChart.Series s = new XYChart.Series();
			s.setName(c.getStrategy().toString());
			moneyBalanceChart.getData().add(s);

			s = new XYChart.Series();
			s.setName(c.getStrategy().toString());
			moneyEarnedChart.getData().add(s);
			
			s = new XYChart.Series();
			s.setName(c.getStrategy().toString());
			moneySpentChart.getData().add(s);

			s = new XYChart.Series();
			s.setName(c.getStrategy().toString());
			energyBalanceChart.getData().add(s);

			s = new XYChart.Series();
			s.setName(c.getStrategy().toString());
			energyBoughtChart.getData().add(s);

			s = new XYChart.Series();
			s.setName(c.getStrategy().toString());
			energySoldChart.getData().add(s);
		}
		Scene scene = new Scene(grid, Color.ALICEBLUE);
		scene.getStylesheets().add("style.css");
		return scene;
	}
}
