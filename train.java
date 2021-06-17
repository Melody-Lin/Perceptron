package NN.hw1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;

public class train {
	static int size = 0; // data size
	static float lr = 0; // learning rate 學習率
	static int cvg = 0; // convergence 收斂條件
	static int[] E = new int[2]; // 期望輸出值種類
	static String[] fileList = { "perceptron1", "perceptron2", "2Ccircle1", "2Circle1", "2Circle2", "2CloseS",
			"2CloseS2", "2CloseS3", "2cring", "2CS", "2Hcircle1", "2ring" };
	static String fileName = "";
	static float bestTrainAcc = -1; // 最佳正確率
	static float[] bestW = new float[3]; // 最佳正確率的鍵結值

	train(float r, int c, int fileIndex) throws FileNotFoundException {
		lr = r;
		cvg = c;
		fileName = fileList[fileIndex];
		dataSize();
		readFile();

	}

	public static void dataSize() throws FileNotFoundException {
		size = 0;
		File file = new File("Resource/" + fileName + ".txt");
		Scanner scanner = new Scanner(file);
		String tmp = "";
		while (scanner.hasNextLine()) {
			tmp = scanner.nextLine();
			size++;
		}
	}

	public static int sgn(float f) {
		if (f >= 0) {
			return E[0];
		} else {
			return E[1];
		}
	}

	public static void random(int size, int[] trainRand, int[] testRand) {
		int range = size * 2 / 3; // 2/3當訓練資料
		Random rand = new Random();
		int rdm[] = new int[size];
		ArrayList list = new ArrayList();
		for (int i = 0; i < size; i++) {
			list.add(i);
		}
		for (int i = 0; i < size; i++) {
			rdm[i] = -1;
		}
		int cnt = range;
		while (cnt > 0) {
			int index = (int) list.remove(rand.nextInt(list.size()));
			rdm[index] = 1; // 1: 隨機訓練資料, -1: 隨機測試資料
			cnt--;
		}
		int k = 0, t = 0;
		for (int i = 0; i < size; i++) {
			if (rdm[i] == 1) {
				trainRand[k] = i;
				k++;
			} else {
				testRand[t] = i;
				t++;
			}
		}
	}

	public static void setData(int trainFreq, int testFreq, float[][] x, float[][] trainData, float[][] testData,
			int[] d, int[] trainRand, int[] testRand, int[] train_d, int[] test_d) {
		// train data
		for (int i = 0; i < trainFreq; i++) {
			for (int j = 0; j < 3; j++) {
				trainData[i][j] = x[trainRand[i]][j];
			}
		}
		for (int i = 0; i < trainFreq; i++) {
			train_d[i] = d[trainRand[i]];
		}
		// test data
		for (int i = 0; i < testFreq; i++) {
			for (int j = 0; j < 3; j++) {
				testData[i][j] = x[testRand[i]][j];
			}
		}
		for (int i = 0; i < testFreq; i++) {
			test_d[i] = d[testRand[i]];
		}
	}

	public static float trainAccuracy(float[][] trainData, int[] train_d, int trainFreq, float[] w) {
		float num = 0;
		float trainAcc = 0;
		for (int i = 0; i < trainFreq; i++) {
			for (int j = 0; j < 3; j++) {
				num += trainData[i][j] * w[j];
			}
			if (sgn(num) == train_d[i]) {
				trainAcc++;
			}
			num = 0;
		}
		return (trainAcc / trainFreq) * 100;
	}

	public static float testAccuracy(float[][] testData, int[] test_d, int testFreq, float[] w) {
		float num = 0;
		float testAcc = 0;
		for (int i = 0; i < testFreq; i++) {
			for (int j = 0; j < 3; j++) {
				num += testData[i][j] * w[j];
			}
			if (sgn(num) == test_d[i]) {
				testAcc++;
			}
			num = 0;
		}
		return (testAcc / testFreq) * 100;
	}

	public static void training(float[][] trainData, int[] train_d, float[] w, float r, int trainFreq) {
		int n = 0;
		int totalFreq = 0;
		float num = 0;

		while (totalFreq < cvg) {
			while (n < trainFreq) {
				for (int i = 0; i < 3; i++) {
					num += w[i] * trainData[n][i];
				}
				int y = sgn(num);

				if (y > train_d[n]) {
					for (int i = 0; i < 3; i++) {
						w[i] = w[i] - r * trainData[n][i];
					}

				} else if (y < train_d[n]) {
					for (int i = 0; i < 3; i++) {
						w[i] = w[i] + r * trainData[n][i];
					}
				}
				n++;
				num = 0;
			}
			totalFreq++;
			n = 0;
			//
			float y = 0;
			float trainAcc = 0;
			for (int i = 0; i < trainFreq; i++) {
				for (int j = 0; j < 3; j++) {
					y += trainData[i][j] * w[j];
				}
				if (sgn(y) == train_d[i]) {
					trainAcc++;
				}
				y = 0;
			}
			if (bestTrainAcc < (trainAcc / trainFreq * 100)) {
				bestTrainAcc = trainAcc / trainFreq * 100;
				bestW = w;
			}

		}

	}

	public static void readFile() throws FileNotFoundException {
		int trainFreq = size * 2 / 3; // training frequency訓練次數
		int testFreq = size - trainFreq; // test frequency測試次數
		float[][] x = new float[size][3]; // 輸入資料
		float[][] trainData = new float[trainFreq][3]; // 訓練資料
		float[][] testData = new float[testFreq][3]; // 測試資料
		int[] trainRand = new int[trainFreq]; // 隨機取出之訓練資料
		int[] testRand = new int[testFreq]; // 隨機取出之測試資料
		int[] d = new int[size]; // 期望輸出值
		float[] w = { -1, 0, 1 }; // weight
		int[] train_d = new int[trainFreq]; // 訓練資料的期望輸出
		int[] test_d = new int[testFreq]; // 測試資料的期輸出
		//
		String tmp = "";
		int row = 0;
		File file = new File("Resource/" + fileName + ".txt");
		Scanner scanner = new Scanner(file);
		while (scanner.hasNextLine()) {
			if (row == size) {
				break;
			}
			tmp = scanner.nextLine();
			String[] arr = tmp.split(" ");
			for (int i = 1, j = 0; i < 3; i++, j++) {
				x[row][i] = Float.parseFloat(arr[j]);
			}
			d[row] = Integer.valueOf(arr[2]);
			row++;
		}
		for (int i = 0; i < size; i++) {
			x[i][0] = -1;
		}
		// 期望值
		for (int i = 1; i < size; i++) {
			if (d[i] > d[i - 1]) {
				E[0] = d[i];
				E[1] = d[i - 1];
				break;
			} else if (d[i] < d[i - 1]) {
				E[0] = d[i - 1];
				E[1] = d[i];
				break;
			}
		}
		//
		random(size, trainRand, testRand);
		setData(trainFreq, testFreq, x, trainData, testData, d, trainRand, testRand, train_d, test_d);
		training(trainData, d, w, lr, trainFreq);
		output op = new output(x, d, trainData, testData, trainFreq, testFreq, train_d, test_d, bestW, E,
				trainAccuracy(trainData, train_d, trainFreq, bestW), testAccuracy(testData, test_d, testFreq, bestW));
	}

}
