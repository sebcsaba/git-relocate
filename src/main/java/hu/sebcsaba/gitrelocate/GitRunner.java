package hu.sebcsaba.gitrelocate;

import java.util.Collection;
import java.util.List;

public interface GitRunner {

	public CommitID getCommitId(String anyGitCommitRef);

	public List<CommitID> getCommitParentIds(CommitID commitId);

	public Collection<String> getTagNames();

	public Collection<String> getBranchNames();

}
