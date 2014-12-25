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

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null) {
			return false;
		} else if (getClass() != obj.getClass()) {
			return false;
		}
		Commit that = (Commit) obj;
		return this.id.equals(that.id);
	}
	
}
