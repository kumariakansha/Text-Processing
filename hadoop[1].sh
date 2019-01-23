
#!/bin/bash


echo "commands for the project1"



sed 's\[[:punct:]]\ \g' ~/Desktop/test/*.txt | sed 's/*//g'>merged.txt

echo "merged file"

echo "Question 1.1:"
echo `grep -o -E '\w+' merged.txt | sort -u -f | wc -w`

echo "Question 1.2:"
echo `grep -o -E '\w+' merged.txt | grep "^[zZ][a-zA-Z]" | sort | uniq -c | wc -l`

echo "Question 1.3:"
echo `grep -o -E '\w+' merged.txt | sort | uniq -c | awk '$1<4{print $2}' | wc -w`

echo "Question 2:"
echo `grep -o -E '\w+' merged.txt | grep "ing$" | sort | uniq -c | sort -n -r | head`

echo "Question 3:"

echo `grep -o -E '\w+' merged.txt | uniq -c |awk 'BEGIN{ sum1=0; sum2=0;} {if($2 ~ /^me$|^my$|^mine$|^I$/ ) sum1=sum1+$1; else if($2 ~ /^us$|^our$|^ours$|^we$/) sum2=sum2+$1} END{ print sum1" "sum2}'`

echo "Question 4:"

echo `grep -ioE 'in(\W+\w+){1}' merged.txt | tr '[:upper:]' '[:lower:]' | sort | uniq -c | sort -gr | head -n5`
