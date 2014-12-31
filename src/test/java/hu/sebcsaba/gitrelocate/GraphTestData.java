package hu.sebcsaba.gitrelocate;

public class GraphTestData {
	
	/*
	 * 7=B0
	 * |\
	 * 5 6=B1
	 * | |
	 * 3 4=T0
	 * |/
	 * 2
	 * |
	 * 1=T1     8=T2
	 * |       /
	 * 0------/
	 */
	protected static final int[][] GR1_PARENTS = new int[][]{
				new int[]{},
				new int[]{0},
				new int[]{1},
				new int[]{2},
				new int[]{2},
				new int[]{3},
				new int[]{4},
				new int[]{5,6},
				new int[]{0},
		};
	protected static final int[] GR1_BRANCHES = new int[]{7,6};
	protected static final int[] GR1_TAGS = new int[]{4,1,8};

}