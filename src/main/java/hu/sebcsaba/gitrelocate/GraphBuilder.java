package hu.sebcsaba.gitrelocate;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class GraphBuilder {
	
	private final GitRunner git;
	
	public GraphBuilder(GitRunner git) {
		this.git = git;
	}

	public GitSubGraph buildFullTree() {
		GitSubGraph result = new GitSubGraph();
		fillBranchesMap(result);
		fillTagsMap(result);
		Queue<CommitID> queue = new OnlyOnceQueue<CommitID>(getInitialCommitSet(result));
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

	private Set<CommitID> getInitialCommitSet(GitSubGraph graph) {
		Set<CommitID> result = new HashSet<CommitID>();
		result.addAll(graph.getBranches().values());
		result.addAll(graph.getTags().values());
		return result;
	}
	
	public GitSubGraph getSubTree(GitSubGraph source, CommitID root) {
		GitSubGraph result = new GitSubGraph();
		Queue<CommitID> queue = new OnlyOnceQueue<CommitID>();
		queue.add(root);
		while (!queue.isEmpty()) {
			CommitID id = queue.poll();
			result.getCommits().add(source.getCommit(id));
			Set<CommitID> children = source.getChildrenCommitIDs(id);
			queue.addAll(children);
		}
		filterNamedCommits(result, source.getBranches(), result.getBranches());
		filterNamedCommits(result, source.getTags(), result.getTags());
		return result;
	}

	private void filterNamedCommits(GitSubGraph result, Map<String, CommitID> sourceMap, Map<String, CommitID> resultMap) {
		for (String branch : sourceMap.keySet()) {
			CommitID c = sourceMap.get(branch);
			if (result.hasCommit(c)) {
				resultMap.put(branch, c);
			}
		}
	}

}
