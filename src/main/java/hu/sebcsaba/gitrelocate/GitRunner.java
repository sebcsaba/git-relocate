package hu.sebcsaba.gitrelocate;

import java.util.List;

public interface GitRunner {

	public CommitID getCommitId(String anyGitCommitRef);

	public List<CommitID> getCommitParentIds(CommitID commitId);

	public List<String> getTagNames();

	public List<String> getBranchNames();

}
