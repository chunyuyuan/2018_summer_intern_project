

# build kd+-tree tree,  knode size is 1 

java -jar kdtree1.jar data_taxi_1000.csv 1 kdtree_taxi.txt


# range query

java -jar Query.jar data_taxi_1000.csv query_taxi_range.txt


# please open the data_pudg_1000_result.txt to see the results