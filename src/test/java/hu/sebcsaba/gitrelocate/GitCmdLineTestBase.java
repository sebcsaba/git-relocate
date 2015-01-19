package hu.sebcsaba.gitrelocate;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;

public abstract class GitCmdLineTestBase {

	protected File baseDir;
	protected CmdLineTool tool;
	protected GitCmdLineRunner git;
	
	@Before
	public void initializeGitFolder() throws IOException {
		baseDir = new File(FileUtils.getTempDirectory(), getTmpFolderName());
		FileUtils.deleteDirectory(baseDir);
		baseDir.mkdirs();
		tool = new CmdLineTool(baseDir);
		git = new GitCmdLineRunner(tool);
	}

	protected abstract String getTmpFolderName();
	
	@After
	public void destroyGitFolder() throws IOException {
		FileUtils.deleteDirectory(baseDir);
	}
	
	protected void doCommit(int i) throws IOException {
		FileUtils.write(new File(baseDir, "data.txt"), Integer.toString(i));
		tool.run("git", "add", "data.txt");
		tool.run("git", "commit", "-m", "commit-"+i);
	}
	
}
