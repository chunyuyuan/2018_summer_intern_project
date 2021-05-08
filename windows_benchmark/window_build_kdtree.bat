
@echo off



for  %%x in (1 ,2 ,4,8,16,32,64,128,256,512,1024) do (

   echo %%x knode start...
   mkdir %%x
   cd %%x
   cd ..
   powershell cp testdata65new.csv ./%%x
   powershell cp kdtree1.jar ./%%x
   powershell cp kdtree_parameter_puda.txt ./%%x
   cd %%x
  
   echo %%x knode build kdtree start... 

   powershell -Command "Measure-Command { & java -Xms14g -jar kdtree1.jar testdata65new.csv %%x kdtree_parameter_puda.txt| Out-Default}"
   
   powershell rm testdata65new.csv 

   echo %%x " knode  done "   
   cd ..\

)




pause

