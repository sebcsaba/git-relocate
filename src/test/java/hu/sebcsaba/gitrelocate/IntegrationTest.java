package hu.sebcsaba.gitrelocate;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class IntegrationTest {

	private File baseDir;
	private CmdLineTool tool;
	private GitCmdLineRunner git;
	
	@Before
	public void initializeGitFolder() throws IOException {
		baseDir = new File(FileUtils.getTempDirectory(), "git-relocate-integration-test-data");
		FileUtils.deleteDirectory(baseDir);
		baseDir.mkdirs();
		tool = new CmdLineTool(baseDir);
		git = new GitCmdLineRunner(tool);
	}

	@Test
	public void testRealGitImpl() throws IOException {
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
		doCommit(7); // TODO not merge yet!
		git.checkOut("B1");
		doCommit(4);
		git.createTag("T0");
		doCommit(6);
		git.checkOut("master");
		doCommit(1); // commit-8
		tool.run("git", "commit", "--amend", "-m", "commit-8");
		git.createTag("T2");
		git.checkOut("B0");
		git.removeBranch("master");
		
		GitRelocate relocator = new GitRelocate(git, new GraphBuilder(git));
		CommitID cutPoint = git.getCommitId("T1");
		CommitID newBase = git.getCommitId("T2");
		relocator.relocate(cutPoint,newBase);
	}
	
	@After
	public void destroyGitFolder() throws IOException {
		FileUtils.deleteDirectory(baseDir);
	}

	private CommitID doCommit(int i) throws IOException {
		FileUtils.write(new File(baseDir, "data.txt"), Integer.toString(i));
		tool.run("git", "add", "data.txt");
		tool.run("git", "commit", "-m", "commit-"+i);
		return git.getCommitId("HEAD");
	}

}