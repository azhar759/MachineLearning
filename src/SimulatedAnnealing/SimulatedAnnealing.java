package SimulatedAnnealing;

import java.lang.Math;
import java.io.File;
import jxl.Sheet;
import jxl.Workbook;

public class SimulatedAnnealing {

	public static int N = 15;
	public static String fileName = "Table.xls";
    public static String sheetName = "Sheet1";
    public static int[][] cells = new int[N][N];
    public static boolean hasReadFile = false;
	
	public static double getDistance(int location1, int location2) {
		if (!hasReadFile) {
			try {
				Workbook wb = Workbook.getWorkbook(new File(fileName));
				Sheet sheet = wb.getSheet(sheetName);

				for (int row = 1; row < N + 1; row++)
					for (int col = 1; col < N + 1; col++)
						cells[row - 1][col - 1] = Integer.parseInt(sheet
								.getCell(row, col).getContents());
				hasReadFile = true;

				for (int row = 0; row < N; row++) {
					for (int col = 0; col < N; col++)
						System.out.print(cells[row][col] + "\t");
					System.out.println();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return cells[location1][location2];
	}

	public static void main(String Args[]) {
		double temp = 100;
		double coolingRate = 0.15;
		int pathLocations[] = { 1, 0, 2, 4, 3, 7, 9, 10, 5, 6, 8, 11, 13, 12,
				14 };
		int noOfLocations = pathLocations.length;
		int newDistance;
		int distance = 0;
		for (int j = 0; j < noOfLocations - 1; j++) {
			distance += getDistance(pathLocations[j], pathLocations[j+1]);
		}

		while (temp > 1) {
			temp *= 1 - coolingRate;
			for (int i = 0; i < Math.floor(temp); i++) {
				int location1 = (int) (noOfLocations * Math.random());
				int location2 = (int) (noOfLocations * Math.random());

				int swap = pathLocations[location1];
				pathLocations[location1] = pathLocations[location2];
				pathLocations[location2] = swap;
			}

			newDistance = 0;
			for (int j = 0; j < noOfLocations - 1; j++)
				newDistance += getDistance(pathLocations[j], pathLocations[j+1]);

			if (newDistance < distance)
				distance = newDistance;
		}
		System.out.println("Shortest distance: " + distance);
	}
}
