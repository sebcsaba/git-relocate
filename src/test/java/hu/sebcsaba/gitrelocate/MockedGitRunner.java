package hu.sebcsaba.gitrelocate;

import java.util.ArrayList;
import java.util.List;

public class MockedGitRunner implements GitRunner {
	
	private static final String ZERO = "0000000000000000000000000000000000000000";
	
	private final int[] branches;
	private final int[] tags;
	private final int[][] parents;
	
	public MockedGitRunner(int[] branches, int[] tags, int[][] parents) {
		this.branches = branches;
		this.tags = tags;
		this.parents = parents;
	}

	public CommitID getCommitId(String anyGitCommitRef) {
		int cid = resolve(anyGitCommitRef);
		return intToCommitID(cid);
	}

	public List<CommitID> getCommitParentIds(CommitID commitId) {
		int cid = Integer.parseInt(commitId.toString());
		int[] pcids = parents[cid];
		List<CommitID> result = new ArrayList<CommitID>(pcids.length);
		for (int pcid : pcids) {
			result.add(intToCommitID(pcid));
		}
		return result;
	}

	public List<String> getTagNames() {
		return toNamesList("T",tags.length);
	}

	public List<String> getBranchNames() {
		return toNamesList("B", branches.length);
	}

	private List<String> toNamesList(String prefix, int length) {
		List<String> result = new ArrayList<String>(length);
		for (int i=0; i<length; ++i) {
			result.add(prefix+i);
		}
		return result;
	}
	
	private int resolve(String id) {
		if (id.matches("^B\\d+$")) {
			int key = Integer.parseInt(id.substring(1));
			return branches[key];
		} else if (id.matches("^T\\d+$")) {
			int key = Integer.parseInt(id.substring(1));
			return tags[key];
		} else {
			throw new IllegalArgumentException("Unable to parse commit id for "+id);
		}
	}

	private CommitID intToCommitID(int cid) {
		String cids = Integer.toString(cid);
		return new CommitID(ZERO.substring(0, ZERO.length()-cids.length()) + cids);
	}
	
}
