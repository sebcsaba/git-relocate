package hu.sebcsaba.gitrelocate;

public class CommitID {

	private final String id;

	public CommitID(String id) {
		if (id==null || id.length()!=40) {
			throw new IllegalArgumentException("Invalid git commit id (SHA1): "+id);
		}
		this.id = id;
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
		CommitID that = (CommitID) obj;
		return this.id.equals(that.id);
	}

	@Override
	public String toString() {
		return id;
	}
	
}
