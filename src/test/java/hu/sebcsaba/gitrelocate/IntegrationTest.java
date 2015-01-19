package hu.sebcsaba.gitrelocate;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class IntegrationTest extends GitCmdLineTestBase {

	protected String getTmpFolderName() {
		return "git-relocate-integration-test-data";
	}

	@Test
	public void testRealGitImpl() throws IOException, InterruptedException {
		tool.run("git", "init");
		doCommit(0);
		git.createBranch("B0");
		git.checkOut("B0");
		doCommit(1);
		git.createTag("T1");
		doCommit(2);
		git.createBranch("B1");
		doCommit(3);
		doCommit(5);
		
		git.checkOut("B1");
		doCommit(4);
		git.createTag("T0");
		doCommit(6);
		
		git.checkOut("B0");
		doMergeCommit(7, "B1");
		
		git.checkOut("master");
		
		// have to sleep to prevent reusing the #1 commit for the next
		Thread.sleep(1000);
		// commit-8
		doCommit(1);
		
		tool.run("git", "commit", "--amend", "-m", "commit-8");
		git.createTag("T2");
		git.checkOut("B0");
		git.removeBranch("master");
		
		GitRelocate relocator = new GitRelocate(git, new GraphBuilder(git), PointerMode.MOVE, PointerMode.SKIP, false);
		CommitID cutPoint = git.getCommitId("T1");
		CommitID newBase = git.getCommitId("T2");
		relocator.relocate(cutPoint,newBase);
	}
	
	private void doMergeCommit(int i, String otherBranch) throws IOException {
		FileUtils.write(new File(baseDir, "data.txt"), Integer.toString(i));
		tool.run("git", "add", "data.txt");
		String treeId = tool.getString("git", "write-tree").trim();
		String commitId = tool.withInput("commit-"+i).getString("git", "commit-tree", treeId, "-p", git.getActualHeadName(), "-p", otherBranch).trim();
		tool.run("git", "reset", "--hard", commitId);
	}
	
}
