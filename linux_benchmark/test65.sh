#!/bin/sh

#cd ./1
for i in 1 2 4 8 16 32 64 128 256 512 1024
do 
  mkdir ./$i/
done 
cp testdata65new.csv ./1/testdata65new.csv 
cp kdtree_parameter_puda.txt ./1/kdtree_parameter_puda.txt 
cp kdtree1.jar ./1/kdtree1.jar
cd ./1
time java -jar kdtree1.jar testdata65new.csv 1 kdtree_parameter_puda.txt
echo "1 done"

cd ..

cp testdata65new.csv ./2/testdata65new.csv 
cp kdtree_parameter_puda.txt ./2/kdtree_parameter_puda.txt 
cp kdtree1.jar ./2/kdtree1.jar
cd ./2
time java -jar kdtree1.jar testdata65new.csv 2 kdtree_parameter_puda.txt
echo "2 done"

cd ..

cp testdata65new.csv ./4/testdata65new.csv 
cp kdtree_parameter_puda.txt ./4/kdtree_parameter_puda.txt 
cp kdtree1.jar ./4/kdtree1.jar
cd ./4
time java -jar kdtree1.jar testdata65new.csv 4 kdtree_parameter_puda.txt
echo "4 done"

cd ..

cp testdata65new.csv ./8/testdata65new.csv 
cp kdtree_parameter_puda.txt ./8/kdtree_parameter_puda.txt 
cp kdtree1.jar ./8/kdtree1.jar
cd ./8
time java -jar kdtree1.jar testdata65new.csv 8 kdtree_parameter_puda.txt
echo "8 done"

cd ..


cp testdata65new.csv ./16/testdata65new.csv 
cp kdtree_parameter_puda.txt ./16/kdtree_parameter_puda.txt 
cp kdtree1.jar ./16/kdtree1.jar
cd ./16
time java -jar kdtree1.jar testdata65new.csv 16 kdtree_parameter_puda.txt
echo "16 done"

cd ..

cp testdata65new.csv ./32/testdata65new.csv 
cp kdtree_parameter_puda.txt ./32/kdtree_parameter_puda.txt 
cp kdtree1.jar ./32/kdtree1.jar
cd ./32
time java -jar kdtree1.jar testdata65new.csv 32 kdtree_parameter_puda.txt
echo "32 done"

cd ..

cp testdata65new.csv ./64/testdata65new.csv 
cp kdtree_parameter_puda.txt ./64/kdtree_parameter_puda.txt 
cp kdtree1.jar ./64/kdtree1.jar
cd ./64
time java -jar kdtree1.jar testdata65new.csv 64 kdtree_parameter_puda.txt
echo "64 done"

cd ..

cp testdata65new.csv ./128/testdata65new.csv 
cp kdtree_parameter_puda.txt ./128/kdtree_parameter_puda.txt 
cp kdtree1.jar ./128/kdtree1.jar
cd ./128
time java -jar kdtree1.jar testdata65new.csv 128 kdtree_parameter_puda.txt
echo "128 done"

cd ..

cp testdata65new.csv ./256/testdata65new.csv 
cp kdtree_parameter_puda.txt ./256/kdtree_parameter_puda.txt 
cp kdtree1.jar ./256/kdtree1.jar
cd ./256
time java -jar kdtree1.jar testdata65new.csv 256 kdtree_parameter_puda.txt
echo "256 done"

cd ..

cp testdata65new.csv ./512/testdata65new.csv 
cp kdtree_parameter_puda.txt ./512/kdtree_parameter_puda.txt 
cp kdtree1.jar ./512/kdtree1.jar
cd ./512
time java -jar kdtree1.jar testdata65new.csv 512 kdtree_parameter_puda.txt
echo "512 done"

cd ..

cp testdata65new.csv ./1024/testdata65new.csv 
cp kdtree_parameter_puda.txt ./1024/kdtree_parameter_puda.txt 
cp kdtree1.jar ./1024/kdtree1.jar
cd ./1024
time java -jar kdtree1.jar testdata65new.csv 1024 kdtree_parameter_puda.txt
echo "1024 done"

cd ..


