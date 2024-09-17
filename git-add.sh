#!/bin/bash
git add .
commit_text="Date: $(date +%F_%H-%M-%S) codedatabase Project..."

echo "$commit_text"

# Wrap the commit message in quotes to handle spaces
git commit -m "$commit_text"

git push origin main

