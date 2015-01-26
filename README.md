# git-relocate
Utility to move or clone full subtrees in git repo

After cloning the repo, just run:

    mvn install
this will create a git alias in your global git config.

    usage: git relocate [options] <source> <destination>
    will clone all descendant commits of source to destination.
    options:
    	--verbose
    	--branches=[move|clone|skip]    (default: move)
    	--tags=[move|clone|skip]        (default: move)
    These flags specify how to handle branches/tags under source commit:
    	move:  remove the original tags/branches, and create a new in the cloned subtree
    	clone: leave the original tags/branches, but create a 'clone-' prefixed copy in the cloned subtree
    	skip:  ignore tags/branches
