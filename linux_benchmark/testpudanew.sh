#!/bin/sh

#cd ./2




echo "range benchmark"
for i in 1 2 4 8 16 32 64 128 256 512 1024
do
 
 cp -f Query.jar ./$i/Query.jar 
 cp -f query_range_puda.txt ./$i/query_range_puda.txt
 cp -f query_topK_puda.txt ./$i/query_topK_puda.txt
 cd /home/yuanchunyu/sdb1/pb/$i

 time java -jar Query.jar testdata65new.csv query_range_puda.txt

 cd /home/yuanchunyu/sdb1/pb
 echo $i " done"

done



cd /home/yuanchunyu/sdb1/pb

for j in 1 10 100 500 1000 5000
do
echo "topK " $j " benchmark\n"
 for i in 1 2 4 8 16 32 64 128 256 512 1024
 do
    cp -f Query.jar ./$i/Query.jar 
    cp -f query_topK_puda.txt ./$i/query_topK_puda.txt
    cd /home/yuanchunyu/sdb1/pb/$i

    time java -jar Query.jar testdata65new.csv query_topK_puda.txt $j


    cd /home/yuanchunyu/sdb1/pb
    echo $i " done"

 done
done
cd /home/yuanchunyu/sdb1/pb


