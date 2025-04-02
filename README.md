# Integrative Project - LAPR1 (Laboratory and Project I)

## **About:**
This project involves the development of a Java application for image compression, random generation, reconstruction, and facial recognition using the ***Eigenfaces algorithm***. Since this project is part of the first semester of Computer Engineering, we face the challenge of developing this software in just one class and one branch.

## **Scenario:**
The project to be developed this academic year is a software application for the **European Intelligence Agency (EIA)** that allows the representation and identification of individuals using eigenfaces (eigenvectors) (Turk & Pentland, 1991), derived from a set of human face images/photos. The EIA possesses a large image database that it does not intend to share and seeks to find a compact representation of this database that can be distributed across all affiliated European institutions to identify individuals in the countries where they operate. To achieve its goals, the EIA wants the system's core to be the decomposition of an image/matrix into eigenvalues and eigenvectors, particularly *the eigenfaces*. The EIA also wants to perform a sensitivity study on the number of eigenfaces (eigenvalues and eigenvectors) to be used in the decomposition of each image.

## **Application Features and Algorithms:**
Since a grayscale image can be represented as a matrix, and to facilitate the development and evaluation of the project, the EIA defines three tasks/features to be included in the application:
 1. Eigen decomposition (Larson, 2012; Ghaoui L., 2024) of a symmetric matrix;
 2. Reconstruction of each image (grayscale image (0-255)) using the most relevant eigenfaces (Turk & Pentland, 1991);
 3. Given an image, identify the closest/most similar image in the image database using the weights of a set of eigenfaces.
 4. Random image generation: Generate random images by combining eigenfaces in a way that creates new synthetic images, allowing for the exploration of various facial variations based on the eigenfaces in the dataset.
    
For the execution of these tasks, it is also necessary to implement modules for reading and writing matrices/images stored in folders, as well as modules for calculating a set of statistics and metrics.

## **Application Structure**
The client requested that the program allow for either an interactive or non-interactive mode (with or without arguments when running the program). If the non-interactive mode is chosen, the system should **verify the output generated after each execution** when the same function is selected again. This is because the .csv and .txt files containing all the output that would be presented on the console (in non-interactive mode) will be overwritten. However, the images are not overwritten, allowing them to be compared.

## **Command Guide for Program Usage: [UsageGuide](UsageGuide.md)**


## **Development Team - TechTitans - LAPR1_24_25_DAB_02**
| Name                                  | Student number|
|-----------------------------------------|-----------|
| Rita Mafalda Martins de Oliveira        | 1240729   |
| Alexandre Pereira Henrique              | 1240720   |
| Rafael Pinto Vieira                     | 1241286   |
| Luiz Gabriel de Souza Sargaço Teixeira  | 1230350   |

```plaintext
LAPR1_24_25_DAB_02
├── code
│   ├── src
│   │   └── LAPR1_24_25_DAB_02.java       # Código principal da aplicação.
│   │   └── Demais classes .java          # Classes dos demais integrantes do grupo.
│   └── lib                                
│       └── commons-math3-3.6.1           # Bibliotecas não nativas utilizadas.
│
├── docs                                  # Documentação do projeto.
│   ├── enunciado                         # Diretório com as versões do enunciado do projeto.
│   │   └── EnunciadoLAPR1_v0.pdf         # Arquivo do enunciado do projeto.
│   ├── outputInterativo.txt              # Documento com toda a saída do programa em modo interativo.
│   └── outputNaoInterativo.txt           # Documento com todos os comandos do programa em modo não interativo e se foram bem sucedidos ou não.
│
├── Output                                # Diretório de todos os outputs gerados pelo programa. 
│       ├── Func1                         # Diretório com os outputs da função 1. As matrizes reconstruídas serão salvas aqui .csv .
│       ├── Func2                         # Diretório com os outputs da função 2.
│       │    ├── Eigenfaces               # Aqui serão salvas as matrizes reconstruídas em extensão .csv .
│       │    └── ImagensReconstruidas     # Aqui serão salvas as imagens reconstruídas em extensão .jpg .
│       ├── Func3
│       │    └── Identificacao            # Aqui serão salvas as imagens que tiveram a menor distância euclidiana, no diretório identificadas em extensão .jpg .
│       ├── Func4                         # Diretório com os outputs da função 4. As imagens geradas aleatoriamente serão salvas aqui .jpg .
│       └── NaoInterativo                 # Diretório com os outputs do modo não interativo. Serão salvos arquivos no formato .txt dentro do diretório de cada função.
│           ├── Func1
│           ├── Func2
│           ├── Func3
│           └── Func4
│
└── README.md                             # Arquivo README do projeto
```
