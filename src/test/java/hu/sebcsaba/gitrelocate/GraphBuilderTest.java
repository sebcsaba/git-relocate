package hu.sebcsaba.gitrelocate;

import org.junit.Assert;
import org.junit.Test;

public class GraphBuilderTest {

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
		final int[][] parents = new int[][]{
				new int[]{},
				new int[]{0},
				new int[]{1},
				new int[]{2},
				new int[]{2},
				new int[]{3},
				new int[]{4},
				new int[]{5,6},
		};
		final int[] branches = new int[]{7,6};
		final int[] tags = new int[]{4};
		
		GitRunner git = new MockedGitRunner(branches, tags, parents);
		GraphBuilder builder = new GraphBuilder(git);
		GitSubGraph result = builder.build();
		Assert.assertEquals(branches.length, result.getBranches().size());
		Assert.assertEquals(tags.length, result.getTags().size());
		Assert.assertEquals(parents.length, result.getCommits().size());
	}

}
