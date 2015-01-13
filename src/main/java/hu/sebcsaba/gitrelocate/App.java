package hu.sebcsaba.gitrelocate;

import java.util.ArrayList;
import java.util.List;

public class App {

	public static void main(String[] args) {
		Parameters params = parseParams(args);
		if (params.help) {
			printHelp(params.errorMessage);
		}
		new App().run(params);
	}

	private static Parameters parseParams(String[] args) {
		Parameters result = new Parameters();
		List<String> items = new ArrayList<String>();
		for (int i=0; i<args.length; ++i) {
			String arg = args[i];
			if ("--help".equalsIgnoreCase(arg) || "/?".equals(arg)) {
				result.help = true;
				return result;
			}
			if (arg.startsWith("--branches=")) {
				result.branches = getPointerMode(arg);
			} else if (arg.startsWith("--tags=")) {
				result.tags = getPointerMode(arg);
			} else {
				items.add(arg);
			}
		}
		if (items.size()!=2) {
			result.errorMessage = "Two commit-ref parameters expected";
			result.help = true;
			return result;
		}
		result.sourceCommit = items.get(0);
		result.destinationCommit = items.get(1);
		return result;
	}

	private static PointerMode getPointerMode(String arg) {
		String mode = arg.split("=")[1];
		return PointerMode.valueOf(mode);
	}

	public void run(Parameters params) {
		CmdLineTool tool = new CmdLineTool();
		GitRunner git = new GitCmdLineRunner(tool);
		GitRelocate relocator = new GitRelocate(git, new GraphBuilder(git), params.branches, params.tags);
		CommitID cutPoint = git.getCommitId(params.sourceCommit);
		CommitID newBase = git.getCommitId(params.destinationCommit);
		relocator.relocate(cutPoint,newBase);
	}
	
	private static final class Parameters {
		public boolean help = false;
		public String errorMessage = null;
		public PointerMode branches = PointerMode.MOVE;
		public PointerMode tags = PointerMode.MOVE;
		public String sourceCommit;
		public String destinationCommit;
	}

	private static void printHelp(String errorMessage) {
		if (errorMessage!=null) {
			System.out.println(errorMessage);
			System.out.println();
		}
		System.out.println("usage: git-relocate [options] <source> <destination>");
		System.out.println("will clone all descendant commits of source to destination.");
		System.out.println("options:");
		System.out.println("	--branches=[move|clone|skip]    (default: move)");
		System.out.println("	--tags=[move|clone|skip]        (default: move)");
		System.out.println("These flags specify how to handle branches/tags under source commit:");
		System.out.println("	move:  remove the original tags/branches, and create a new in the cloned subtree");
		System.out.println("	clone: leave the original tags/branches, but create a 'clone-' prefixed copy in the cloned subtree");
		System.out.println("	skip:  ignore tags/branches");
		System.exit(-1);
	}

}
