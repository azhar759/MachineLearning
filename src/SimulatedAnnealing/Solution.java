package SimulatedAnnealing;

public class Solution {
	public int distance;
	public int[] paths;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Distance = " + distance
				+ "; Path = ");
		for (int i = 0; i < paths.length - 1; i++) {
			sb.append(paths[i] + " - ");
		}
		sb.append(paths[paths.length - 1]);
		return sb.toString();
	}

	public Solution() {
	}

	public Solution(int distance, int[] paths) {
		this.distance = distance;
		this.paths = paths;
	}
}
