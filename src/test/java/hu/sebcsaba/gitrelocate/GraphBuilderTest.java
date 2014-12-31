package hu.sebcsaba.gitrelocate;

import org.junit.Assert;
import org.junit.Test;

public class GraphBuilderTest {

	/*
	 * 7=B0
	 * |\
	 * 5 6=B1
	 * | |
	 * 3 4=T0
	 * |/
	 * 2
	 * |
	 * 1
	 * |
	 * 0
	 */
	private static final int[][] GR1_PARENTS = new int[][]{
			new int[]{},
			new int[]{0},
			new int[]{1},
			new int[]{2},
			new int[]{2},
			new int[]{3},
			new int[]{4},
			new int[]{5,6},
	};
	private static final int[] GR1_BRANCHES = new int[]{7,6};
	private static final int[] GR1_TAGS = new int[]{4};
	
	@Test
	public void testEmptyGraph() {
		GitRunner git = new MockedGitRunner(new int[0], new int[0], new int[0][]);
		GraphBuilder builder = new GraphBuilder(git);
		GitSubGraph result = builder.build();
		Assert.assertTrue(result.getBranches().isEmpty());
		Assert.assertTrue(result.getTags().isEmpty());
		Assert.assertTrue(result.getCommits().isEmpty());
	}
	
	@Test
	public void testRealGraph() {
		GitRunner git = new MockedGitRunner(GR1_BRANCHES, GR1_TAGS, GR1_PARENTS);
		GraphBuilder builder = new GraphBuilder(git);
		GitSubGraph result = builder.build();
		Assert.assertEquals(GR1_BRANCHES.length, result.getBranches().size());
		Assert.assertEquals(GR1_TAGS.length, result.getTags().size());
		Assert.assertEquals(GR1_PARENTS.length, result.getCommits().size());
	}

}
