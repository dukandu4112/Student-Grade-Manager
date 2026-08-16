#!/usr/bin/env bash
set -euo pipefail

# scripts/purge-students-db.sh
# Purpose: Mirror-clone the repository, run BFG to remove a file pattern (default: students.db),
#         garbage-collect, and force-push cleaned history back to GitHub.
# WARNING: This rewrites remote history. All collaborators MUST re-clone after this.

REPO_URL="https://github.com/dukandu4112/Student-Grade-Manager.git"
MIRROR_DIR="Student-Grade-Manager.git"
BFG_VERSION="1.14.0"
BFG_JAR="bfg-${BFG_VERSION}.jar"
PATTERN="${1:-students.db}"

echo "Repository: $REPO_URL"
echo "Mirror dir: $MIRROR_DIR"
echo "BFG jar: $BFG_JAR"
echo "File pattern to remove: $PATTERN"

# 1) Mirror clone (bare)
if [ -d "$MIRROR_DIR" ]; then
  echo "Mirror directory '$MIRROR_DIR' already exists. Removing it to start fresh..."
  rm -rf "$MIRROR_DIR"
fi

echo "Cloning mirror..."
git clone --mirror "$REPO_URL" "$MIRROR_DIR"
cd "$MIRROR_DIR"

# 2) Download BFG if needed
if [ ! -f "$BFG_JAR" ]; then
  echo "Downloading BFG $BFG_VERSION..."
  curl -sSL -o "$BFG_JAR" "https://repo1.maven.org/maven2/com/madgag/bfg/${BFG_VERSION}/${BFG_JAR}"
fi

# 3) Run BFG to delete files matching the pattern
echo "Running BFG to delete files matching: $PATTERN"
java -jar "$BFG_JAR" --delete-files "$PATTERN"

# 4) Cleanup and garbage collection
echo "Expiring reflog and running git gc..."
git reflog expire --expire=now --all
git gc --prune=now --aggressive

# 5) Force-push cleaned history back to origin
echo "Force-pushing cleaned refs to origin..."
git push --force --all
git push --force --tags

cat <<'EOF'
Done.
WARNING: You have rewritten repository history.
- All commit SHAs have changed.
- Collaborators must re-clone the repository, or reset their local clones to the new history.

Recommended post-steps:
1) Re-enable branch protection if you disabled it.
2) Inform collaborators to re-clone.
3) Verify the remote repository on GitHub no longer contains the target files.

Example: Run this script from a safe working directory:
chmod +x scripts/purge-students-db.sh
./scripts/purge-students-db.sh students.db
EOF
