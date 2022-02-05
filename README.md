# EMR-MatrixMultiplication
Matrix multiplication using Map Reduce

![image](https://user-images.githubusercontent.com/23340399/152645322-0c3d5f2f-2721-4c59-ae6a-3ca58b1a6b44.png)

C output needs to be generated
### Input file
![image](https://user-images.githubusercontent.com/23340399/152645336-08cc6550-ca06-46bc-8b17-986babf37adb.png)

### Mapper Function
**N = Number of rows in matrix A or number of columns in matrix B**<br>
The map function will duplicate N times as follows.
![image](https://user-images.githubusercontent.com/23340399/152645390-a0dbc93f-7623-4e89-96de-13203c6d34f9.png)


### Reducer Function
![image](https://user-images.githubusercontent.com/23340399/152645425-23bdbc8a-107c-4f96-9842-2e38a5fc2ef0.png)
<br>
![image](https://user-images.githubusercontent.com/23340399/152645433-01dee6ac-a833-4bc7-8a03-7f2ae28f2761.png)

### Output file
![image](https://user-images.githubusercontent.com/23340399/152645445-0e511e8e-a0a6-4cb6-b10d-f9ae27890fe4.png)

