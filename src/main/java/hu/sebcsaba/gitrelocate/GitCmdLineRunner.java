package hu.sebcsaba.gitrelocate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
