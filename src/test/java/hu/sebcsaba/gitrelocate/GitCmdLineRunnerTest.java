package hu.sebcsaba.gitrelocate;

import java.io.IOException;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Test;

public class GitCmdLineRunnerTest extends GitCmdLineTestBase {

	protected String getTmpFolderName() {
		return "git-relocate-cmlinerunner-test-data";
	}

	@Test
	public void testDetachedHeadBranches() throws IOException {
		tool.run("git", "init");
		doCommit(0);
		git.createBranch("B0");
		doCommit(1);
		CommitID middleCommit = git.getCommitId(git.getActualHeadName());
		doCommit(2);
		git.checkOut(middleCommit.toString());
		
		Assert.assertTrue(git.isDetachedHead());
		
		Collection<String> branches = git.getBranchNames();
		Assert.assertEquals(2, branches.size());
		Assert.assertTrue(branches.contains("B0"));
		Assert.assertTrue(branches.contains("master"));
	}
	
	@Test
	public void testGetCommitMessage() throws IOException {
		tool.run("git", "init");
		doCommit(0);
		CommitID id = git.getCommitId(git.getActualHeadName());
		Assert.assertEquals("commit-0", git.getCommitMessage(id));
	}
	
	@Test
	public void testCherryPickEmptyCommit() throws IOException {
		tool.run("git", "init");
		doCommit(0);
		git.createBranch("B0");
		doCommit(1);
		tool.run("git", "commit", "--allow-empty", "-m", "commit-2");
		CommitID emptyCommit = git.getCommitId(git.getActualHeadName());
		git.checkOut("B0");
		git.cherryPick(emptyCommit);
	}

}
