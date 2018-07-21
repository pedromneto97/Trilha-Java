package aima.core.environment.Trilha;

import aima.core.environment.tictactoe.TicTacToeState;
import aima.core.search.adversarial.Game;
import aima.core.util.datastructure.XYLocation;

import java.util.List;

public class TrilhaGame implements Game<TrilhaState, XYLocation, String> {

    TrilhaState initialState = new TrilhaState();

    @Override
    public TrilhaState getInitialState() {
        return initialState;
    }

    @Override
    public String[] getPlayers() {
        return new String[]{TrilhaState.BRANCO, TrilhaState.PRETO};
    }

    @Override
    public String getPlayer(TrilhaState state) {
        return state.getPlayerToMove();
    }

    @Override
    public List<XYLocation> getActions(TrilhaState state) {
        return state.getUnMarkedPositions();
    }

    @Override
    public TrilhaState getResult(TrilhaState state, XYLocation action) {
        TrilhaState result = state.clone();
        result.mark(action);
        return result;
    }

    @Override
    public boolean isTerminal(TrilhaState state) {
        return state.getUtility() != -1;
    }

    @Override
    public double getUtility(TrilhaState state, String player) {
        double result = state.getUtility();
        if (result != -1) {
            if (player == TrilhaState.BRANCO)
                result = 1 - result;
        } else {
            throw new IllegalArgumentException("Estado não é terminal.");
        }
        return result;
    }
}
