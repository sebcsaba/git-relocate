package hu.sebcsaba.gitrelocate;

import java.util.Collection;
import java.util.List;

public interface GitRunner {

	public CommitID getCommitId(String anyGitCommitRef);

	public List<CommitID> getCommitParentIds(CommitID commitId);

	public Collection<String> getTagNames();

	public Collection<String> getBranchNames();

	public String getActualHeadName();
	
	public void createBranch(String branchName, CommitID commitId);

	public void removeBranch(String branchName);

	public void moveBranch(String branchName, CommitID commitId);

	public void checkOut(String branchName);

	public void createTag(String tagName, CommitID commitId);

	public void removeTag(String tagName);

	public CommitID cherryPick(CommitID commitId);

}
