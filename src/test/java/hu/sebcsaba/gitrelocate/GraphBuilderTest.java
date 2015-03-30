package hu.sebcsaba.gitrelocate;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class GraphBuilderTest extends GraphTestData {

	@Test
	public void testEmptyGraph() {
		GitRunner git = new MockedGitRunner(new int[0], new int[0], new int[0][]);
		GraphBuilder builder = new GraphBuilder(git);
		GitSubGraph result = builder.buildFullTree();
		Assert.assertNotNull(result);
		Assert.assertTrue(result.getBranches().isEmpty());
		Assert.assertTrue(result.getTags().isEmpty());
		Assert.assertTrue(result.getCommits().isEmpty());
	}
	
	@Test
	public void testRealGraph() {
		GitRunner git = new MockedGitRunner(GR1_BRANCHES, GR1_TAGS, GR1_PARENTS);
		GraphBuilder builder = new GraphBuilder(git);
		GitSubGraph result = builder.buildFullTree();
		Assert.assertNotNull(result);
		Assert.assertEquals(GR1_BRANCHES.length, result.getBranches().size());
		Assert.assertEquals(GR1_TAGS.length, result.getTags().size());
		Assert.assertEquals(GR1_PARENTS.length, result.getCommits().size());
	}
	
	@Test
	public void testGraphSubtree() {
		GitRunner git = new MockedGitRunner(GR1_BRANCHES, GR1_TAGS, GR1_PARENTS);
		GraphBuilder builder = new GraphBuilder(git);
		GitSubGraph source = builder.buildFullTree();
		Set<CommitID> cutPoints = new HashSet<CommitID>();
		cutPoints.add(MockedGitRunner.intToCommitID(2));
		GitSubGraph result = builder.getSubTree(source, cutPoints);
		Assert.assertNotNull(result);
		Assert.assertEquals(6, result.getCommits().size());
		Assert.assertEquals(2, result.getBranches().size());
		Assert.assertEquals(1, result.getTags().size());
	}

}
