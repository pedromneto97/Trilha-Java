package aima.core.environment.Trilha;

import aima.core.util.datastructure.XYLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TrilhaState implements Cloneable {
    public static final String BRANCO = "BRANCO";
    public static final String PRETO = "PRETO";
    public static final String EMPTY = "XXXXX";

    private String[] board = new String[]{EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
            EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, "0", "0", "0"};

    private String playerToMove = BRANCO;

    private double utility = -1; // 1: win for X, 0: win for O, 0.5: draw

    public int adjacentes[][] = new int[24][4];

    public TrilhaState() {
        // Cria os adjacentes
        for (int i = 0; i < 24; i++) {
            //Caso x seja par
            if ((i % 2) == 0) {
                //Verifica se é 0, 8 ou 16
                if ((i % 8) == 0) {
                    this.adjacentes[i][0] = i + 1;
                    this.adjacentes[i][1] = i + 7;
                    this.adjacentes[i][2] = -1;
                    this.adjacentes[i][3] = -1;
                } else {
                    this.adjacentes[i][0] = i - 1;
                    this.adjacentes[i][1] = i + 1;
                    this.adjacentes[i][2] = -1;
                    this.adjacentes[i][3] = -1;
                }
            } else {
                //Caso ímpar
                //1 até 8
                if (i < 8) {
                    if ((i % 8) == 7) {
                        this.adjacentes[i][0] = i - 7;
                        this.adjacentes[i][1] = i - 1;
                        this.adjacentes[i][2] = i + 8;
                        this.adjacentes[i][3] = -1;
                    } else {
                        this.adjacentes[i][0] = i - 1;
                        this.adjacentes[i][1] = i + 1;
                        this.adjacentes[i][2] = i + 8;
                        this.adjacentes[i][3] = -1;
                    }
                }
                //De 9 até 16
                else {
                    if (i < 16) {
                        if ((i % 8) == 7) {
                            this.adjacentes[i][0] = i - 8;
                            this.adjacentes[i][1] = i - 7;
                            this.adjacentes[i][2] = i - 1;
                            this.adjacentes[i][3] = i + 8;
                        } else {
                            this.adjacentes[i][0] = i - 8;
                            this.adjacentes[i][1] = i - 1;
                            this.adjacentes[i][2] = i + 1;
                            this.adjacentes[i][3] = i + 8;
                        }
                    }
                    //De 17 até 24
                    else {
                        if ((i % 8) == 7) {
                            this.adjacentes[i][0] = i - 8;
                            this.adjacentes[i][1] = i - 7;
                            this.adjacentes[i][2] = i - 1;
                            this.adjacentes[i][3] = -1;
                        } else {
                            this.adjacentes[i][0] = i - 8;
                            this.adjacentes[i][1] = i - 1;
                            this.adjacentes[i][2] = i + 1;
                            this.adjacentes[i][3] = -1;
                        }
                    }
                }
            }
        }
    }

    //Retorna o próximo jogador
    public String getPlayerToMove() {
        return playerToMove;
    }

    //Retorna se uma posição está vazia
    public boolean isEmpty(int pos) {
        return board[pos] == EMPTY;
    }

    //Retorna o valor de uma posição, origem deve ser -1
    public String getValue(int origem, int destino) {
        if (origem == -1)
            return board[destino];
        if (board[origem] == this.getPlayerToMove())
            return board[destino];
        return "0";
    }

    //Retorna a utility
    public double getUtility() {
        return utility;
    }

    //Executa uma ação a partir de um XYLocation
    public void mark(XYLocation action) {
        if (this.getMoves().contains(action))
            mark(action.getXCoOrdinate(), action.getYCoOrdinate());
    }

    //Executa uma ação a partir de dois pontos
    public void mark(int origem, int destino) {
        if (!this.getMoves().contains(new XYLocation(origem, destino)))
            return;
        if (utility == -1 && board[26] == "1") {
            if (board[destino] != playerToMove) {
                board[destino] = EMPTY;
                board[26] = "0";
                analyzeUtility();
                playerToMove = (playerToMove == BRANCO ? PRETO : BRANCO);
            }
        } else {
            if ((utility == -1) && getValue(origem, destino).equals(EMPTY)) {
                if (origem != -1)
                    board[origem] = EMPTY;
                board[destino] = playerToMove;
                if (playerToMove == BRANCO) {
                    if (board[26] == "0") {
                        if (Integer.parseInt(board[24]) < 9)
                            board[24] = Integer.toString(Integer.parseInt(board[24]) + 1);
                        if (this.verificaTrinca(destino))
                            board[26] = "1";
                        else
                            playerToMove = PRETO;
                    }
                } else {
                    if (board[26] == "0") {
                        if (Integer.parseInt(board[25]) < 9)
                            board[25] = Integer.toString(Integer.parseInt(board[25]) + 1);
                        if (this.verificaTrinca(destino))
                            board[26] = "1";
                        else
                            playerToMove = BRANCO;
                    }
                }
            }
        }
    }

    //Verifica se o movimento é final
    private void analyzeUtility() {
        if (this.contaPecas()) {
            utility = (playerToMove == BRANCO ? 1 : 0);
        } else
            utility = -1;
    }

    //Verifica se o movimento aplicado gera uma trinca
    public boolean verificaTrinca(int move) {
        // Caso move seja par
        if ((move % 2) == 0) {
            // Verifica se é 0,8,16
            if ((move % 8) == 0) {
                return (board[move + 1] == playerToMove && board[move + 2] == playerToMove) || (
                        board[move + 7] == playerToMove && board[move + 6] == playerToMove);
            } else {
                if ((move % 8) == 6) {
                    return (board[move + 1] == playerToMove && board[move - 6] == playerToMove) ||
                            (board[move - 1] == playerToMove && board[move - 2] == playerToMove);
                } else {
                    return (board[move - 1] == playerToMove && board[move - 2] == playerToMove) ||
                            (board[move + 1] == playerToMove && board[move + 2] == playerToMove);
                }
            }
        }
        // Caso ímpar
        else {
            if ((move % 8) == 7) {
                if (board[move - 7] == playerToMove && board[move - 1] == playerToMove)
                    return true;
            } else {
                if (board[move + 1] == playerToMove && board[move - 1] == playerToMove)
                    return true;
            }
            if (move < 8) {
                return (board[move + 8] == playerToMove && board[move + 16] == playerToMove);
            }
            // De 9 até 16
            else {
                if (move < 16)
                    return (board[move - 8] == playerToMove && board[move + 8] == playerToMove);
                    // De 17 até 24
                else
                    return (board[move - 8] == playerToMove && board[move - 16] == playerToMove);

            }
        }
    }

    //Retorna se o número de peças de algum jogador chegou a ser menor que 3
    public boolean contaPecas() {
        if (Integer.parseInt(board[24]) < 9 && Integer.parseInt(board[25]) < 9) {
            return false;
        }
        int contb = 0;
        int contp = 0;
        for (int i = 0; i < 24; i++) {
            if (board[i] == BRANCO)
                contb++;
            else {
                if (board[i] == PRETO)
                    contp++;
            }
        }
        if (contb < 3 || contp < 3)
            return true;
        return false;
    }

    //Retorna uma lista com todos os movimentos possível
    public List<XYLocation> getMoves() {
        List<XYLocation> result = new ArrayList<XYLocation>();
        //Verifica se não é para remover uma peça
        if (board[26] == "1") {
            for (int i = 0; i < 24; i++) {
                if (board[i] != EMPTY && board[i] != this.playerToMove)
                    result.add(new XYLocation(-1, i));
            }
        } else {
            int aux = 1;
            if (this.playerToMove == BRANCO)
                aux = 0;
            if (Integer.parseInt(board[24 + aux]) < 9) {
                for (int i = 0; i < 24; i++) {
                    if (isEmpty(i))
                        result.add(new XYLocation(-1, i));
                }
            } else {
                for (int i = 0; i < 24; i++) {
                    if (board[i] == playerToMove) {
                        int cont = 0;
                        for (int j = 0; j < 24; j++) {
                            if (board[j] == playerToMove)
                                cont++;
                        }
                        if (cont > 3) {
                            for (int e : this.adjacentes[i]) {
                                if (e != -1 && getValue(-1, e) == EMPTY)
                                    result.add(new XYLocation(i, e));
                            }
                        } else {
                            for (int j = 0; j < 24; j++) {
                                if (board[j] == EMPTY)
                                    result.add(new XYLocation(i, j));
                            }
                        }
                    }
                }
            }
        }
        Collections.shuffle(result);
        return result;
    }


    @Override
    public TrilhaState clone() {
        TrilhaState copy = null;
        try {
            copy = (TrilhaState) super.clone();
            copy.board = Arrays.copyOf(board, board.length);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace(); // should never happen...
        }
        return copy;
    }

    @Override
    public boolean equals(Object anObj) {
        if (anObj != null && anObj.getClass() == getClass()) {
            TrilhaState anotherState = (TrilhaState) anObj;
            for (int i = 0; i < 27; i++) {
                if (board[i] != anotherState.board[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        // Need to ensure equal objects have equivalent hashcodes (Issue 77).
        return toString().hashCode();
    }

    @Override
    public String toString() {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(board[0] + "----------------" + board[1] + "----------------" + board[2] + "\n");
        strBuilder.append("|     " + board[8] + "---------" + board[9] + "---------" + board[10] + "     |\n");
        strBuilder.append("|       |     " + board[16] + "-----" + board[17] + "-----" + board[18] + "     |     |\n");
        strBuilder.append(board[7] + "--" + board[15] + "--" + board[23] + "               " + board[19] + "--" + board[11] + "--" + board[3] + "\n");
        strBuilder.append("|       |     " + board[22] + "-----" + board[21] + "-----" + board[20] + "     |     |\n");
        strBuilder.append("|       " + board[14] + "---------" + board[13] + "---------" + board[12] + "     |     |\n");
        strBuilder.append(board[6] + "----------------" + board[5] + "----------------" + board[4] + "\n");
        strBuilder.append("\n");
        return strBuilder.toString();
    }
}
