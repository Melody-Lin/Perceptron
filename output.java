package NN.hw1;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class output extends JFrame {

	static int size;
	static int trainFreq;
	static int testFreq;
	static float[][] trainData = new float[trainFreq][3];
	static float[][] testData = new float[testFreq][3];
	static int[] train_d = new int[trainFreq];
	static int[] test_d = new int[testFreq];
	static float[][] x = new float[size][3];
	static float[] w = new float[3];
	static int[] d = new int[size];
	static int[] E = new int[2];
	static float trainCR;
	static float testCR;
	static JFrame jf = new JFrame("Output");
	static boolean s3_train = false;
	static boolean s3_test = false;

	public output(float[][] x1, int[] d, float[][] trainData1, float[][] testData1, int trainFreq1, int testFreq1,
			int[] train_d1, int[] test_d1, float[] w1, int[] E1, float trainCR1, float testCR1) {
		trainData = trainData1;
		testData = testData1;
		trainFreq = trainFreq1;
		testFreq = testFreq1;
		train_d = train_d1;
		test_d = test_d1;
		w = w1;
		E = E1;
		trainCR = trainCR1;
		testCR = testCR1;

		jf.setSize(1200, 1000);
		jf.setLocationRelativeTo(null);
		jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JFreeChart chart = trainChart();
		ChartPanel panel = new ChartPanel(chart, true, true, true, false, true);
		panel.setPreferredSize(new java.awt.Dimension(500, 270));

		jf.setContentPane(panel);
		jf.setVisible(true);
	}

	public static JFreeChart trainChart() {
		// train
		XYDataset dataset1 = createTrainDataset();
		JFreeChart chart1 = ChartFactory.createXYLineChart("Train", "", "", dataset1, PlotOrientation.VERTICAL, true,
				false, false);

		XYPlot subplot1 = (XYPlot) chart1.getPlot();
		XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer();
		renderer1.setSeriesLinesVisible(0, false);
		renderer1.setSeriesShapesVisible(0, true);
		renderer1.setSeriesLinesVisible(1, false);
		renderer1.setSeriesShapesVisible(1, true);
		if (s3_train == true) {
			renderer1.setSeriesLinesVisible(2, false);
			renderer1.setSeriesShapesVisible(2, true);
		}
		renderer1.setSeriesLinesVisible(3, true);
		renderer1.setSeriesShapesVisible(3, false);
		subplot1.setRenderer(renderer1);
		// test
		XYDataset dataset2 = createTestDataset();
		JFreeChart chart2 = ChartFactory.createXYLineChart("Test", "", "", dataset2, PlotOrientation.VERTICAL, true,
				false, false);
		XYPlot subplot2 = (XYPlot) chart2.getPlot();
		XYLineAndShapeRenderer renderer2 = new XYLineAndShapeRenderer();
		renderer2.setSeriesLinesVisible(0, false);
		renderer2.setSeriesShapesVisible(0, true);
		renderer2.setSeriesLinesVisible(1, false);
		renderer2.setSeriesShapesVisible(1, true);
		if (s3_test == true) {
			renderer2.setSeriesLinesVisible(2, false);
			renderer2.setSeriesShapesVisible(2, true);
		}
		renderer2.setSeriesLinesVisible(3, true);
		renderer2.setSeriesShapesVisible(3, false);
		subplot2.setRenderer(renderer2);
		//
		CombinedDomainXYPlot plot = new CombinedDomainXYPlot();
		plot.add(subplot1, 1);
		plot.add(subplot2, 1);
		plot.setOrientation(PlotOrientation.VERTICAL);

		return new JFreeChart(
				"Train vs Test\n" + "w = { " + w[0] + ", " + w[1] + ", " + w[2] + " }\n" + "Train Correct Ratio: "
						+ trainCR + "% vs " + "Test Correct Ratio: " + testCR + "%\n",
				JFreeChart.DEFAULT_TITLE_FONT, plot, true) {
		};
	}

	private static XYDataset createTrainDataset() {
		XYSeries series1 = new XYSeries("Series 1");
		XYSeries series2 = new XYSeries("Series 2");
		XYSeries series3 = new XYSeries("Series 3");
		XYSeries series4 = new XYSeries("Series 4");
		float max_X = trainData[0][1]; // 程X
		float min_X = trainData[0][1]; // 程X
		float max_Y = trainData[0][2]; // 程Y
		float min_Y = trainData[0][2]; // 程Y

		for (int i = 0; i < trainFreq; i++) {
			if (train_d[i] == E[0]) {
				series1.add(trainData[i][1], trainData[i][2]);
			} else if (train_d[i] == E[1]) {
				series2.add(trainData[i][1], trainData[i][2]);
			} else {
				s3_train = true;
				series3.add(trainData[i][1], trainData[i][2]);
			}
			if (trainData[i][1] < min_X) {
				min_X = trainData[i][1];
			}
			if (trainData[i][1] > max_X) {
				max_X = trainData[i][1];
			}
		}

		float cal1 = 0, cal2 = 0;
		if (w[2] == 0) { // 絬
			cal1 = w[0] / w[1];
			for (int i = 0; i < trainFreq; i++) {
				if (trainData[i][2] < min_Y) {
					min_Y = trainData[i][2];
				} else if (trainData[i][2] > max_Y) {
					max_Y = trainData[i][2];
				}
			}
			series4.add(cal1, min_Y + 0.1);
			series4.add(cal1, max_Y + 0.1);
		} else {
			if (min_X != max_X) {
				cal1 = (0 - w[0] * (-1) - w[1] * min_X) / w[2];
				cal2 = (0 - w[0] * (-1) - w[1] * max_X) / w[2];
				series4.add(min_X, cal1);
				series4.add(max_X, cal2);
			} else {
				for (int i = 0; i < trainFreq; i++) {
					if (trainData[i][2] < min_Y) {
						min_Y = trainData[i][2];
					} else if (trainData[i][2] > max_Y) {
						max_Y = trainData[i][2];
					}
				}
				if (w[1] != 0) {
					cal1 = (0 - w[0] * (-1) - w[2] * min_Y) / w[1];
					cal2 = (0 - w[0] * (-1) - w[2] * max_Y) / w[1];
				} else {
					cal1 = 0 - w[0] * (-1) - w[2] * min_Y;
					cal2 = 0 - w[0] * (-1) - w[2] * max_Y;
				}
				series4.add(cal1, min_Y);
				series4.add(cal2, max_Y);
			}
		}

		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series1);
		dataset.addSeries(series2);
		if (s3_train == true) {
			dataset.addSeries(series3);
		}
		dataset.addSeries(series4);
		return dataset;
	}

	private static XYDataset createTestDataset() {
		XYSeries series1 = new XYSeries("Series 1");
		XYSeries series2 = new XYSeries("Series 2");
		XYSeries series3 = new XYSeries("Series 3");
		XYSeries series4 = new XYSeries("Series 4");
		float max_X = testData[0][1]; // 程X
		float min_X = testData[0][1]; // 程X
		float max_Y = testData[0][2]; // 程Y
		float min_Y = testData[0][2]; // 程Y

		for (int i = 0; i < testFreq; i++) {
			if (test_d[i] == E[0]) {
				series1.add(testData[i][1], testData[i][2]);
			} else if (test_d[i] == E[1]) {
				series2.add(testData[i][1], testData[i][2]);
			} else {
				s3_test = true;
				series3.add(testData[i][1], testData[i][2]);
			}
			if (testData[i][1] < min_X) {
				min_X = testData[i][1];
			}
			if (testData[i][1] > max_X) {
				max_X = testData[i][1];
			}
		}

		float cal1 = 0, cal2 = 0;
		if (w[2] == 0) { // 絬
			cal1 = w[0] / w[1];
			for (int i = 0; i < testFreq; i++) {
				if (testData[i][2] < min_Y) {
					min_Y = testData[i][2];
				} else if (testData[i][2] > max_Y) {
					max_Y = testData[i][2];
				}
			}
			series4.add(cal1, min_Y + 0.1);
			series4.add(cal1, max_Y + 0.1);
		} else {
			if (min_X != max_X) {
				cal1 = (0 - w[0] * (-1) - w[1] * min_X) / w[2];
				cal2 = (0 - w[0] * (-1) - w[1] * max_X) / w[2];
				series4.add(min_X, cal1);
				series4.add(max_X, cal2);
			} else {
				for (int i = 0; i < testFreq; i++) {
					if (testData[i][2] < min_Y) {
						min_Y = testData[i][2];
					} else if (testData[i][2] > max_Y) {
						max_Y = testData[i][2];
					}
				}
				if (w[1] != 0) {
					cal1 = (0 - w[0] * (-1) - w[2] * min_Y) / w[1];
					cal2 = (0 - w[0] * (-1) - w[2] * max_Y) / w[1];
				} else {
					cal1 = 0 - w[0] * (-1) - w[2] * min_Y;
					cal2 = 0 - w[0] * (-1) - w[2] * max_Y;
				}
				series4.add(cal1, min_Y);
				series4.add(cal2, max_Y);
			}
		}

		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series1);
		dataset.addSeries(series2);
		if (s3_test == true) {
			dataset.addSeries(series3);
		}
		dataset.addSeries(series4);
		return dataset;
	}

}
