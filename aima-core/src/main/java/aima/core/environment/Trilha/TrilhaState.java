package aima.core.environment.Trilha;

import aima.core.util.datastructure.XYLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TrilhaState implements Cloneable {
    public static final String BRANCO = "BRANCO";
    public static final String PRETO = "PRETO";
    public static final String EMPTY = "-";

    private String[] board = new String[]{EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
            EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, "0", "0", "0"};

    private String playerToMove = BRANCO;

    private double utility = -1; // 1: win for X, 0: win for O, 0.5: draw

    private int adjacentes[][] = new int[24][4];

    public TrilhaState() {
        for (int i = 1; i <= 24; i++) {
            //Caso x seja ímpar
            if ((i % 2) == 1) {
                //Verifica se é 1, 9 ou 17
                if ((i % 8) == 1) {
                    this.adjacentes[i][0] = i + 1;
                    this.adjacentes[i][1] = i + 7;
                    this.adjacentes[i][2] = 0;
                    this.adjacentes[i][3] = 0;
                } else {
                    this.adjacentes[i][0] = i - 1;
                    this.adjacentes[i][1] = i + 1;
                    this.adjacentes[i][2] = 0;
                    this.adjacentes[i][3] = 0;
                }
            } else {
                //Caso par
                //1 até 8
                if (i <= 8) {
                    if ((i % 8) == 0) {
                        this.adjacentes[i][0] = i - 7;
                        this.adjacentes[i][1] = i - 1;
                        this.adjacentes[i][2] = i + 8;
                        this.adjacentes[i][3] = 0;
                    } else {
                        this.adjacentes[i][0] = i - 1;
                        this.adjacentes[i][1] = i + 1;
                        this.adjacentes[i][2] = i + 8;
                        this.adjacentes[i][3] = 0;
                    }
                }
                //De 9 até 16
                else {
                    if (i <= 16) {
                        if ((i % 8) == 0) {
                            this.adjacentes[i][0] = i - 8;
                            this.adjacentes[i][1] = i - 7;
                            this.adjacentes[i][2] = i + 1;
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
                        if ((i % 8) == 0) {
                            this.adjacentes[i][0] = i - 8;
                            this.adjacentes[i][1] = i - 7;
                            this.adjacentes[i][2] = i - 1;
                            this.adjacentes[i][3] = 0;
                        } else {
                            this.adjacentes[i][0] = i - 8;
                            this.adjacentes[i][1] = i - 1;
                            this.adjacentes[i][2] = i + 1;
                            this.adjacentes[i][3] = 0;
                        }
                    }
                }
            }
        }
    }

    public String getPlayerToMove() {
        return playerToMove;
    }

    public boolean isEmpty(int pos) {
        return board[pos] == EMPTY;
    }

    public String getValue(int origem, int destino) {
        if (origem == 0)
            return board[destino];
        if (board[origem] == this.getPlayerToMove())
            return board[destino];
        return "0";
    }

    public double getUtility() {
        return utility;
    }

    public void mark(XYLocation action) {
        if (this.getMoves().contains(action))
            mark(action.getXCoOrdinate(), action.getYCoOrdinate());
    }

    public void mark(int origem, int destino) {
        if (utility == -1 && board[26] == "1") {
            if (board[destino] != this.getPlayerToMove()) {
                board[destino] = EMPTY;
                board[26] = "0";
                analyzeUtility();
            }
        } else {
            if (utility == -1 && getValue(origem, destino) == EMPTY) {
                if (origem != 0)
                    board[origem] = EMPTY;
                if (playerToMove == BRANCO) {
                    if (this.verificaTrinca(destino) && board[26] == "0") {
                        board[26] = "1";
                    } else
                        playerToMove = PRETO;
                } else {
                    if (this.verificaTrinca(destino) && board[26] == "0") {
                        board[26] = "1";
                    } else
                        playerToMove = BRANCO;
                }
                board[destino] = playerToMove;
            }
        }
    }

    private void analyzeUtility() {
        if (this.contaPecas()) {
            utility = (playerToMove == BRANCO ? 1 : 0);
        } else
            utility = -1;
    }

    private boolean verificaTrinca(int move) {
        // Caso move seja ímpar
        if ((move % 2) == 1) {
            // Verifica se é 1,9 ou 17
            if ((move % 8) == 1) {
                return (board[move + 1] == playerToMove && board[move + 2] == playerToMove) || (
                        board[move + 7] == playerToMove && board[move + 6] == playerToMove);
            } else {
                if ((move % 8) == 7) {
                    return (board[move + 1] == playerToMove && board[move - 6] == playerToMove) ||
                            (board[move - 1] == playerToMove && board[move - 2] == playerToMove);
                } else {
                    return (board[move - 1] == playerToMove && board[move - 2] == playerToMove) ||
                            (board[move + 1] == playerToMove && board[move + 2] == playerToMove);
                }
            }
        }
        // Caso par
        else {
            if ((move % 8) == 0) {
                if (board[move - 7] == playerToMove && board[move - 1] == playerToMove)
                    return true;
            } else {
                if (board[move + 1] == playerToMove && board[move - 1] == playerToMove)
                    return true;
            }
            if (move <= 8) {
                return (board[move + 8] == playerToMove && board[move + 16] == playerToMove);
            }
            // De 9 até 16
            else {
                if (move <= 16)
                    return (board[move - 8] == playerToMove && board[move + 8] == playerToMove);
                    // De 17 até 24
                else
                    return (board[move - 8] == playerToMove && board[move - 16] == playerToMove);

            }
        }
    }

    private boolean contaPecas() {
        if (Integer.parseInt(board[24]) != 9 && Integer.parseInt(board[25]) != 9) {
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

    public List<XYLocation> getMoves() {
        List<XYLocation> result = new ArrayList<XYLocation>();
        //Verifica se não é para remover uma peça
        if (board[26] == "1") {
            for (int i = 0; i < 24; i++) {
                if (board[i] != EMPTY && board[i] != this.playerToMove)
                    result.add(new XYLocation(0, i));

            }
        } else {
            if (this.playerToMove == BRANCO) {
                if (Integer.parseInt(board[24]) < 9) {
                    for (int i = 0; i < 24; i++) {
                        if (isEmpty(i))
                            result.add(new XYLocation(0, i));
                    }
                } else {
                    for (int i = 0; i < 24; i++) {
                        if (board[i] == BRANCO) {
                            for (int e : this.adjacentes[i + 1]) {
                                if (board[e] == EMPTY)
                                    result.add(new XYLocation(i, e));
                            }
                        }
                    }
                }
            } else {
                if (this.playerToMove == PRETO) {
                    if (Integer.parseInt(board[25]) < 9) {
                        for (int i = 0; i < 24; i++) {
                            if (isEmpty(i))
                                result.add(new XYLocation(0, i));
                        }
                    } else {
                        for (int i = 0; i < 24; i++) {
                            if (board[i] == PRETO) {
                                for (int e : this.adjacentes[i + 1]) {
                                    if (board[e] == EMPTY)
                                        result.add(new XYLocation(i, e));
                                }
                            }
                        }
                    }
                }
            }
        }
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
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                strBuilder.append(getValue(col, row) + " ");
            }
            strBuilder.append("\n");
        }
        return strBuilder.toString();
    }
}
