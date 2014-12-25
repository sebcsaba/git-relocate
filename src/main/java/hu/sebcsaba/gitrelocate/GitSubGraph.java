package hu.sebcsaba.gitrelocate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GitSubGraph {
	
	private final Map<String,CommitID> tags = new HashMap<String, CommitID>();
	private final Map<String,CommitID> branches = new HashMap<String, CommitID>();
	private final Set<Commit> commits = new HashSet<Commit>();
	
	public Map<String, CommitID> getTags() {
		return tags;
	}
	
	public Map<String, CommitID> getBranches() {
		return branches;
	}
	
	public Set<Commit> getCommits() {
		return commits;
	}
	
}
