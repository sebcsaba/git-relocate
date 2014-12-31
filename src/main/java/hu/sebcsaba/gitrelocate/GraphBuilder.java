package hu.sebcsaba.gitrelocate;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class GraphBuilder {
	
	private final GitRunner git;
	
	public GraphBuilder(GitRunner git) {
		this.git = git;
	}

	public GitSubGraph buildFullTree() {
		GitSubGraph result = new GitSubGraph();
		fillBranchesMap(result);
		fillTagsMap(result);
		Queue<CommitID> queue = getInitialCommitSet(result);
		while (!queue.isEmpty()) {
			CommitID id = queue.poll();
			List<CommitID> parentIDs = git.getCommitParentIds(id);
			result.getCommits().add(new Commit(id, parentIDs));
			queue.addAll(parentIDs);
		}
		return result;
	}

	private void fillBranchesMap(GitSubGraph graph) {
		for (String branch : git.getBranchNames()) {
			graph.getBranches().put(branch, git.getCommitId(branch));
		}
	}

	private void fillTagsMap(GitSubGraph graph) {
		for (String tag : git.getTagNames()) {
			graph.getTags().put(tag, git.getCommitId(tag));
		}
	}

	private Queue<CommitID> getInitialCommitSet(GitSubGraph graph) {
		Queue<CommitID> result = new LinkedList<CommitID>();
		result.addAll(graph.getBranches().values());
		result.addAll(graph.getTags().values());
		return result;
	}

}
