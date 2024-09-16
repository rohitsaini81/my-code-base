git add .

commit_text = "Date : "$(date +%F_%H-%M-%S) "codedatabase Project..."

echo commit_text

git commit -m commit_text
 
git push origin main
