
@echo off



for  %%x in (1,2 ,4,8,16,32,64,128,256,512,1024 ) do (

   echo %%x knode start...
   mkdir %%x
   cd %%x
   cd ..
   powershell cp testdata65new.csv ./%%x
   powershell cp Query.jar ./%%x
   powershell cp query_range_puda.txt ./%%x
   cd %%x
  
   echo %%x Query range start... 

   powershell -Command "Measure-Command { & java -Xms10g -jar Query.jar testdata65new.csv query_range_puda.txt| Out-Default}"
   
   powershell rm testdata65new.csv 

   echo %%x " range  done "   
   cd ..\

)




pause
for  %%a in (1 ,10 ,100,500,1000,5000) do (
 echo %%a top start...
 for  %%x in (1,2 ,4,8,16,32,64,128,256,512,1024 ) do (

   echo %%x knode start...
   mkdir %%x
   cd %%x
   cd ..
   powershell cp testdata65new.csv ./%%x
   powershell cp query_topK_puda.txt ./%%x
   cd %%x
  
   echo %%x Query topK start... 

   powershell -Command "Measure-Command { & java -Xms10g -jar Query.jar testdata65new.csv query_topK_puda.txt %%a| Out-Default}"
   
   powershell rm testdata65new.csv 

   echo %%x " topK  done "   
   cd ..\

 )
 echo %%a top done
)
pause
