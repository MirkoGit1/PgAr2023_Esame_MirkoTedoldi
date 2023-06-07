package it.mirko.fp.esame.Game;

import it.ayman.fp.lib.RandomDraws;

public class Balance {
    int [][] balance;

    public Balance(int n, int hp) {
        this.balance = createBalance(n, hp);
    }

    /**
     * Metodo per generare il balance
     * @param n numero di elementi per il game
     * @param hp vita massima dei golem
     * @return la matrice di int caratterizzante il balance
     */
    public int[][] createBalance(int n, int hp) {
        int [][] matrix;
        //Generazione di n - 1 coppie di indici da inizializzare random
        int [][] indexMatrix = new int[n][2];

        do {
            matrix = new int[n][n];
            do {
                //Genera n - 1 coppie di indici e verifica che siano posizioni valide
                for (int i = 0; i < n; i++) {
                    int row = RandomDraws.drawInteger(0, n - 1);
                    int column = RandomDraws.drawInteger(0, n - 1);
                    indexMatrix[i] = new int[]{row, column};
                }
            } while (!validRandomIndexes(indexMatrix));

            do {
                for (int[] index : indexMatrix) {
                    int randomNumber;
                    do {
                        randomNumber = RandomDraws.drawInteger(-hp, hp);
                    } while (randomNumber == 0);

                    matrix[index[0]][index[1]] = randomNumber;
                    matrix[index[1]][index[0]] = -randomNumber;
                }
            } while (!validRandomValues(matrix, hp));
        } while(!solveMatrix(matrix, hp));



        return matrix;
    }

    /**
     * Bisogna verificare che gli indici row e column non siano uguali tra di loro perché altrimenti
     * sarebbero valori della diagonale, poi verificare che le coppie di indici siano univoche all'interno
     * della matrice, per ultimo bisogna verificare che una riga non sia già del tutto determinata in seguito
     * alla generazione dei numeri casuali.
     * @param indexMatrix la matrice degli indici dove si esegue il controllo
     * @return se gli indici sono validi ritorna true
     */
    private boolean validRandomIndexes(int[][] indexMatrix) {

        for (int[] indexes : indexMatrix) {
            if (indexes[0] == indexes[1])
                return false;
        }

        for (int i = 0; i < indexMatrix.length - 1; i++) {
            for (int j = i + 1; j < indexMatrix.length; j++) {
                //Controllo che le due posizioni non siano uguali
                if (indexMatrix[i][0] == indexMatrix[j][0] && indexMatrix[i][1] == indexMatrix[j][1])
                    return false;
                //Controllo che le due posizioni non siano simmetriche
                if (indexMatrix[i][0] == indexMatrix[j][1] && indexMatrix[i][1] == indexMatrix[j][0])
                    return false;
            }
        }

        //Array totale degli indici
        int [] indexArray = new int[indexMatrix.length * 2];

        for (int i = 0; i < indexMatrix.length * 2; i++) {
            indexArray[i] = indexMatrix[i / 2][i % 2];
        }

        int [] indexCounter = new int[indexMatrix.length + 1];

        //i rappresenta il numero di cui controllare la frequenza
        for (int i = 0; i < indexMatrix.length + 1; i++) {
            //j rappresenta il j-esimo indice nell'indexArray
            for (int j : indexArray) {
                if (i == j)
                    indexCounter[i]++;
            }
        }

        for (int i : indexCounter) {
            if (i >= indexMatrix.length)
                return false;
        }

        return true;
    }

    /**
     * Metodo che controlla che su ogni riga, e di conseguenza colonna, la somma dei valori
     * già inizializzati non superi gli hp di un TamaGolem.
     * @param matrix la matrice da controllare
     * @param hp la vita massima del TamaGolem
     * @return ritorna true se la matrice è valida
     */
    private boolean validRandomValues(int [][] matrix, int hp) {
        int n = matrix.length;
        for (int [] row : matrix) {
            boolean flag = false;
            int sum = 0;
            for (int value : row) {
                sum += value;
                if (value != 0)
                    flag = true;
            }

            if (sum < -hp * (n - 1) / 2 || sum > hp * (n - 1) / 2 || (sum == 0 && flag))
                return false;
        }
        return true;
    }

    /**
     * Metodo per risolvere la matrice, ovvero per aggiungere i numeri mancanti dopo la generazione dei valori iniziali.
     * Il metodo risolve la matrice inserendo numeri che rispettano i parametri i generazione imposti dalla consegna.
     * @param matrix la matrice da risolvere
     * @param hp gli hp max dei golem
     * @return true se la matrice è stata risolta, false se si tratta di una matrice irrisolvibile per i numeri generati
     * inizialmente
     */
    private boolean solveMatrix(int [][] matrix, int hp) {
        int n = matrix.length;
        for (int row = 0; row < n; row++) {
            for (int column = 0; column < n; column++) {
                if (row != column && matrix[row][column] == 0) {
                    for (int numberToTry = -hp; numberToTry <= hp; numberToTry++) {
                        numberToTry = RandomDraws.drawInteger(-hp, hp);
                        if (numberToTry != 0) {
                            matrix[row][column] = numberToTry;
                            matrix[column][row] = -numberToTry;

                            //Se la riga è piena
                            if (isFullRow(matrix, row)) {
                                //Se tutta la matrice è già valida ritorna direttamente true
                                if (validMatrix(matrix)) {
                                    return true;
                                }
                                //Se la riga è valida
                                if (isValidRow(matrix, row)) {
                                    //
                                    if (solveMatrix(matrix, hp)) {
                                        return true;
                                    }
                                    else {
                                        matrix[row][column] = 0;
                                        matrix[column][row] = 0;
                                        return false;
                                    }
                                }
                                else {
                                    matrix[row][column] = 0;
                                    matrix[column][row] = 0;
                                }
                            }
                            //Altrimenti passa al numero successivo nella riga
                            else {
                                //Richiama nuovamente il metodo con la matrice già leggermente completa
                                if (solveMatrix(matrix, hp))
                                    return true;
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return validMatrix(matrix);
    }

    /**
     * Metodo per controllare che tutta la matrice sia valida in base alle condizioni date
     * @param matrix la matrice su cui si vuole eseguire il controllo
     * @return se la matrice è valida ritorna true, in caso contrario ritorna false
     */
    private boolean validMatrix(int [][] matrix) {
        int rowAdder = 0;
        int zeroCounter = 0;
        for (int [] row : matrix) {
            for (int number : row) {
                rowAdder += number;
                if (number == 0)
                    zeroCounter++;
            }
            if (rowAdder != 0 || zeroCounter > 1)
                return false;

            zeroCounter = 0;
        }
        return true;
    }

    /**
     * Metodo per controllare che una riga sia piena. Una riga è piena se contiene più di uno zero al suo interno.
     * @param matrix matrice da controllare
     * @param row riga della matrice da controllare
     * @return true se è piena, false in caso contrario
     */
    private boolean isFullRow(int [][] matrix, int row) {
        int zeroCounter = 0;
        for (int element : matrix[row]) {
            if (element == 0)
                zeroCounter++;
        }

        return zeroCounter < 2;
    }

    /**
     * Metodo per controllare se una riga è valida per i criteri imposti
     * @param matrix la matrice da controllare
     * @param row la riga della matrice da controllare
     * @return true se la riga è valida, false in caso contrario
     */
    private boolean isValidRow(int [][] matrix, int row) {
        int sum = 0;
        for (int number : matrix[row])
            sum += number;

        return sum == 0;
    }

    public int[][] getBalance() {
        return balance;
    }
}
