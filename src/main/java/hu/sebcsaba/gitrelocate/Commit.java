package hu.sebcsaba.gitrelocate;

import java.util.Collections;
import java.util.List;

public class Commit {
	
	private final CommitID id;
	
	private final List<CommitID> parents;

	public Commit(CommitID id, List<CommitID> parents) {
		this.id = id;
		this.parents = Collections.unmodifiableList(parents);
	}

	public CommitID getId() {
		return id;
	}

	public List<CommitID> getParents() {
		return parents;
	}
	
}
