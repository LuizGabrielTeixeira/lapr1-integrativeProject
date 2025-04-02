import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
//* NOTE: The code has been divided into several parts to facilitate reading and understanding.
//* Collapse all for a better view.
//* Use the keyboard shortcut Ctrl + F and search for "//*" to navigate between parts of the code.


public class LAPR1_24_25_DAB_02 {
    //* Constantes para limites de tamanho.
    public static final int MAX_SIZE_ROWS_AND_COLS = 256;
    public static final int MIN_SIZE_ROWS_AND_COLS = 1;
    public static final int MIN_QUANTITY_VECTORS = 1;
    public static final int CODE_FOR_ALL_VECTORS = -1;
    public static final int MIN_BIT_VALUE = 0;
    public static final int MAX_BIT_VALUE = 255;
    public static final double MIN_LAMBDA_VALUE = 1e-8;
    public static final double MIN_DECIMAL_VALUE = 1e-2;

    //* Scanner global para ser utilizado em todos os métodos necessários.
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Verificação de parâmetros para decidir entre interativo e não interativo.
        if (checkCorrectParametersStructure(args)) {
            runNonInteractive(args);
        } else {
            runInteractive();
        }
        scanner.close();
    }

    //* ------------------ Modos de execução ------------------
    public static void runInteractive() {
        int function;

        // Roda enquanto a função for inválida
        function = verifyFunction();

        switch (function) {
            case 1:
                function1(function);
                break;
            case 2:
                function2(function);
                break;
            case 3:
                function3(function);
                break;
            case 4:
                function4(function);
                break;
            case 5:
                devTeam();
                break;
            case 0:
                quitApplication();
                break;
        }
    }

    public static void runNonInteractive(String[] args) {
        int function;
        int vectorNumbers;
        String csvLocation;
        String dataBaseLocation;

        function = receiveFunction(args);
        vectorNumbers = receiveNumberVectors(args);
        csvLocation = receiveCsvLocation(args, function);
        dataBaseLocation = receiveDataBaseLocation(args);

        checkExistenceFileDirectory(csvLocation);

        String[] csvFiles = getCSVFileNames(dataBaseLocation);

        double[][] oneMatrixCsv = readCSVToMatrix(csvLocation);

        double[][][] allMatricesCsv = getMatricesFromCsvFolder(dataBaseLocation);

        try {
            String filePath = "Output/NaoInterativo/Func" + function;

            File file = new File(filePath, "/outputFunc" + function + ".txt");

            System.setOut(new PrintStream(new FileOutputStream(file)));

        } catch (FileNotFoundException e) {
            errorGeneral("Erro ao criar o arquivo de saída: " + e.getMessage());
        }

        runNonInteractiveOutputs(function, vectorNumbers, csvLocation, csvFiles, oneMatrixCsv, allMatricesCsv, dataBaseLocation);
    }
    //* ------------------ Fim modos de execução ------------------


    //* ------------------ Métodos principais ------------------
    public static void runNonInteractiveOutputs(int function, int vectorNumbers, String csvLocation, String[] csvFiles, double[][] oneMatrixCsv, double[][][] allMatricesCsv, String dataBaseLocation) {
        double[][] linearizedImages = new double[allMatricesCsv[0].length * allMatricesCsv[0].length][allMatricesCsv.length];
        populateLinearizedImages(linearizedImages, allMatricesCsv);
        double[] averageVectors = calculateMeanVector(linearizedImages);
        double[][] phi = centralizeImages(linearizedImages, averageVectors);
        int vectorK = validateEigenVectors(linearizedImages, vectorNumbers);

        double[][] phiT = transposeMatrix(phi);
        double[][] phiTxPhi = multiplyMatrices(phiT, phi);
        double[][] eigenVectors = getEigenVectors(phiTxPhi);
        double[][] eigenValues = getEigenValues(phiTxPhi);
        double[][] selectedColumnsK = getValuesAndIndexArray(eigenValues, vectorK);
        double[][] newEigenVectorsK = createSubMatrix(eigenVectors, selectedColumnsK);
        double[][] expandedVectorsK = multiplyMatrices(phi, newEigenVectorsK);
        double[][] eigenfaces = normalize(expandedVectorsK);
        double[][] weightsMatrix = new double[eigenfaces[0].length][allMatricesCsv.length];


        populateWeightsMatrix(weightsMatrix, phi, eigenfaces);

        switch (function) {
            case 1:
                printHeaderFunction("Decomposição Própria de uma Matriz Simétrica");
                decomposeSymmetricMatrix(vectorNumbers, csvLocation);
                System.out.println();
                System.out.println("Funcionalidade 1 finalizada.");
                break;
            case 2:
                printHeaderFunction("Reconstrução de Imagens usando Eigenfaces");
                reconstructImagesWithEigenfaces(vectorK, csvFiles, averageVectors, eigenfaces, linearizedImages, weightsMatrix, allMatricesCsv, function, phiTxPhi);
                System.out.println();
                System.out.println("Funcionalidade 2 finalizada.");
                break;
            case 3:
                printHeaderFunction("Identificação de imagem mais próxima");
                identifyClosestImage(vectorK, csvFiles, averageVectors, eigenfaces, oneMatrixCsv, weightsMatrix, allMatricesCsv, function);
                System.out.println();
                System.out.println("Funcionalidade 3 finalizada.");
                break;
            case 4:
                printHeaderFunction("Gerar uma imagem aleatória com Eigenfaces");
                generateNewImage(vectorNumbers, dataBaseLocation, function);
                System.out.println();
                System.out.println("Funcionalidade 4 finalizada.");
                break;

        }
    }

    public static void function1(int function) {
        int vectorNumbers = verifyVectorNumbers();
        String csvLocation = verifyCsvLocation(function);

        printHeaderFunction("Decomposição Própria de uma Matriz Simétrica:");

        decomposeSymmetricMatrix(vectorNumbers, csvLocation);

        System.out.println();
        System.out.println("Funcionalidade 1 finalizada, a retornar ao menu inicial.");

        runInteractive();
    }

    public static void function2(int function) {
        int vectorNumbers = verifyVectorNumbers();
        String dataBase = verifyDataBaseLocation();

        printHeaderFunction("Reconstrução de Imagens utilizando Eigenfaces");

        calculateFunction2(vectorNumbers, dataBase, function);

        System.out.println();
        System.out.println("Funcionalidade 2 finalizada, a retornar ao menu inicial.");

        runInteractive();
    }

    public static void function3(int function) {
        int vectorNumbers = verifyVectorNumbers();
        String csvLocation = verifyCsvLocation(function);
        String dataBase = verifyDataBaseLocation();

        printHeaderFunction("Identificação da imagem mais próxima utilizando Eigenfaces");

        calculateFunction3(vectorNumbers, csvLocation, dataBase, function);

        System.out.println();
        System.out.println("Funcionalidade 3 finalizada, a retornar ao menu inicial.");

        runInteractive();
    }

    public static void function4(int function) {
        int vectorNumbers = verifyVectorNumbers();
        String dataBase = verifyDataBaseLocation();

        printHeaderFunction("Gerar uma imagem aleatória com Eigenfaces");

        generateNewImage(vectorNumbers, dataBase, function);

        System.out.println();
        System.out.println("Funcionalidade 4 finalizada, a retornar ao menu inicial.");

        runInteractive();
    }

    public static void devTeam() {
        printHeaderFunction("Desenvolvido por: TechTitans!");

        uiDevTeam();

        System.out.println();
        System.out.println("A retornar ao menu inicial.");

        runInteractive();
    }
    //* ------------------ fim dos métodos principais ------------------


    //* ------------------ Métodos de distribuição de tarefas ------------------
    public static void decomposeSymmetricMatrix(int vectorNumbers, String csvLocation) {
        double[][] oneMatrixCsv = readCSVToMatrix(csvLocation);

        double[][] eigenVectors = getEigenVectors(oneMatrixCsv);
        double[][] eigenValues = getEigenValues(oneMatrixCsv);

        int vectorK = validateEigenVectors(oneMatrixCsv, vectorNumbers);

        double[][] valuesAndIndexArray = getValuesAndIndexArray(eigenValues, vectorK);
        double[][] newEigenVectorsK = createSubMatrix(eigenVectors, valuesAndIndexArray);
        double[][] newEigenValuesK = constructDiagonalMatrix(valuesAndIndexArray);
        double[][] newEigenVectorsTransposeK = transposeMatrix(newEigenVectorsK);
        double[][] matrixEigenFaces = multiplyMatrices(multiplyMatrices(newEigenVectorsK, newEigenValuesK), newEigenVectorsTransposeK);

        double maximumAbsolutError = calculateMAE(oneMatrixCsv, matrixEigenFaces);
        maximumAbsolutError = adjustValue(maximumAbsolutError);

        printFunction1(vectorK, newEigenValuesK, newEigenVectorsK, maximumAbsolutError, matrixEigenFaces);
        saveMatrixToFile(matrixEigenFaces, csvLocation, "Output/Func1", 1);
    }

    public static void calculateFunction2(int vectorNumbers, String dataBase, int function) {
        String[] csvFiles = getCSVFileNames(dataBase);
        double[][][] allMatricesCsv = getMatricesFromCsvFolder(dataBase);

        double[][] linearizedImages = new double[allMatricesCsv[0].length * allMatricesCsv[0].length][allMatricesCsv.length];
        populateLinearizedImages(linearizedImages, allMatricesCsv);
        double[] averageVectors = calculateMeanVector(linearizedImages);
        double[][] phi = centralizeImages(linearizedImages, averageVectors);
        int vectorK = validateEigenVectors(linearizedImages, vectorNumbers);

        double[][] phiT = transposeMatrix(phi);
        double[][] phiTxPhi = multiplyMatrices(phiT, phi);
        double[][] eigenVectors = getEigenVectors(phiTxPhi);
        double[][] eigenValues = getEigenValues(phiTxPhi);
        double[][] selectedColumnsK = getValuesAndIndexArray(eigenValues, vectorK);
        double[][] newEigenVectorsK = createSubMatrix(eigenVectors, selectedColumnsK);
        double[][] expandedVectorsK = multiplyMatrices(phi, newEigenVectorsK);
        double[][] eigenfaces = normalize(expandedVectorsK);
        double[][] weightsMatrix = new double[eigenfaces[0].length][allMatricesCsv.length];


        populateWeightsMatrix(weightsMatrix, phi, eigenfaces);

        reconstructImagesWithEigenfaces(vectorK, csvFiles, averageVectors, eigenfaces, linearizedImages, weightsMatrix, allMatricesCsv, function, phiTxPhi);
    }

    public static void reconstructImagesWithEigenfaces(int vectorNumbers, String[] csvFiles, double[] averageVectors, double[][] eigenfaces, double[][] linearizedImages, double[][] weightsMatrix, double[][][] allMatricesCsv, int function, double[][] AtxA) {

        printVector("Valores do vetor médio :", averageVectors);
        printMatrix(AtxA, "AtxA ", true);
        System.out.println("\nQuantidade de Eigenfaces utilizadas:  " + vectorNumbers);

        for (int img = 0; img < linearizedImages[0].length; img++) {
            double[] columnWeights = getColumn(weightsMatrix, img);
            double[] reconstructedImage = reconstructImage(averageVectors, eigenfaces, columnWeights, vectorNumbers);
            double[][] reconstructedImageMatrix = array1DToMatrix(reconstructedImage, allMatricesCsv[img]);
            double maximumAbsolutError = calculateMAE(allMatricesCsv[img], reconstructedImageMatrix);
            maximumAbsolutError = adjustValue(maximumAbsolutError);
            System.out.print("\nPara a imagem: " + csvFiles[img]);
            printVector(", foi utilizado este vetor peso :", columnWeights);
            System.out.printf("O erro absoluto médio dessa imagem com sua original foi: %.2f\n", maximumAbsolutError);
            saveImage(reconstructedImageMatrix, csvFiles[img], "Output/Func2/ImagensReconstruidas", function);
            saveMatrixToFile(reconstructedImageMatrix, csvFiles[img], "Output/Func2/Eigenfaces", 0);
        }
    }

    public static void calculateFunction3(int vectorNumbers, String csvLocation, String dataBase, int function) {
        String[] csvFiles = getCSVFileNames(dataBase);
        double[][][] allMatricesCsv = getMatricesFromCsvFolder(dataBase);
        double[][] oneMatrixCsv = readCSVToMatrix(csvLocation);


        double[][] linearizedImages = new double[allMatricesCsv[0].length * allMatricesCsv[0].length][allMatricesCsv.length];
        populateLinearizedImages(linearizedImages, allMatricesCsv);
        double[] averageVectors = calculateMeanVector(linearizedImages);
        double[][] phi = centralizeImages(linearizedImages, averageVectors);
        int vectorK = validateEigenVectors(linearizedImages, vectorNumbers);

        double[][] phiT = transposeMatrix(phi);
        double[][] phiTxPhi = multiplyMatrices(phiT, phi);
        double[][] eigenVectors = getEigenVectors(phiTxPhi);
        double[][] eigenValues = getEigenValues(phiTxPhi);
        double[][] selectedColumnsK = getValuesAndIndexArray(eigenValues, vectorK);
        double[][] newEigenVectorsK = createSubMatrix(eigenVectors, selectedColumnsK);
        double[][] expandedVectorsK = multiplyMatrices(phi, newEigenVectorsK);
        double[][] eigenfaces = normalize(expandedVectorsK);
        double[][] weightsMatrix = new double[eigenfaces[0].length][allMatricesCsv.length];


        populateWeightsMatrix(weightsMatrix, phi, eigenfaces);

        identifyClosestImage(vectorK, csvFiles, averageVectors, eigenfaces, oneMatrixCsv, weightsMatrix, allMatricesCsv, function);
    }

    public static void identifyClosestImage(int vectorNumbers, String[] csvFiles, double[] averageVectors, double[][] eigenfaces, double[][] oneMatrixCsv, double[][] weightsMatrix, double[][][] allMatricesCsv, int function) {
        double[] linearizedPrincipalImage = matrixToArray1D(oneMatrixCsv);
        double[] phiPrincipalImage = subtractionColumns(linearizedPrincipalImage, averageVectors);

        double[] principalWeightsVector = calculateWeights(phiPrincipalImage, eigenfaces);

        double[] distances = calculateEuclideanDistance(principalWeightsVector, weightsMatrix);
        int[] closestImageIndex = checkCloserVetor(distances);

        System.out.println("O número de vetores próprios utilizados: " + vectorNumbers + "\n");
        printVector("Novo vetor Omega (Ω nova) :", principalWeightsVector);

        int counter = 0;
        for (int i = 0; closestImageIndex[i] != Integer.MAX_VALUE; i++) {
            counter++;
        }

        for (int i = 0; i < counter; i++) {
            double[] closestImageWeights = getColumn(weightsMatrix, closestImageIndex[i]);
            double[] reconstructedImage = reconstructImage(averageVectors, eigenfaces, closestImageWeights, vectorNumbers);
            double[][] reconstructedImageMatrix = array1DToMatrix(reconstructedImage, allMatricesCsv[0]);

            printFunction3(csvFiles, closestImageIndex[i], distances, counter, i, closestImageWeights, weightsMatrix);
            saveImage(reconstructedImageMatrix, csvFiles[closestImageIndex[i]], "Output/Func3/Identificacao", function);
        }
    }

    public static void generateNewImage(int vectorNumbers, String dataBase, int function) {
        double[][][] allMatricesCsv = getMatricesFromCsvFolder(dataBase);
        double[][] linearizedImages = new double[allMatricesCsv[0].length * allMatricesCsv[0].length][allMatricesCsv.length];
        populateLinearizedImages(linearizedImages, allMatricesCsv);
        double[] meanVector = calculateMeanVector(linearizedImages);
        double[][] phi = centralizeImages(linearizedImages, meanVector);
        int vectorK = validateEigenVectors(linearizedImages, vectorNumbers);

        double[][] phiT = transposeMatrix(phi);
        double[][] phiTxPhi = multiplyMatrices(phiT, phi);
        double[][] eigenVectors = getEigenVectors(phiTxPhi);
        double[][] eigenValues = getEigenValues(phiTxPhi);
        double[][] selectedColumnsK = getValuesAndIndexArray(eigenValues, vectorK);
        double[][] newEigenVectorsK = createSubMatrix(eigenVectors, selectedColumnsK);
        double[][] newEigenValuesK = constructDiagonalMatrix(selectedColumnsK);
        double[][] expandedVectorsK = multiplyMatrices(phi, newEigenVectorsK);
        double[][] eigenfaces = normalize(expandedVectorsK);
        double[][] weightsMatrix = new double[eigenfaces[0].length][allMatricesCsv.length];


        populateWeightsMatrix(weightsMatrix, phi, eigenfaces);
        int dimension = meanVector.length;
        double[] newImage = creationImage(dimension, meanVector, vectorK, newEigenValuesK, eigenfaces);
        double[][] newImageMatrix = array1DToMatrix(newImage, allMatricesCsv[0]);
        System.out.println("A quantidade de Eigenfaces selecionadas para a variável K foi: " + vectorK + "\n");
        saveImage(newImageMatrix, "Input/Funcao2-3/csv", "Output/Func4", function);
    }
    //* ------------------ Fim dos métodos de distribuição de tarefas ------------------


    //* ------------------ Métodos de cálculos ------------------
    public static EigenDecomposition decomposeMatrix(double[][] matrixToDecompose) {
        Array2DRowRealMatrix decomposedMatrix = new Array2DRowRealMatrix(matrixToDecompose);
        return new EigenDecomposition(decomposedMatrix);
    }

    public static void quitApplication() {
        uiQuitParameterMenu();
        receiveExitConfirmation(null);
    }

    public static void populateWeightsMatrix(double[][] weightsMatrix, double[][] phi, double[][] eigenfaces) {
        for (int img = 0; img < phi[0].length; img++) {
            double[] actualPhiColumn = getColumn(phi, img);
            double[] weights = calculateWeights(actualPhiColumn, eigenfaces);

            for (int i = 0; i < weights.length; i++) {
                weightsMatrix[i][img] = weights[i];
            }
        }
    }

    public static void populateLinearizedImages(double[][] linearizedImages, double[][][] imageMatrices) {
        int expectedLength = imageMatrices[0].length * imageMatrices[0][0].length;
        for (int img = 0; img < imageMatrices.length; img++) {
            if (imageMatrices[img].length * imageMatrices[img][0].length != expectedLength) {
                errorGeneral("Todas as matrizes devem ter o mesmo tamanho.");
            }
            double[] linearizedMatrix = matrixToArray1D(imageMatrices[img]);
            for (int i = 0; i < linearizedMatrix.length; i++) {
                linearizedImages[i][img] = linearizedMatrix[i];
            }
        }
    }

    public static int validateEigenVectors(double[][] matrix, int vectorNumbers) {
        if (vectorNumbers < MIN_QUANTITY_VECTORS || vectorNumbers > matrix[0].length) {
            vectorNumbers = matrix[0].length;
        }
        return vectorNumbers;
    }

    public static double calculateMAE(double[][] originalMatrix, double[][] matrixEigenFaces) {
        int rows = originalMatrix.length;
        int columns = originalMatrix[0].length;
        double errorAbsMed = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                errorAbsMed += Math.abs(originalMatrix[i][j] - matrixEigenFaces[i][j]);
            }
        }
        return errorAbsMed / (rows * columns);
    }

    public static double[] getColumn(double[][] matrix, int column) {
        double[] columnData = new double[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            columnData[i] = matrix[i][column];
        }
        return columnData;
    }

    public static double[] matrixToArray1D(double[][] matrix) {
        int rows = matrix.length;
        int columns = matrix[0].length;
        double[] array = new double[rows * columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                array[i * columns + j] = matrix[i][j];
            }
        }
        return array;
    }

    public static double[] calculateWeights(double[] phi, double[][] eigenfaces) {
        if (phi.length != eigenfaces.length) {
            errorGeneral("Para calcular os pesos o comprimento de 'phi' deve ser igual a quantidade de linhas da matriz 'eigenfaces'.");
        }

        double[] weights = new double[eigenfaces[0].length];

        for (int j = 0; j < eigenfaces[0].length; j++) {
            weights[j] = 0;
            for (int i = 0; i < eigenfaces.length; i++) {
                weights[j] += phi[i] * eigenfaces[i][j];
            }
        }
        return weights;
    }

    public static double[] calculateMeanVector(double[][] linearizedImages) {
        int numPixels = linearizedImages.length;
        int numImages = linearizedImages[0].length;
        double[] meanVector = new double[numPixels];

        for (int i = 0; i < numPixels; i++) {
            double sum = 0;
            for (int j = 0; j < numImages; j++) {
                sum += linearizedImages[i][j];
            }
            meanVector[i] = sum / numImages;
        }
        return meanVector;
    }

    public static double[][] centralizeImages(double[][] images, double[] meanVector) {
        int numPixels = meanVector.length;
        int numImages = images[0].length;
        if (images.length != numPixels) {
            errorGeneral("Para centralizar a imagem o número de pixels na matriz de imagens deve ser igual ao tamanho do vetor médio.");
        }

        double[][] phi = new double[numPixels][numImages];

        for (int i = 0; i < numPixels; i++) {
            for (int j = 0; j < numImages; j++) {
                phi[i][j] = images[i][j] - meanVector[i];
            }
        }

        return phi;
    }

    public static double[][] getEigenVectors(double[][] matrix) {
        EigenDecomposition eigenDecomposition = decomposeMatrix(matrix);
        RealMatrix eigenVectors = eigenDecomposition.getV();
        return eigenVectors.getData();
    }

    public static double[][] getEigenValues(double[][] matrix) {
        EigenDecomposition eigenDecomposition = decomposeMatrix(matrix);
        RealMatrix eigenValues = eigenDecomposition.getD();
        return eigenValues.getData();
    }

    public static double[][] constructDiagonalMatrix(double[][] matrixValuesK) {
        double[][] matrixValuesKPrint = new double[matrixValuesK.length][matrixValuesK.length];
        for (int i = 0; i < matrixValuesK.length; i++) {
            matrixValuesKPrint[i][i] = matrixValuesK[i][0];
        }
        return matrixValuesKPrint;
    }

    public static double[][] getValuesAndIndexArray(double[][] eigenValuesArray, int eigenfaces) {
        double[][] valuesAndIndexArray = new double[eigenfaces][2];

        for (int i = 0; i < valuesAndIndexArray.length; i++) {
            valuesAndIndexArray[i][0] = Double.MIN_VALUE;
        }

        for (int i = 0; i < eigenValuesArray.length; i++) {
            double absValue = Math.abs(eigenValuesArray[i][i]);
            for (int j = 0; j < valuesAndIndexArray.length; j++) {
                if (absValue > Math.abs(valuesAndIndexArray[j][0])) {
                    // Joga os valores para a direita se encontrar um valor maior mais ao fim da matrix
                    for (int l = valuesAndIndexArray.length - 1; l > j; l--) {
                        valuesAndIndexArray[l][0] = valuesAndIndexArray[l - 1][0];
                        valuesAndIndexArray[l][1] = valuesAndIndexArray[l - 1][1];
                    }
                    valuesAndIndexArray[j][0] = eigenValuesArray[i][i];
                    valuesAndIndexArray[j][1] = i;
                    break;
                }
            }
        }

        return valuesAndIndexArray;
    }

    public static double[][] array1DToMatrix(double[] reconstructedImage, double[][] matrixSizeExample) {
        int rows = matrixSizeExample.length;
        int columns = matrixSizeExample[0].length;
        if (reconstructedImage.length != rows * columns) {
            errorGeneral("O tamanho do vetor não corresponde às dimensões da matriz.");
        }

        double[][] reconstructedMatrix = new double[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                reconstructedMatrix[i][j] = reconstructedImage[i * columns + j];
            }
        }
        return reconstructedMatrix;
    }

    public static double[][] normalize(double[][] eigenVectorsATxA) {
        for (int i = 0; i < eigenVectorsATxA[0].length; i++) {
            double norm = 0;

            for (double[] doubles : eigenVectorsATxA) {
                norm += doubles[i] * doubles[i];
            }
            norm = Math.sqrt(norm);

            for (int j = 0; j < eigenVectorsATxA.length; j++) {
                eigenVectorsATxA[j][i] /= norm;
            }
        }
        return eigenVectorsATxA;
    }

    public static double[] reconstructImage(double[] averageVector, double[][] eigenfaces, double[] columnWeights, int quantityEigenfaces) {
        double[] reconstructed = new double[averageVector.length];
        for (int i = 0; i < averageVector.length; i++) {
            reconstructed[i] = averageVector[i];
        }

        for (int j = 0; j < quantityEigenfaces; j++) {
            for (int i = 0; i < eigenfaces.length; i++) {
                reconstructed[i] += columnWeights[j] * eigenfaces[i][j];
            }
        }
        return reconstructed;
    }

    public static double[] calculateEuclideanDistance(double[] principalVector, double[][] weightsMatrix) {
        if (principalVector.length != weightsMatrix.length) {
            errorGeneral("O comprimento do vetor principal não corresponde ao número de linhas da matriz de pesos.");
        }

        double[] result = new double[weightsMatrix[0].length];
        for (int i = 0; i < weightsMatrix[0].length; i++) {
            double sum = 0;
            for (int j = 0; j < weightsMatrix.length; j++) {
                sum += Math.pow(principalVector[j] - weightsMatrix[j][i], 2);
            }
            result[i] = Math.sqrt(sum);
        }
        return result;
    }

    public static int[] checkCloserVetor(double[] distances) {
        double minDistance = Double.MAX_VALUE;
        int[] closestImageIndex = new int[distances.length];
        fillArrayMax(closestImageIndex);

        int j = 0;
        for (int i = 0; i < distances.length; i++) {
            if (distances[i] < minDistance) {
                minDistance = distances[i];
                j = 1;
                closestImageIndex = new int[distances.length];
                fillArrayMax(closestImageIndex);
                closestImageIndex[0] = i;
            } else if (distances[i] == minDistance) {
                closestImageIndex[j] = i;
                j++;
            }
        }
        return closestImageIndex;
    }

    public static double[] creationImage(int dimension, double[] meanVector, int k, double[][] lambdas, double[][] eigenfaces) {
        double[] newImage = new double[dimension];
        for (int i = 0; i < dimension; i++) {
            newImage[i] = meanVector[i];
        }
        for (int i = 0; i < k; i++) {
            adjustPrecision(lambdas);
            double weightsImage = Math.random() * (2 * Math.sqrt(lambdas[i][i])) - Math.sqrt(lambdas[i][i]);
            for (int j = 0; j < dimension; j++) {
                newImage[j] += weightsImage * eigenfaces[j][i];
            }
        }

        return newImage;
    }
    //* ------------------ Fim dos métodos de cálculos ------------------


    //* ------------------ Métodos de entrada e saída ------------------
    public static void writeArrayAsImage(int[][] array, String outputFilePath) throws IOException {
        int height = array.length;
        int width = array[0].length;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int intensity = array[y][x];
                if (checkValuesLimits(intensity)) {
                    errorGeneral("Erro: Na normalização dos pixels, a intensidade do pixel deve estar entre 0 e 255.");
                }
                int rgb = (intensity << 16) | (intensity << 8) | intensity;
                image.setRGB(x, y, rgb);
            }
        }

        File outputFile = new File(outputFilePath);
        ImageIO.write(image, "jpg", outputFile);
    }

    public static double[] findMinMaxValues(double[][] imageArray) {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;

        for (double[] row : imageArray) {
            for (double val : row) {
                if (val < min) min = val;
                if (val > max) max = val;
            }
        }

        return new double[]{min, max};
    }

    public static int[][] normalizeImage(double[][] imageArray, double min, double max) {
        int height = imageArray.length;
        int width = imageArray[0].length;
        int[][] normalizedImage = new int[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                normalizedImage[y][x] = (int) ((imageArray[y][x] - min) / (max - min) * MAX_BIT_VALUE);
            }
        }

        return normalizedImage;
    }

    public static void saveImage(double[][] imageArray, String inputCsvPath, String outputFolderPath, int function) {
        double[] minMaxValues = findMinMaxValues(imageArray);
        double min = minMaxValues[0];
        double max = minMaxValues[1];

        int[][] normalizedImage = normalizeImage(imageArray, min, max);

        String jpgFileName;
        if (function == 2 || function == 3) {
            jpgFileName = new File(inputCsvPath).getName().replace(".csv", ".jpg");
        } else if (function == 4) {
            jpgFileName = "RandomImage.jpg";
        } else {
            errorGeneral("Função inválida para salvar a imagem.");
            return;
        }

        int counter = 1;
        String outputPath = outputFolderPath + "/" + jpgFileName;
        File file = new File(outputPath);
        while (file.exists()) {
            String baseName = jpgFileName.replace(".jpg", "");
            outputPath = outputFolderPath + "/" + baseName + "(" + counter + ").jpg";
            file = new File(outputPath);
            counter++;
        }


        File outputFolder = new File(outputFolderPath);
        if (!outputFolder.exists()) {
            if (!outputFolder.mkdirs()) {
                errorGeneral("Falha ao criar o diretório: " + outputFolderPath);
                return;
            }
        }

        try {
            writeArrayAsImage(normalizedImage, outputPath);
            System.out.println("A imagem foi gerada com sucesso: " + outputPath);
        } catch (IOException e) {
            errorGeneral("Erro ao salvar a imagem: " + e.getMessage());
        }
    }

    public static void saveMatrixToFile(double[][] matrix, String inputCsvPath, String outputFolderPath, int printOrNot) {
        String csvFileName = new File(inputCsvPath).getName();
        String newFileName = "Reconstruct-" + csvFileName;

        File file = new File(outputFolderPath + "/" + newFileName);


        try (PrintWriter writer = new PrintWriter(file)) {
            for (double[] row : matrix) {
                String rowString = String.join(" , ", Arrays.stream(row)
                        .mapToObj(val -> String.format("%.0f", val))
                        .toArray(String[]::new));
                writer.println(rowString);
            }
            if (printOrNot == 1) {
                System.out.println("\nArquivo CSV criado com sucesso: " + file.getName());
            }
        } catch (IOException e) {
            errorGeneral("Erro ao salvar a matriz no arquivo: " + e.getMessage());
        }
    }

    public static String[] getCSVFileNames(String folderLocation) {
        File folder = new File(folderLocation);
        File[] csvFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));
        if (csvFiles == null || csvFiles.length == 0) {
            errorGeneral("Nenhum arquivo CSV encontrado na pasta: " + folderLocation);
        }

        String[] fileNames = new String[csvFiles.length];
        for (int i = 0; i < csvFiles.length; i++) {
            fileNames[i] = csvFiles[i].getName();
        }

        return fileNames;
    }

    public static double[][] readCSVToMatrix(String path) {
        try {
            int[] dimensions = getCsvDimensions(path);
            int rowCount = dimensions[0];
            int columnCount = dimensions[1];

            if (checkSizeBoundaries(rowCount, columnCount)) {
                errorGeneral("O tamanho da matriz não está dentro dos limites permitidos.");
            } else if (checkSquareMatrix(rowCount, columnCount)) {
                errorGeneral("A matriz não é quadrada.");
            }

            double[][] matrix = new double[rowCount][columnCount];
            fillMatrixFromCsv(path, matrix);
            return matrix;
        } catch (Exception e) {
            errorGeneral("Erro ao ler o arquivo CSV: " + e.getMessage());
            return new double[0][]; // Nunca será alcançado
        }
    }

    public static int[] getCsvDimensions(String path) throws FileNotFoundException {
        Scanner lineCounter = new Scanner(new File(path));
        int rowCount = 0;
        int columnCount = 0;

        while (lineCounter.hasNextLine()) {
            String line = lineCounter.nextLine();
            if (!line.trim().isEmpty()) {
                rowCount++;
                if (columnCount == 0) {
                    columnCount = line.split(",").length;
                }
            }
        }
        lineCounter.close();
        return new int[]{rowCount, columnCount};
    }

    public static void fillMatrixFromCsv(String path, double[][] matrix) throws FileNotFoundException {
        Scanner fileScanner = new Scanner(new File(path));
        int row = 0;
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            if (!line.trim().isEmpty()) {
                String[] values = line.split(",");
                for (int col = 0; col < values.length; col++) {
                    if (checkValuesLimits(Double.parseDouble(values[col].trim()))) {
                        errorGeneral("Erro: Os valores da matriz devem estar entre 0 e 255.");
                    }
                    matrix[row][col] = Double.parseDouble(values[col].trim());
                }
                row++;
            }
        }
        fileScanner.close();
    }

    public static double[][][] getMatricesFromCsvFolder(String folderLocation) {
        File folder = new File(folderLocation);
        File[] csvFiles = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));
        if (csvFiles == null || csvFiles.length == 0) {
            errorGeneral("Nenhum arquivo CSV encontrado na pasta: " + folderLocation);
        }

        for (int i = 0; i < csvFiles.length - 1; i++) {
            for (int j = i + 1; j < csvFiles.length; j++) {
                if (csvFiles[i].getName().compareTo(csvFiles[j].getName()) > 0) {
                    File temp = csvFiles[i];
                    csvFiles[i] = csvFiles[j];
                    csvFiles[j] = temp;
                }
            }
        }

        double[][][] matrices = new double[csvFiles.length][][];

        for (int i = 0; i < csvFiles.length; i++) {
            File csvFile = csvFiles[i];
            matrices[i] = readCSVToMatrix(csvFile.getPath());
        }

        return matrices;
    }
    //* ------------------ Fim dos Métodos de entrada e saída ------------------


    //* ------------------ Verificações ------------------
    public static boolean checkCorrectParametersStructure(String[] parameters) {
        if (parameters.length == 8) {
            return parameters[0].equals("-f") && parameters[2].equals("-k") && parameters[4].equals("-i") && parameters[6].equals("-j");
        }
        return false;
    }

    public static void adjustPrecision(double[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] < MIN_LAMBDA_VALUE) {
                    matrix[i][j] = MIN_LAMBDA_VALUE;
                }
            }
        }
    }

    public static double adjustValue(double value) {
        if (value < 0 && Math.abs(value) < MIN_DECIMAL_VALUE) {
            value = -MIN_DECIMAL_VALUE;
        }
        return value;
    }

    public static boolean checkFunctionOptions(int function) {
        return function >= 1 && function <= 5 || function == 0;
    }

    public static boolean checkVectorsNumbers(int vectorNumbers) {
        return vectorNumbers >= MIN_QUANTITY_VECTORS || vectorNumbers == CODE_FOR_ALL_VECTORS;
    }

    public static boolean checkSizeBoundaries(int rows, int cols) {
        return rows > MAX_SIZE_ROWS_AND_COLS || cols > MAX_SIZE_ROWS_AND_COLS || rows < MIN_SIZE_ROWS_AND_COLS || cols < MIN_SIZE_ROWS_AND_COLS;
    }

    public static boolean checkValuesLimits(double value) {
        return value < MIN_BIT_VALUE || value > MAX_BIT_VALUE;
    }

    public static boolean checkSquareMatrix(int rows, int cols) {
        return rows != cols;
    }

    public static boolean checkCsvLocation(String csvLocation) {
        File csv = new File(csvLocation);
        if (csvLocation.isEmpty()) {
            return false;
        } else if (!csvLocation.contains(".csv")) {
            return false;
        } else return csv.exists();
    }

    public static boolean checkDataBaseLocation(String dataBaseLocation) {
        File imageDirectory = new File(dataBaseLocation);
        if (dataBaseLocation.isEmpty()) {
            return false;
        }
        return imageDirectory.exists();
    }

    public static String verifyCsvLocation(int function) {
        String csvLocation;
        uiCsvLocation();
        csvLocation = receiveCsvLocation(null, function);
        return csvLocation;
    }

    public static String verifyDataBaseLocation() {
        String dataBaseLocation;
        uiDataBase();
        dataBaseLocation = receiveDataBaseLocation(null);
        return dataBaseLocation;
    }

    public static int verifyFunction() {
        int function;
        do {
            uiInitialMenu();
            function = receiveFunction(null);
        } while (!checkFunctionOptions(function));
        return function;
    }

    public static int verifyVectorNumbers() {
        int vectorNumbers;
        do {
            uiVectorNumbers();
            vectorNumbers = receiveNumberVectors(null);
        } while (!checkVectorsNumbers(vectorNumbers));
        return vectorNumbers;
    }

    public static void checkExistenceFileDirectory(String csvLocation) {
        try {
            scanner = new Scanner(new File(csvLocation));
        } catch (FileNotFoundException e) {
            errorGeneral("Erro ao abrir os arquivos: " + e.getMessage());
        }
    }

    public static boolean checkIfIsSymmetric(double[][] matrix) {
        int a = matrix.length;
        for (int i = 0; i < a; i++) {
            for (int j = i + 1; j < a; j++) {
                if (matrix[i][j] != matrix[j][i]) {
                    return false;
                }
            }
        }
        return true;
    }

    public static String verifySymmetricMatrix(String csvLocation, int function) {

        double[][] matrix = readCSVToMatrix(csvLocation);

        while (!checkIfIsSymmetric(matrix)) {
            System.out.println("A matriz não é simétrica.");
            System.out.println("Tentar novamente? (S/N)");
            String answer = scanner.next().toUpperCase();
            if (answer.equals("S")) {
                csvLocation = verifyCsvLocation(function);
                matrix = readCSVToMatrix(csvLocation);
            } else {
                System.out.println("Saindo da aplicação, ainda pode desistir mas retornará ao menu inicial.");
                quitApplication();
            }
        }


        return csvLocation;
    }
    //* ------------------ Fim verificações ------------------


    //* ------------------ Métodos de Interfaces ------------------
    public static void uiInitialMenu() {
        System.out.print("\n+----------------------------------------------------+\n");
        System.out.println("|            Qual função deseja realizar?            |");
        System.out.println("+----------------------------------------------------+");
        System.out.println("|                                                    |");
        System.out.println("| 1 - Decomposição própria de uma matriz simétrica.  |");
        System.out.println("| 2 - Reconstrução de imagens usando Eigenfaces.     |");
        System.out.println("| 3 - Identificação de imagem mais próximas.         |");
        System.out.println("| 4 - Gerar uma imagem aleatória com Eigenfaces.     |");
        System.out.println("| 5 - Conheça a equipa de desenvolvimento!           |");
        System.out.println("| 0 - Deseja sair da aplicação ?                     |");
        System.out.println("+----------------------------------------------------+");
        System.out.print("Opção: ");
    }

    public static void uiDevTeam() {
        System.out.println("+----------------------------------------------------+");
        System.out.println("|             Equipa de desenvolvimento:             |");
        System.out.println("+----------------------------------------------------+");
        System.out.println("|Alexandre Pereira Henrique                          |");
        System.out.println("|Luiz Gabriel de Souza Sargaço Teixeira              |");
        System.out.println("|Rafael Pinto Vieira                                 |");
        System.out.println("|Rita Mafalda Martins de Oliveira                    |");
        System.out.println("+----------------------------------------------------+");
    }

    public static void uiVectorNumbers() {
        System.out.println("----- Quantos vetores próprios deseja utilizar? -----");
        System.out.print("Quantidade: ");
    }

    public static void uiCsvLocation() {
        System.out.println("-- Qual a localização do csv que deseja utilizar? --");
        System.out.print("Localização: ");
    }

    public static void uiDataBase() {
        System.out.println("------ Qual a localização da base de imagens? ------");
        System.out.print("Localização: ");
    }

    public static void uiQuitParameterMenu() {
        System.out.println("-- Tem certeza que deseja sair da aplicação? (S/N) --");
        System.out.print("Opção: ");
    }
    //* --------------------- Fim menus de opções ------------------


    //* ------------------ Receber parâmetros ------------------
    public static int receiveFunction(String[] args) {
        int functionArgs;
        if (args == null) {
            int function = scanner.nextInt();
            if (!checkFunctionOptions(function)) {
                System.out.println("Erro: Opção inválida.");
                System.out.println("Tente novamente.");
            }
            return function;
        } else {
            functionArgs = Integer.parseInt(args[1]);
            if (!checkFunctionOptions(functionArgs)) {
                errorGeneral("Erro: Opção inválida, as opções são: 1 a 4, opções como 5 e 0 estão disponíveis apenas para o modo interativo!.");
            }
            return functionArgs;
        }
    }

    public static int receiveNumberVectors(String[] args) {
        int vectorNumbersArgs;
        if (args == null) {
            vectorNumbersArgs = scanner.nextInt();
            if (vectorNumbersArgs < MIN_QUANTITY_VECTORS && vectorNumbersArgs != CODE_FOR_ALL_VECTORS) {
                System.out.println("Erro: O número de vetores deve ser maior que 0.\nOu insira -1 para utilizar todos os vetores possíveis.");
                System.out.println("Tentar novamente? (S/N)");
                String answer = scanner.next().toUpperCase();
                if (answer.equals("N")) {
                    System.out.println("A sair da aplicação, ainda poderá desistir mas\nretornará ao menu inicial.");
                    quitApplication();
                }
            }
        } else {
            vectorNumbersArgs = Integer.parseInt(args[3]);
            if (vectorNumbersArgs < MIN_QUANTITY_VECTORS && vectorNumbersArgs != CODE_FOR_ALL_VECTORS) {
                errorGeneral("Erro: O número de vetores deve ser maior que 0.\nOu insira -1 para utilizar todos os vetores possíveis.");
            }
            return vectorNumbersArgs;
        }

        if (vectorNumbersArgs == CODE_FOR_ALL_VECTORS) {
            vectorNumbersArgs = MAX_SIZE_ROWS_AND_COLS;
        }
        return vectorNumbersArgs;
    }

    public static String receiveDataBaseLocation(String[] args) {
        if (args == null) {
            return getDataBaseLocationFromUser();
        } else {
            return getDataBaseLocationFromArgs(args);
        }
    }

    public static String getDataBaseLocationFromUser() {
        String dataBaseLocation = scanner.next();
        while (!checkDataBaseLocation(dataBaseLocation)) {
            System.out.println("Erro: Localização da base de imagens inválida.");
            System.out.println("Tentar novamente ? (S/N)");
            String answer = scanner.next().toUpperCase();
            if (answer.equals("S")) {
                dataBaseLocation = verifyDataBaseLocation();
            } else if (answer.equals("N")) {
                System.out.println("Saindo da aplicação, ainda poderá retornar ao menu inicial.");
                quitApplication();
            } else {
                System.out.println("Opção inválida, responda com S/N.");
            }
        }
        return dataBaseLocation;
    }

    public static String getDataBaseLocationFromArgs(String[] args) {
        String dataBaseLocationArgs = args[7];
        if (!checkDataBaseLocation(dataBaseLocationArgs)) {
            errorGeneral("Erro: Localização da base de dados inválida.");
        }
        return dataBaseLocationArgs;
    }

    public static String receiveCsvLocation(String[] args, int function) {
        if (args == null) {
            return getCsvLocationFromUser(function);
        } else {
            return getCsvLocationFromArgs(args, function);
        }
    }

    public static String getCsvLocationFromUser(int function) {
        String csvLocation = scanner.next();
        while (!checkCsvLocation(csvLocation)) {
            System.out.println("Erro: Localização do csv inválida");
            System.out.println("Tentar novamente ? (S/N)");
            String answer = scanner.next().toUpperCase();
            if (answer.equals("S")) {
                csvLocation = verifyCsvLocation(function);
            } else if (answer.equals("N")) {
                System.out.println("Saindo da aplicação, ainda poderá retornar ao menu inicial.");
                quitApplication();
            } else {
                System.out.println("Opção inválida, responda com S/N.");
            }
        }
        if (function == 1) {
            csvLocation = verifySymmetricMatrix(csvLocation, function);
        }
        return csvLocation;
    }

    public static String getCsvLocationFromArgs(String[] args, int function) {
        String csvLocationArgs = args[5];
        if (!checkCsvLocation(csvLocationArgs)) {
            errorGeneral("Erro: Localização do csv inválida");
        } else if (function == 1) {
            csvLocationArgs = verifySymmetricMatrix(csvLocationArgs, function);
        }
        return csvLocationArgs;
    }

    public static void receiveExitConfirmation(String[] args) {
        if (args == null) {
            String confirmeExit;
            do {
                confirmeExit = scanner.next().toUpperCase();
                if (confirmeExit.equals("S")) {
                    System.exit(0);
                } else if (confirmeExit.equals("N")) {
                    System.out.println("A retornar para o menu inicial.");
                    System.out.println();
                    runInteractive();
                } else {
                    System.out.println("Erro: Responda com S/N");
                }
            } while (!confirmeExit.equals("N"));
        }
    }
    //* -------------------- Fim receber parâmetros ------------------


    //* ------------------ Operações básicas com Matrizes ------------------
    public static double[][] multiplyMatrices(double[][] matrizLeft, double[][] matrizRight) {
        double[][] matrizResultante = new double[matrizLeft.length][matrizRight[0].length];
        for (int i = 0; i < matrizLeft.length; i++) {
            for (int j = 0; j < matrizRight[0].length; j++) {
                for (int k = 0; k < matrizRight.length; k++) {
                    matrizResultante[i][j] += matrizLeft[i][k] * matrizRight[k][j];
                }
            }
        }
        return matrizResultante;
    }

    public static double[][] transposeMatrix(double[][] matrix) {
        double[][] transposedMatrix = new double[matrix[0].length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                transposedMatrix[j][i] = matrix[i][j];
            }
        }
        return transposedMatrix;
    }

    public static void fillArrayMax(int[] arrayToFill) {
        for (int i = 0; i < arrayToFill.length; i++) {
            arrayToFill[i] = Integer.MAX_VALUE;
        }
    }

    public static double[][] multiplyMatrixEscalar(double[][] matriz, double escalar) {
        double[][] matrizResultante = new double[matriz.length][matriz[0].length];
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[0].length; j++) {
                matrizResultante[i][j] = matriz[i][j] * escalar;
            }
        }
        return matrizResultante;
    }

    public static double[] subtractionColumns(double[] columnLeft, double[] columnRight) {
        double[] matrixResult = new double[columnLeft.length];
        for (int i = 0; i < columnLeft.length; i++) {
            matrixResult[i] = columnLeft[i] - columnRight[i];
        }

        return matrixResult;
    }

    public static double[][] createSubMatrix(double[][] eigenVectors, double[][] valuesAndIndexArray) {
        for (int i = 0; i < valuesAndIndexArray.length - 1; i++) {
            for (int j = 0; j < valuesAndIndexArray.length - 1 - i; j++) {
                if (valuesAndIndexArray[j][1] > valuesAndIndexArray[j + 1][1]) {
                    double[] temp = valuesAndIndexArray[j];
                    valuesAndIndexArray[j] = valuesAndIndexArray[j + 1];
                    valuesAndIndexArray[j + 1] = temp;
                }
            }
        }

        double[][] subMatrix = new double[eigenVectors.length][valuesAndIndexArray.length];

        for (int i = 0; i < eigenVectors.length; i++) {
            for (int j = 0; j < valuesAndIndexArray.length; j++) {
                subMatrix[i][j] = eigenVectors[i][(int) valuesAndIndexArray[j][1]];
            }
        }

        return subMatrix;
    }
    //* ----------------- Fim operações básicas com matrizes ------------------


    //* ----------------- Métodos para printar -----------------
    public static void printHeaderFunction(String functionName) {
        int length = functionName.length();
        int totalWidth = length + 4; // 2 espaços de cada lado do texto
        int padding = (totalWidth - length) / 2;

        System.out.print("\n+");
        printLine(totalWidth, "-");
        System.out.print("+\n");
        System.out.printf("|%" + padding + "s%s%" + padding + "s|\n+", "", functionName, "");
        printLine(totalWidth, "-");
        System.out.print("+\n");
        System.out.println();
    }

    public static void printMatrix(double[][] matrixToPrint, String matrixName, boolean printInt) {
        System.out.println("\nMatriz: " + matrixName + " ↓");
        printLine(matrixToPrint[0].length, "____________");
        System.out.println();

        for (double[] row : matrixToPrint) {
            System.out.print("|");
            for (int i = 0; i < row.length; i++) {
                double value = adjustValue(row[i]);
                if (printInt) {
                    System.out.printf("%9.0f\t", value);
                } else {
                    System.out.printf("%10.2f\t", value);
                }
                if (i == row.length - 1) {
                    System.out.print("|");
                }
            }
            System.out.println();
        }
        printLine(matrixToPrint[0].length, "============");
        System.out.println();
    }

    public static void printVector(String vectorName, double[] array) {
        System.out.print(vectorName);
        for (int i = 0; i < array.length; i++) {
            double value = adjustValue(array[i]);
            if (i == 0) {
                if (array.length == 1) {
                    System.out.printf("[%.2f]\n", value);
                } else {
                    System.out.printf(" [%.2f; ", value);
                }
            } else if (i == array.length - 1) {
                System.out.printf("%.2f]\n", value);
            } else {
                System.out.printf("%.2f; ", value);
            }
        }
    }

    public static void printVectorTests(double[] vetorToPrint, String vetorName) {
        System.out.println("Vetor: " + vetorName + " ↓");
        System.out.println(" ___________ ");
        for (double v : vetorToPrint) {
            v = adjustValue(v);
            System.out.printf("|%8.2f\t|\n", v);
        }
        System.out.println(" =========== ");
        System.out.println();
    }

    public static void printLine(int length, String pattern) {
        for (int i = 0; i < length; i++) {
            System.out.print(pattern);
        }
    }

    public static void printFunction1(int numbersEigenfaces, double[][] newEigenValuesK, double[][] newEigenVectorsK, double maximumAbsolutError, double[][] reconstructedMatrix) {
        System.out.println("A quantidade de Eigenfaces selecionadas para a variável K foi: " + numbersEigenfaces);
        printMatrix(newEigenValuesK, "Valores Próprios da matriz K", false);
        printMatrix(newEigenVectorsK, "Vetores Próprios matriz K:", false);
        printMatrix(reconstructedMatrix, "Reconstruída", true);
        System.out.printf("\nErro Absoluto Médio: %.2f\n", maximumAbsolutError);
    }

    public static void printFunction3(String[] csvFiles, int closestImageIndex, double[] distances, int counter, int imageIndex, double[] actualVectorOmegaI, double[][] weightsMatrix) {
        if (counter == 1) {
            System.out.printf("\nA imagem mais próxima foi: %s e será armazenada na pasta Identificação!\n", csvFiles[closestImageIndex]);
            System.out.println();
            printDistances(csvFiles, distances, closestImageIndex, counter, actualVectorOmegaI, weightsMatrix);
        } else if (counter > 1 && imageIndex == 0) {
            System.out.println("\nForam identificadas " + counter + " imagens com a mesma distância!\nTodas serão armazenadas na pasta Identificação!\n");
            printDistances(csvFiles, distances, closestImageIndex, counter, actualVectorOmegaI, weightsMatrix);
        }
    }

    public static void printDistances(String[] csvFiles, double[] distances, int closestImageIndex, int counter, double[] actualVectorOmegaI, double[][] weightsMatrix) {
        for (int i = 0; i < csvFiles.length; i++) {
            if (i == closestImageIndex || distances[i] == distances[closestImageIndex]) {
                if (counter == 1) {
                    System.out.printf("Essa foi a imagem mais próxima da solicitada! %s e a sua distância foi: %.1f\n", csvFiles[i], distances[i]);
                } else {
                    System.out.printf("Essa foi uma das " + counter + " imagens mais próximas da solicitada! %s e a sua distância foi: %.1f\n", csvFiles[i], distances[i]);
                }
                printVector("E o seu vetor Ômega (Ωi) foi:", actualVectorOmegaI);
                System.out.println();
            } else {
                System.out.printf("Distância euclidiana para a imagem %s: %.1f\n", csvFiles[i], distances[i]);
                double[] omegaI = getColumn(weightsMatrix, i);
                printVector("E o vetor Ômega (Ωi) da imagem " + csvFiles[i] + " foi:", omegaI);
                System.out.println();
            }
        }
    }
    //* ----------------- Fim métodos para printar -----------------------


    //! ------------------ Error Messages para não interativo ------------------
    public static void errorGeneral(String error) {
        //! Esse tipo de mensagem de erro deve ser usado apenas para o modo não interativo!
        System.err.println(error);
        System.exit(1);
    }
    //! ------------------ Fim error messages --------------

    //* ------------------ Testes Unitários ------------------
    public static boolean checkIgualdadeMatrizes(double[][] obtido, double[][] esperado) {
        for (int i = 0; i < esperado.length; i++) {
            for (int j = 0; j < esperado[0].length; j++) {
                if (Math.abs(esperado[i][j] - obtido[i][j]) > 1e-3) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean checkIgualdadeVetores(double[] vetor1, double[] vetor2) {
        if (vetor1.length != vetor2.length) {
            return false;
        }

        for (int i = 0; i < vetor1.length; i++) {
            if (Math.abs(vetor1[i] - vetor2[i]) > 1e-3) {
                return false;
            }
        }
        return true;
    }

    public static void checkAverageVector() {
        System.out.println("Teste : Vetor Médio");
        double[] expectedResult = {2.0, 5.0, 8.0};

        double[][] inputMatrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        double[] obtainedResult = calculateMeanVector(inputMatrix);

        if (checkIgualdadeVetores(obtainedResult, expectedResult)) {
            System.out.println("calcularVetorMedio: Teste bem sucedido!");
        } else {
            System.out.println("calcularVetorMedio: Falha - Resultado incorreto.");
            printVectorTests(expectedResult, "Esperado");
            printVectorTests(obtainedResult, "Obtido");
        }
        System.out.println();
    }

    public static void checkCentralizeImage() {
        System.out.println("Teste : Centralização de Imagens");
        double[] meanVector = {2.0, 5.0, 8.0};

        double[][] inputMatrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        double[][] expectedResult = {
                {-1, 0, 1},
                {-1, 0, 1},
                {-1, 0, 1}
        };

        double[][] obtainedResult = centralizeImages(inputMatrix, meanVector);

        if (checkIgualdadeMatrizes(obtainedResult, expectedResult)) {
            System.out.println("centralizarMatriz: Teste bem sucedido!");
        } else {
            System.out.println("centralizarMatriz: Falha - Resultado incorreto.");
            printMatrix(expectedResult, "Esperado", false);
            printMatrix(obtainedResult, "Obtido", false);
        }
        System.out.println();
    }

    public static void checkMultiplication() {
        double[][] leftMatrix = {
                {1, 2},
                {3, 4}
        };
        double[][] rightMatrix = {
                {5, 6},
                {7, 8}
        };
        double[][] expectedResult = {
                {19, 22},
                {43, 50}
        };
        double[][] obtainedResult = multiplyMatrices(leftMatrix, rightMatrix);
        checkIgualdadeMatrizes(obtainedResult, expectedResult);

        if (checkIgualdadeMatrizes(obtainedResult, expectedResult)) {
            System.out.println("Multiplicação: Teste bem sucedido!");
        } else {
            System.out.println("Multiplicação: Falha - Resultado incorreto.");
            printMatrix(expectedResult, "Esperado", false);
            printMatrix(obtainedResult, "Obtido", false);
        }
        System.out.println();
    }

    public static void checkNormalization() {
        System.out.println("Teste 2: Normalização de matrizes");
        double[][] inputMatrix = {
                {3, 4},
                {2, 6},
                {4, 2}
        };


        double[][] expectedResult = {
                {0.557, 0.535},
                {0.371, 0.802},
                {0.743, 0.267}
        };


        double[][] obtainedResult = normalize(inputMatrix);
        checkIgualdadeMatrizes(obtainedResult, expectedResult);

        if (checkIgualdadeMatrizes(obtainedResult, expectedResult)) {
            System.out.println("Normalização: Teste bem sucedido!");
        } else {
            System.out.println("Normalização: Falha - Resultado incorreto.");
            printMatrix(expectedResult, "Esperado", false);
            printMatrix(obtainedResult, "Obtido", false);
        }
        System.out.println();
    }

    public static void checkTranspose() {
        System.out.println("Teste : Transposta");

        double[][] inputMatrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        double[][] expectedResult = {
                {1, 4, 7},
                {2, 5, 8},
                {3, 6, 9}
        };

        double[][] obtainedResult = transposeMatrix(inputMatrix);
        checkIgualdadeMatrizes(obtainedResult, expectedResult);

        if (checkIgualdadeMatrizes(obtainedResult, expectedResult)) {
            System.out.println("Transposta: Teste bem sucedido!");
        } else {
            System.out.println("Transposta: Falha - Resultado incorreto.");
            printMatrix(expectedResult, "Esperado", false);
            printMatrix(obtainedResult, "Obtido", false);
        }
        System.out.println();
    }

    public static void checkMultiplicationEscalar() {
        System.out.println("Teste : Multiplicação por escalar");

        double[][] inputMatrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        int x = 2;

        double[][] expectedResult = {
                {2, 4, 6},
                {8, 10, 12},
                {14, 16, 18}
        };

        double[][] obtainedResult = multiplyMatrixEscalar(inputMatrix, x);
        checkIgualdadeMatrizes(obtainedResult, expectedResult);

        if (checkIgualdadeMatrizes(obtainedResult, expectedResult)) {
            System.out.println("Multiplicação por escalar: Teste bem sucedido!");
        } else {
            System.out.println("Multiplicação por escalar: Falha - Resultado incorreto.");
            printMatrix(expectedResult, "Esperado", false);
            printMatrix(obtainedResult, "Obtido", false);
        }
        System.out.println();
    }

    public static void checkSubtractionColumns() {
        System.out.println("Teste : Subtração de colunas");

        double[] leftColumn = {1, 2, 3};
        double[] rightColumn = {4, 5, 6};

        double[] expectedResult = {-3, -3, -3};

        double[] obtainedResult = subtractionColumns(leftColumn, rightColumn);
        checkIgualdadeVetores(obtainedResult, expectedResult);

        if (checkIgualdadeVetores(obtainedResult, expectedResult)) {
            System.out.println("Subtração de colunas: Teste bem sucedido!");
        } else {
            System.out.println("Subtração de colunas: Falha - Resultado incorreto.");
            printVectorTests(expectedResult, "Esperado");
            printVectorTests(obtainedResult, "Obtido");
        }
        System.out.println();
    }

    public static void checkSubMatrix() {
        System.out.println("Teste : SubMatriz");

        double[][] inputMatrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        double[][] valuesAndIndexArray = {
                {5, 1},  // Segunda coluna
                {9, 2}   // Terceira coluna
        };

        double[][] expectedResult = {
                {2, 3},
                {5, 6},
                {8, 9}

        };


        double[][] obtainedResult = createSubMatrix(inputMatrix, valuesAndIndexArray);
        checkIgualdadeMatrizes(obtainedResult, expectedResult);

        if (checkIgualdadeMatrizes(obtainedResult, expectedResult)) {
            System.out.println("SubMatriz: Teste bem sucedido!");
        } else {
            System.out.println("SubMatriz: Falha - Resultado incorreto.");
            printMatrix(expectedResult, "Esperado", false);
            printMatrix(obtainedResult, "Obtido", false);
        }
        System.out.println();
    }

    public static void checkEuclideanDistance() {
        System.out.println("Teste : Distância Euclidiana");

        double[] principalVector = {1, 2, 3};
        double[][] weightMatrix = {
                {4, 5},
                {6, 7},
                {8, 9}
        };

        double[] expectedResult = {7.071, 8.775};

        double[] obtainedResult = calculateEuclideanDistance(principalVector, weightMatrix);
        checkIgualdadeVetores(obtainedResult, expectedResult);

        if (checkIgualdadeVetores(obtainedResult, expectedResult)) {
            System.out.println("Distância Euclidiana: Teste bem sucedido!");
        } else {
            System.out.println("Distância Euclidiana: Falha - Resultado incorreto.");
            printVectorTests(expectedResult, "Esperado");
            printVectorTests(obtainedResult, "Obtido");
        }
        System.out.println();
    }

    public static void checkCloserVetorTest() {
        System.out.println("Teste: Verificação do vetor mais próximo");

        double[] distances = {5.3, 3.2, 7.8, 1.4, 6.9, 1.4};

        int expectedResult1 = 3;
        int expectedResult2 = 5;

        int[] obtainedResult = checkCloserVetor(distances);
        boolean testPassed1 = false;
        boolean testPassed2 = false;

        for (int i = 0; obtainedResult[i] != Integer.MAX_VALUE; i++) {
            if (obtainedResult[i] == expectedResult1) {
                testPassed1 = true;
                System.out.println("Verificação do vetor mais próximo: Teste bem sucedido para o índice " + expectedResult1 + "!");
            } else if (obtainedResult[i] == expectedResult2) {
                testPassed2 = true;
                System.out.println("Verificação do vetor mais próximo: Teste bem sucedido para o índice " + expectedResult2 + "!");
            } else {
                System.out.println("Verificação do vetor mais próximo: Falha - Resultado incorreto.");
                System.out.println("Esperado: " + expectedResult1 + " ou " + expectedResult2);
                System.out.println("Obtido: " + obtainedResult[i]);
            }
            System.out.println();
        }

        if (!testPassed1) {
            System.out.println("Verificação do vetor mais próximo: Falha - Índice " + expectedResult1 + " não encontrado.");
        }
        if (!testPassed2) {
            System.out.println("Verificação do vetor mais próximo: Falha - Índice " + expectedResult2 + " não encontrado.");
        }
    }

    public static void checkMAETest() {
        System.out.println("Teste: Cálculo do MAE");

        // Matrizes de exemplo
        double[][] originalMatrix = {
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        };

        double[][] matrizEigenFaces = {
                {1.1, 2.1, 3.1},
                {4.1, 5.1, 6.1},
                {7.1, 8.1, 9.1}
        };

        double expectedResult = 0.1;

        double obtainedResult = calculateMAE(originalMatrix, matrizEigenFaces);

        if (Math.abs(obtainedResult - expectedResult) < 1e-3) {
            System.out.println("Cálculo do MAE: Teste bem sucedido!");
        } else {
            System.out.println("Cálculo do MAE: Falha - Resultado incorreto.");
            System.out.println("Esperado: " + expectedResult);
            System.out.printf("Obtido: %.2f\n", obtainedResult);
        }
        System.out.println();
    }

    public static void checkCalculateWeights() {
        System.out.println("Teste: Cálculo dos pesos");


        double[] phi = {1.0, 2.0, 3.0};

        double[][] eigenfaces = {
                {1.0, 2.0, 3.0},
                {4.0, 5.0, 6.0},
                {7.0, 8.0, 9.0}
        };

        double[] expectedResult = {30.0, 36.0, 42.0};

        double[] obtainedResult = calculateWeights(phi, eigenfaces);

        if (checkIgualdadeVetores(obtainedResult, expectedResult)) {
            System.out.println("Cálculo dos pesos: Teste bem sucedido!");
        } else {
            System.out.println("Cálculo dos pesos: Falha - Resultado incorreto.");
            printVectorTests(expectedResult, "Esperado");
            printVectorTests(obtainedResult, "Obtido");
        }
        System.out.println();
    }

    public static void checkGetValuesAndIndexArray() {
        System.out.println("Teste: getValuesAndIndexArray");

        double[][] eigenValuesArray = {
                {1.0, 0, 0},
                {0, 5.0, 0},
                {0, 0, 3.0}
        };

        int eigenfaces = 2;

        double[][] expectedResult = {
                {5.0, 1},
                {3.0, 2}
        };

        double[][] obtainedResult = getValuesAndIndexArray(eigenValuesArray, eigenfaces);

        if (checkIgualdadeMatrizes(obtainedResult, expectedResult)) {
            System.out.println("getValuesAndIndexArray: Teste bem sucedido!");
        } else {
            System.out.println("getValuesAndIndexArray: Falha - Resultado incorreto.");
            printMatrix(expectedResult, "Esperado", false);
            printMatrix(obtainedResult, "Obtido", false);
        }
        System.out.println();
    }

    public static void checkReconstructImage() {
        System.out.println("Teste: reconstructImage");

        double[] averageVector = {1.0, 2.0, 3.0};

        double[][] eigenfaces = {
                {0.1, 0.2},
                {0.3, 0.4},
                {0.5, 0.6}
        };

        double[] columnWeights = {0.5, 0.3};

        int quantityEigenfaces = 2;

        double[] expectedResult = {1.11, 2.27, 3.43};

        double[] obtainedResult = reconstructImage(averageVector, eigenfaces, columnWeights, quantityEigenfaces);

        if (checkIgualdadeVetores(obtainedResult, expectedResult)) {
            System.out.println("reconstructImage: Teste bem sucedido!");
        } else {
            System.out.println("reconstructImage: Falha - Resultado incorreto.");
            printVectorTests(expectedResult, "Esperado");
            printVectorTests(obtainedResult, "Obtido");
        }
        System.out.println();
    }

    public static void checkConstructDiagonalMatrix() {
        System.out.println("Teste: Construção de Matriz Diagonal");

        double[][] inputMatrix = {
                {5, 0},
                {3, 0},
                {8, 0}
        };

        double[][] expectedResult = {
                {5, 0, 0},
                {0, 3, 0},
                {0, 0, 8}
        };


        double[][] obtainedResult = constructDiagonalMatrix(inputMatrix);


        if (checkIgualdadeMatrizes(obtainedResult, expectedResult)) {
            System.out.println("Matriz Diagonal: Teste bem sucedido!");
        } else {
            System.out.println("Matriz Diagonal: Falha - Resultado incorreto.");
            printMatrix(expectedResult, "Esperado", false);
            printMatrix(obtainedResult, "Obtido", false);
        }
        System.out.println();
    }

    public static void checkMatrixToArray1D() {
        System.out.println("Teste: Conversão de Matriz para Array 1D");

        double[][] inputMatrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        double[] expectedResult = {1, 2, 3, 4, 5, 6, 7, 8, 9};

        double[] obtainedResult = matrixToArray1D(inputMatrix);

        if (checkIgualdadeVetores(obtainedResult, expectedResult)) {
            System.out.println("Conversão de Matriz para Array 1D: Teste bem sucedido!");
        } else {
            System.out.println("Conversão de Matriz para Array 1D: Falha - Resultado incorreto.");
            printVectorTests(expectedResult, "Esperado");
            printVectorTests(obtainedResult, "Obtido");
        }
        System.out.println();
    }

    public static void checkGetColumn() {
        System.out.println("Teste: Obter Coluna de Matriz");

        double[][] inputMatrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        int index = 1;

        double[] expectedResult = {2, 5, 8};

        double[] obtainedResult = getColumn(inputMatrix, index);

        if (checkIgualdadeVetores(obtainedResult, expectedResult)) {
            System.out.println("Obter Coluna de Matriz: Teste bem sucedido!");
        } else {
            System.out.println("Obter Coluna de Matriz: Falha - Resultado incorreto.");
            printVectorTests(expectedResult, "Esperado");
            printVectorTests(obtainedResult, "Obtido");
        }
        System.out.println();

    }
    //* ------------------ Fim verificações -------------------


    //* ----------------- Correr Testes -----------------------
    public static void runTests() {
        checkAverageVector();
        checkCentralizeImage();
        checkMultiplication();
        checkMultiplicationEscalar();
        checkNormalization();
        checkTranspose();
        checkSubtractionColumns();
        checkSubMatrix();
        checkEuclideanDistance();
        checkCloserVetorTest();
        checkMAETest();
        checkCalculateWeights();
        checkGetValuesAndIndexArray();
        checkReconstructImage();
        checkConstructDiagonalMatrix();
        checkMatrixToArray1D();
        checkGetColumn();
    }
    //* ----------------- Fim Correr Testes -------------------

}