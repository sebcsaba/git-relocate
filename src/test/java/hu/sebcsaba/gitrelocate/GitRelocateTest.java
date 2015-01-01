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

	private boolean hasAncestor(GitSubGraph tree, String commit1, String commit2) {
		MockedGitRunner git = new MockedGitRunner(tree);
		CommitID commit1id = git.getCommitId(commit1);
		CommitID commit2id = git.getCommitId(commit2);
		Set<CommitID> ancestors = git.getAllAncestors(commit1id);
		return ancestors.contains(commit2id);
	}
	
}
