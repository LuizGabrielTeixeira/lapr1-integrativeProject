# 📖 Program Usage Guide

## Interactive Mode
To run the program **in interactive mode**, without arguments:

```sh
java -jar LAPR1_24_25_DAB_02.jar
```

## Non-Interactive Mode (With Arguments)
To run the program with different functions, use the following format:

```
java -jar LAPR1_24_25_DAB_02.jar -f <FUNCTION> -k <EIGENVECTORS> -i <INPUT_FILE> -j <DATABASE_FILE>
```

### Parameters
- -f → Specifies the function to execute (values from 1 to 4).
- -k → Number of eigenvectors to use. (Use "-1" to use the maximum number of eigenvectors).
- -i → Path to the input file (CSV).
- -j → Path to the image database (CSV).

---

## Usage Examples
### ✅ Function 1
```sh
java -jar LAPR1_24_25_DAB_02.jar -f 1 -k -1 -i 'Input/Funcao1/exampleInputFunc1.csv' -j 'Input/Funcao2-3/csv'
```

### ✅ Function 2
```sh
java -jar LAPR1_24_25_DAB_02.jar -f 2 -k -1 -i 'Input/Funcao1/exampleInputFunc1.csv' -j 'Input/Funcao2-3/csv'
```

### ✅ Function 3
```sh
java -jar LAPR1_24_25_DAB_02.jar -f 3 -k -1 -i 'Input/Funcao3/csv/image_140.csv' -j 'Input/Funcao2-3/csv'
```

### ✅ Function 3 – Test with Multiple Images of the Same Weight
```sh
java -jar LAPR1_24_25_DAB_02.jar -f 3 -k -1 -i 'Input/Funcao3/csv/image_777.csv' -j 'Input/Funcao3/csv'
```

### ✅ Function 4 – Random Image Generation with One Eigenface
```sh
java -jar LAPR1_24_25_DAB_02.jar -f 4 -k 1 -i 'Input/Funcao2-3/csv/image_001.csv' -j 'Input/Funcao2-3/csv'
```

### ✅ Function 4 – Random Image Generation with Maximum Eigenfaces
```sh
java -jar LAPR1_24_25_DAB_02.jar -f 4 -k -1 -i 'Input/Funcao2-3/csv/image_001.csv' -j 'Input/Funcao2-3/csv'
```

### ✅ Execute All Functions in Sequence
```sh
java -jar LAPR1_24_25_DAB_02.jar -f 1 -k -1 -i 'Input/Funcao1/exampleInputFunc1.csv' -j 'Input/Funcao2-3/csv'
java -jar LAPR1_24_25_DAB_02.jar -f 2 -k -1 -i 'Input/Funcao1/exampleInputFunc1.csv' -j 'Input/Funcao2-3/csv'
java -jar LAPR1_24_25_DAB_02.jar -f 3 -k -1 -i 'Input/Funcao3/csv/image_140.csv' -j 'Input/Funcao2-3/csv'
java -jar LAPR1_24_25_DAB_02.jar -f 4 -k -1 -i 'Input/Funcao2-3/csv/image_001.csv' -j 'Input/Funcao2-3/csv'
```