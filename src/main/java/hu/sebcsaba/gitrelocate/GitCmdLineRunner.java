package hu.sebcsaba.gitrelocate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.IOUtils;

public class GitCmdLineRunner implements GitRunner {
	
	private final CmdLineTool cmdLine;
	
	public GitCmdLineRunner(CmdLineTool cmdLine) {
		this.cmdLine = cmdLine;
	}
	
	public CommitID getCommitId(String anyGitCommitRef) {
		return new CommitID(gitString("rev-parse", anyGitCommitRef).trim());
	}
	
	public List<CommitID> getCommitParentIds(CommitID commitId) {
		return getCommitIDList(gitStringList("log", "--pretty=%P", "-n", "1", commitId.toString()));
	}
	
	public Collection<String> getTagNames() {
		return gitStringList("tag");
	}
	
	public Collection<String> getBranchNames() {
		List<String> result = gitStringList("branch");
		result.remove("*");
		return result;
	}
	
	public String getActualHeadName() {
		String head = gitString("rev-parse", "--abbrev-ref", "HEAD");
		if ("HEAD".equals(head)) {
			head = gitString("rev-parse", "HEAD");
		}
		return head.trim();
	}

	public void createBranch(String branchName) {
		gitExec("branch", branchName);
	}

	public void createBranch(String branchName, CommitID commitId) {
		gitExec("branch", branchName, commitId.toString());
	}

	public void removeBranch(String branchName) {
		gitExec("branch", "-D", branchName);
	}

	public void moveBranch(String branchName, CommitID commitId) {
		String head = getActualHeadName();
		checkOut(branchName);
		gitExec("reset", "--hard", commitId.toString());
		checkOut(head);
	}

	public void checkOut(String branchName) {
		gitExec("checkout", branchName);
	}

	public void createTag(String tagName) {
		gitExec("tag", tagName);
	}

	public void createTag(String tagName, CommitID commitId) {
		gitExec("tag", tagName, commitId.toString());
	}

	public void removeTag(String tagName) {
		gitExec("tag", "-D", tagName);
	}

	public CommitID cherryPick(CommitID commitId) {
		gitExec("cherry-pick", commitId.toString());
		return getCommitId("HEAD");
	}

	public CommitID cherryPickMergeCommit(CommitID commitId, List<CommitID> newParentsIds) {
		try {
			File TMP_MSG_FILE = File.createTempFile("git-relocate", ".tmp");
			
			gitExec("cherry-pick", "--no-ff", "--no-commit", "--mainline", "1", commitId.toString());
			String treeId = gitString("write-tree").trim();
			String message = gitString("log", "-1", "--pretty=%B", commitId.toString());
			IOUtils.write(message, new FileOutputStream(TMP_MSG_FILE), "UTF-8");

			List<String> commitParams = new ArrayList<String>();
			commitParams.addAll(Arrays.asList("commit-tree", treeId, "-F", TMP_MSG_FILE.getAbsolutePath()));
			for (CommitID newParentId : newParentsIds) {
				commitParams.add("-p");
				commitParams.add(newParentId.toString());
			}
			String commit = gitString(commitParams.toArray(new String[commitParams.size()])).trim();
			
			TMP_MSG_FILE.delete();
			
			return new CommitID(commit);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	private void gitExec(String... params) {
		try {
			cmdLine.run(unshift("git", params));
		}
		catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	private String gitString(String... params) {
		try {
			return cmdLine.getString(unshift("git", params));
		}
		catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	private List<String> gitStringList(String... params) {
		try {
			return cmdLine.getStringList(unshift("git", params));
		}
		catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	private List<CommitID> getCommitIDList(List<String> ids) {
		List<CommitID> result = new ArrayList<CommitID>(ids.size());
		for (String id : ids) {
			result.add(new CommitID(id));
		}
		return result;
	}
	
	private String[] unshift(String first, String[] rest) {
		String[] result = new String[rest.length+1];
		result[0] = first;
		System.arraycopy(rest, 0, result, 1, rest.length);
		return result;
	}

}
