package hu.sebcsaba.gitrelocate;

public class App {

	public static void main(String[] args) {
		if (args.length!=2) {
			System.out.println("usage: git-relocate <source> <destination>");
			System.out.println("will clone all descendant commits of source to destination.");
			System.out.println("will move all descendant branches under source to below destination.");
			System.out.println("will not touch tags.");
			System.exit(-1);
		}
		new App().run(args[0], args[1]);
	}

	public void run(String source, String destination) {
		CmdLineTool tool = new CmdLineTool();
		GitRunner git = new GitCmdLineRunner(tool);
		GitRelocate relocator = new GitRelocate(git, new GraphBuilder(git));
		CommitID cutPoint = git.getCommitId(source);
		CommitID newBase = git.getCommitId(destination);
		relocator.relocate(cutPoint,newBase);
	}

}
