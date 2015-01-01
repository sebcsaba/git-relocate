package hu.sebcsaba.gitrelocate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

public class MockedGitRunner implements GitRunner {
	
	private static final String ZERO = "0000000000000000000000000000000000000000";
	
	private final GitSubGraph graph;
	private String head;
	
	public MockedGitRunner(GitSubGraph graph) {
		this.graph = graph;
		if (!this.graph.getBranches().isEmpty()) {
			head = this.graph.getBranches().keySet().iterator().next();
		}
	}

	public MockedGitRunner(int[] branches, int[] tags, int[][] parents) {
		this.graph = buildGraph(branches, tags, parents);
		if (branches.length>0) {
			head = "B0";
		}
	}

	private static GitSubGraph buildGraph(int[] branches, int[] tags, int[][] parents) {
		GitSubGraph graph = new GitSubGraph();
		for (int i=0; i<branches.length; ++i) {
			graph.getBranches().put("B"+i, intToCommitID(branches[i]));
		}
		for (int i=0; i<tags.length; ++i) {
			graph.getTags().put("T"+i, intToCommitID(tags[i]));
		}
		for (int i=0; i<parents.length; ++i) {
			List<CommitID> parentIDs = new ArrayList<CommitID>();
			for (int j=0; j<parents[i].length; ++j) {
				parentIDs.add(intToCommitID(parents[i][j]));
			}
			Commit c = new Commit(intToCommitID(i), parentIDs);
			graph.getCommits().add(c);
		}
		return graph;
	}

	public CommitID getCommitId(String anyGitCommitRef) {
		if (graph.getBranches().containsKey(anyGitCommitRef)) {
			return graph.getBranches().get(anyGitCommitRef);
		} else if (graph.getTags().containsKey(anyGitCommitRef)) {
			return graph.getTags().get(anyGitCommitRef);
		} else if (anyGitCommitRef.matches("\\w{40}")) {
			return new CommitID(anyGitCommitRef);
		} else {
			throw new IllegalArgumentException("Unable to parse commit id for "+anyGitCommitRef);
		}
	}

	public List<CommitID> getCommitParentIds(CommitID commitId) {
		return graph.getCommit(commitId).getParents();
	}

	public Collection<String> getTagNames() {
		return new ArrayList<String>(this.graph.getTags().keySet());
	}

	public Collection<String> getBranchNames() {
		return new ArrayList<String>(this.graph.getBranches().keySet());
	}

	public static CommitID intToCommitID(int cid) {
		String cids = Integer.toString(cid);
		return new CommitID(ZERO.substring(0, ZERO.length()-cids.length()) + cids);
	}

	public String getActualHeadName() {
		return head;
	}
	
	public void createBranch(String branchName, CommitID commitId) {
		graph.getBranches().put(branchName, commitId);
	}
	
	public void removeBranch(String branchName) {
		graph.getBranches().remove(branchName);
	}

	public void checkOut(String branchName) {
		head = branchName;
	}

	public void createTag(String tagName, CommitID commitId) {
		graph.getTags().put(tagName, commitId);
	}
	
	public void removeTag(String tagName) {
		graph.getTags().remove(tagName);
	}

	public CommitID cherryPick(CommitID commitId) {
		CommitID newId = intToCommitID(graph.getCommits().size());
		List<CommitID> parents = Arrays.asList(getCommitId(head));
		Commit c = new Commit(newId, parents);
		graph.getCommits().add(c);
		if (isDetachedHead()) {
			head = newId.toString();
		} else {
			graph.getBranches().put(head, newId);
		}
		return newId;
	}
	
	private boolean isDetachedHead() {
		return !graph.getBranches().containsKey(head);
	}

	public Set<CommitID> getAllAncestors(CommitID commitId) {
		Set<CommitID> result = new HashSet<CommitID>();
		Queue<CommitID> commits = new LinkedList<CommitID>();
		commits.add(commitId);
		while (!commits.isEmpty()) {
			CommitID id = commits.poll();
			Commit c = graph.getCommit(id);
			commits.addAll(c.getParents());
			result.add(id);
		}
		return result ;
	}

}
