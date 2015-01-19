package hu.sebcsaba.gitrelocate;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class GitRelocateTest extends GraphTestData {
	
	@Test
	public void testAncestorAssertion() {
		GitRunner git = new MockedGitRunner(GR1_BRANCHES, GR1_TAGS, GR1_PARENTS);
		GraphBuilder builder = new GraphBuilder(git);
		GitSubGraph tree = builder.buildFullTree();
		Assert.assertTrue(hasAncestor(tree, "B0", "T1"));
		Assert.assertTrue(hasAncestor(tree, "B1", "T1"));
		Assert.assertTrue(hasAncestor(tree, "T0", "T1"));
		Assert.assertFalse(hasAncestor(tree, "B0", "T2"));
		
	}

	@Test
	public void testRelocate() {
		// given
		GitRunner git = new MockedGitRunner(GR1_BRANCHES, GR1_TAGS, GR1_PARENTS);
		GraphBuilder builder = new GraphBuilder(git);
		GitRelocate relocator = new GitRelocate(git, builder, PointerMode.MOVE, PointerMode.SKIP, false);
		
		// when
		CommitID cutPoint = MockedGitRunner.intToCommitID(1);
		CommitID newBase = MockedGitRunner.intToCommitID(8);
		relocator.relocate(cutPoint,newBase);
		
		// then
		GitSubGraph result = builder.buildFullTree();
		Assert.assertEquals(11, result.getCommits().size());
		Assert.assertTrue(hasAncestor(result, "B0", "T2"));
		Assert.assertTrue(hasAncestor(result, "B1", "T2"));
		Assert.assertFalse(hasAncestor(result, "T0", "T2"));
	}

	private boolean hasAncestor(GitSubGraph tree, String commit1, String commit2) {
		MockedGitRunner git = new MockedGitRunner(tree);
		CommitID commit1id = git.getCommitId(commit1);
		CommitID commit2id = git.getCommitId(commit2);
		Set<CommitID> ancestors = git.getAllAncestors(commit1id);
		return ancestors.contains(commit2id);
	}
	
}
