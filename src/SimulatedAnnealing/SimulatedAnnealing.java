package SimulatedAnnealing;

import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.File;

import jxl.Sheet;
import jxl.Workbook;

public class SimulatedAnnealing {
	public static int noOfLocations = 15;
	public static int runs = 250;
	public static String fileName = "Table.xls";
	public static String sheetName = "Sheet1";
	public static int[][] cells = new int[noOfLocations][noOfLocations];
	public static boolean hasReadFile = false;
	public static double temperature = 100;
	public static double coolingRate = 0.15;
	public static double acceptanceMargin = 0.5;
	public static int maxTabuCount = 3;
	public static Random random = new Random();

	public static double getDistance(int location1, int location2) {
		if (!hasReadFile) {
			try {
				Workbook wb = Workbook.getWorkbook(new File(fileName));
				Sheet sheet = wb.getSheet(sheetName);

				for (int row = 1; row < noOfLocations + 1; row++)
					for (int col = 1; col < noOfLocations + 1; col++)
						cells[row - 1][col - 1] = Integer.parseInt(sheet
								.getCell(row, col).getContents());
				hasReadFile = true;
				// printTable(N);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return cells[location1][location2];
	}

	@SuppressWarnings("unused")
	private static void printTable(int n2) {
		for (int row = 0; row < noOfLocations; row++) {
			for (int col = 0; col < noOfLocations; col++)
				System.out.print(cells[row][col] + "\t");
			System.out.println();
		}
	}

	public static Solution generateSolution() {
		int pathOfLocations[] = new int[noOfLocations];
		ArrayList<Solution> solutions = new ArrayList<Solution>();
		int newDistance;
		int distance = 0;
		int tabuCount = 0;
		temperature = 100;

		// Biased locations, distance 1
		// B A I D N G E H M K O J L: 1 0 8 3 13 6 4 7 12 10 14 9 11
		// Biased locations, distance 2
		// C B F D K H G: 2, 1, 5, 3, 10, 7, 6
		List<Integer> listOfLocations = new ArrayList<Integer>();
		int temp = random.nextInt(noOfLocations);
		for (int i = 0; i < pathOfLocations.length; i++) {
			while (listOfLocations.contains(temp))
				temp = random.nextInt(noOfLocations);
			listOfLocations.add(temp);
			pathOfLocations[i] = temp;
		}

		int[] biasedLocations = new int[] {1, 0, 8, 3, 13, 6, 4, 7, 12, 10, 14, 9, 11};
		for (int i = 0; i < biasedLocations.length; i++) {
			pathOfLocations[i] = biasedLocations[i];
		}
		
		for (int j = 0; j < noOfLocations - 1; j++) {
			distance += getDistance(pathOfLocations[j], pathOfLocations[j + 1]);
		}
		solutions.add(new Solution(distance, pathOfLocations.clone()));

		while (temperature > 1) {
			temperature *= 1 - coolingRate;
			for (int i = 0; i < Math.floor(temperature); i++) {
				int location1 = random.nextInt(noOfLocations);
				int location2 = random.nextInt(noOfLocations);

				int swap = pathOfLocations[location1];
				pathOfLocations[location1] = pathOfLocations[location2];
				pathOfLocations[location2] = swap;
			}

			newDistance = 0;
			for (int j = 0; j < noOfLocations - 1; j++)
				newDistance += getDistance(pathOfLocations[j],
						pathOfLocations[j + 1]);

			if (newDistance < distance) {
				distance = newDistance;
				tabuCount = 0;
				solutions.add(new Solution(distance, pathOfLocations.clone()));
			} else if (newDistance * (1 + acceptanceMargin) < distance) {
				distance = newDistance;
				tabuCount++;
				solutions.add(new Solution(distance, pathOfLocations.clone()));
				if (tabuCount >= maxTabuCount) {
					while (tabuCount > 0) {
						solutions.remove(solutions.size() - 1);
						temperature *= 1 + coolingRate;
						tabuCount--;
					}
					distance = solutions.get(solutions.size() - 1).distance;
					pathOfLocations = solutions.get(solutions.size() - 1).paths
							.clone();
				}
			}
		}
		for (Solution solution : solutions) {
			System.out.println(solution);
		}
		return solutions.get(solutions.size() - 1);
	}

	public static void main(String Args[]) {
		Solution optimalSolution = null, tempSolution;
		for (int i = 1; i < runs+1; i++) {
			System.out.println("Run " + i);
			tempSolution = generateSolution();
			if (optimalSolution != null) {
				if (tempSolution.distance < optimalSolution.distance) {
					optimalSolution = tempSolution;
				}
			} else {
				optimalSolution = tempSolution;
			}
			System.out.println("\n");
		}
		System.out.println("Optimal solution in "+ runs +" runs:");
		System.out.println(optimalSolution);
	}
}
