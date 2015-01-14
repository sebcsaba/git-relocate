package hu.sebcsaba.gitrelocate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class GitRelocate {
	
	private final GitRunner git;
	private final GraphBuilder builder;
	private final PointerMode modeOfBranches;
	private final PointerMode modeOfTags;
	
	public GitRelocate(GitRunner git, GraphBuilder builder, PointerMode modeOfBranches, PointerMode modeOfTags) {
		this.git = git;
		this.builder = builder;
		this.modeOfBranches = modeOfBranches;
		this.modeOfTags = modeOfTags;
	}

	public void relocate(CommitID cutPoint, CommitID newBase) {
		
		System.out.print("Building full tree...");
		GitSubGraph fullTree = builder.buildFullTree();
		System.out.println("done");
		
		System.out.print("Calculating subtree to relocate...");
		GitSubGraph subTree = builder.getSubTree(fullTree, cutPoint);
		System.out.println("done");
		
		String previousHead = git.getActualHeadName();
		
		System.out.print("Relocate...");
		relocate(subTree, cutPoint, newBase);
		System.out.println("done");
		
		git.checkOut(previousHead);
	}

	private void relocate(GitSubGraph subTree, CommitID cutPoint, CommitID newBase) {
		Queue<Commit> commitsToClone = new LinkedList<Commit>();
		commitsToClone.addAll(subTree.getCommits());
		
		Map<CommitID, CommitID> cloneMap = new HashMap<CommitID, CommitID>();
		cloneMap.put(cutPoint, newBase);
		
		while (!commitsToClone.isEmpty()) {
			Commit commitToClone = commitsToClone.poll();
			if (commitToClone.getId().equals(cutPoint)) {
				// this is in the queue, but not need to clone
			} else if (hasAllParentsCloned(subTree, commitToClone, cloneMap)) {
				clone(commitToClone, cloneMap);
			} else {
				commitsToClone.add(commitToClone);
			}
		}
		if (modeOfBranches!=PointerMode.SKIP) {
			moveOrClonePointers(cloneMap, subTree.getBranches(), modeOfBranches, false);
		}
		if (modeOfTags!=PointerMode.SKIP) {
			moveOrClonePointers(cloneMap, subTree.getTags(), modeOfTags, true);
		}
	}

	private void moveOrClonePointers(Map<CommitID, CommitID> cloneMap, Map<String, CommitID> pointers, PointerMode mode, boolean isTag) {
		for (String pointerName : pointers.keySet()) {
			CommitID oldId = pointers.get(pointerName);
			if (cloneMap.containsKey(oldId)) {
				CommitID newId = cloneMap.get(oldId);
				if (mode==PointerMode.CLONE) {
					if (isTag) {
						git.createTag("clone-"+pointerName, newId);
					} else {
						git.createBranch("clone-"+pointerName, newId);
					}
				} else if (mode==PointerMode.MOVE) {
					if (isTag) {
						git.removeTag(pointerName);
						git.createTag(pointerName, newId);
					} else {
						git.moveBranch(pointerName, newId);
					}
				}
			}
		}
	}

	private boolean hasAllParentsCloned(GitSubGraph treeToClone, Commit commit, Map<CommitID, CommitID> cloneMap) {
		for (CommitID parentID : commit.getParents()) {
			if (treeToClone.hasCommit(parentID) && !cloneMap.containsKey(parentID)) {
				return false;
			}
		}
		return true;
	}

	private void clone(Commit commitToClone, Map<CommitID, CommitID> cloneMap) {
		if (commitToClone.getParents().isEmpty()) {
			throw new UnsupportedOperationException("Cloning root commit is not implemented yet");
		}
		
		CommitID oldParent1Id = commitToClone.getParents().get(0);
		CommitID newParent1Id = cloneMap.containsKey(oldParent1Id) ? cloneMap.get(oldParent1Id) : oldParent1Id;
		git.checkOut(newParent1Id.toString());
		
		if (commitToClone.getParents().size()==1) {
			CommitID newId = git.cherryPick(commitToClone.getId());
			cloneMap.put(commitToClone.getId(), newId);
		} else {
			List<CommitID> newParentsIds = new ArrayList<CommitID>();
			for (CommitID oldParentId : commitToClone.getParents()) {
				newParentsIds.add(cloneMap.containsKey(oldParentId) ? cloneMap.get(oldParentId) : oldParentId);
			}
			CommitID newId = git.cherryPickMergeCommit(commitToClone.getId(), newParentsIds);
			cloneMap.put(commitToClone.getId(), newId);
		}
	}
	
}
