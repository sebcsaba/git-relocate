package hu.sebcsaba.gitrelocate;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GitCmdLineRunnerTest {

	private File baseDir;
	private CmdLineTool tool;
	private GitCmdLineRunner git;
	
	@Before
	public void initializeGitFolder() throws IOException {
		baseDir = new File(FileUtils.getTempDirectory(), "git-relocate-cmdlinerunner-test-data");
		FileUtils.deleteDirectory(baseDir);
		baseDir.mkdirs();
		tool = new CmdLineTool(baseDir);
		git = new GitCmdLineRunner(tool);
	}

	@After
	public void destroyGitFolder() throws IOException {
		FileUtils.deleteDirectory(baseDir);
	}

	private void doCommit(int i) throws IOException {
		FileUtils.write(new File(baseDir, "data.txt"), Integer.toString(i));
		tool.run("git", "add", "data.txt");
		tool.run("git", "commit", "-m", "commit-"+i);
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

}
