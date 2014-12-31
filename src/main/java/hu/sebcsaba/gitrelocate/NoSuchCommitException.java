package hu.sebcsaba.gitrelocate;

public class NoSuchCommitException extends RuntimeException {
	
	private static final long serialVersionUID = -2455737776827643056L;
	
	private final CommitID id;
	
	public NoSuchCommitException(CommitID id) {
		super("No commit found: "+id);
		this.id = id;
	}



	public CommitID getId() {
		return id;
	}
	
}
