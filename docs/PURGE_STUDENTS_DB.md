# Purge students.db from Git history (BFG + git)

WARNING: This procedure REWRITES repository history. All collaborators must re-clone the repository after this. Only do this if you need the DB removed from all past commits (e.g., it contains sensitive or large data).

Recommended approach: use the BFG Repo-Cleaner (faster and simpler than git filter-branch).

Prerequisites
- Java (for BFG)
- git
- network access to GitHub
- You must have permission to force-push to the repository's default branch (often `main`).

High-level steps (BFG)
1. Create a local mirror clone of the repository.
2. Run BFG to delete students.db (or any *.db files).
3. Run git gc and push the cleaned history with --force.
4. Notify collaborators to re-clone.

Exact commands (copy-paste)

# 1) Mirror-clone the repository
# Replace the URL with SSH if you prefer (git@github.com:dukandu4112/Student-Grade-Manager.git)
git clone --mirror https://github.com/dukandu4112/Student-Grade-Manager.git
cd Student-Grade-Manager.git

# 2) Download BFG if you don't already have it (version may change)
# This downloads the bfg jar into the current directory
curl -LO https://repo1.maven.org/maven2/com/madgag/bfg/1.14.0/bfg-1.14.0.jar

# 3) Run BFG to remove students.db (and other .db files if desired)
# The --delete-files accepts glob patterns
java -jar bfg-1.14.0.jar --delete-files students.db
# Optional: remove all .db files
# java -jar bfg-1.14.0.jar --delete-files '*.db'

# 4) Clean up and expire reflog, run garbage collection
git reflog expire --expire=now --all
git gc --prune=now --aggressive

# 5) Force-push cleaned branches and tags back to GitHub
# WARNING: This rewrites history on remote. Make sure you understand the consequences.
git push --force --all
git push --force --tags

# 6) Notify collaborators
# Everyone who has cloned the repo must re-clone, or follow the recovery steps below.

Optional: Using git filter-branch (slower / built-in)

# Clone the repo normally (not mirror)
git clone https://github.com/dukandu4112/Student-Grade-Manager.git
cd Student-Grade-Manager

# Rewrite history to remove students.db
# This will touch all branches and tags. Replace 'students.db' pattern as needed.
git filter-branch --force --index-filter "git rm --cached --ignore-unmatch students.db" --prune-empty --tag-name-filter cat -- --all

# Garbage collect and force-push
rm -rf .git/refs/original/
git reflog expire --expire=now --all
git gc --prune=now --aggressive
git push --force --all
git push --force --tags

Recovery instructions for collaborators
- After you force-push rewritten history, collaborators' local clones will be incompatible. They should either:
  - Re-clone the repository from GitHub: git clone <repo-url>
  - Or reset an existing clone (advanced):
    # Fetch the rewritten history
    git fetch origin
    # Reset local branches to match remote
    git checkout main
    git reset --hard origin/main
    # Delete and re-create local feature branches if necessary

Notes and warnings
- Rewriting history will change all commit SHAs. Any PRs based on old commits will be affected and may need to be recreated.
- If your repository has branch protections that prevent force pushes, you must disable those protections temporarily or perform this with elevated permissions.
- If you need me to perform the purge for you (I cannot force-push from here), I can prepare a script or guide and walk you through each step.

If you're ready, run the commands above locally. If you'd like, I also added a small helper script (scripts/purge-students-db.sh) in this repo that automates the BFG flow — run it locally.
