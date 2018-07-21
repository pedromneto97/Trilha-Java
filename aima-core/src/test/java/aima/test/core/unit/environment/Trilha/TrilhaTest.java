package aima.test.core.unit.environment.Trilha;

import aima.core.environment.Trilha.TrilhaGame;
import aima.core.environment.Trilha.TrilhaState;
import aima.core.search.adversarial.MinimaxSearch;
import aima.core.util.datastructure.XYLocation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TrilhaTest {
    private TrilhaGame game;
    private TrilhaState state;
    private double epsilon = 0.0001;

    @Before
    public void setUp() {
        this.game = new TrilhaGame();
        this.state = this.game.getInitialState();
    }

    @Test
    public void testCreation() {
        Assert.assertEquals(24, game.getActions(state).size());
        Assert.assertEquals(TrilhaState.BRANCO, game.getPlayer(state));
    }

    @Test
    public void testHashCode() {
        TrilhaState initialState1 = game.getInitialState();
        TrilhaState initialState2 = game.getInitialState();
        Assert.assertEquals(initialState1.hashCode(), initialState2.hashCode());
        TrilhaState state1 = game.getResult(initialState1, new XYLocation(0, 0));
        Assert.assertNotSame(state1.hashCode(), initialState2.hashCode());
        TrilhaState state2 = game.getResult(initialState2, new XYLocation(0, 0));
        Assert.assertEquals(state1.hashCode(), state2.hashCode());
    }

    @Test
    public void testOnCreationBoardIsEmpty() {
        for (int i = 0; i < 24; i++) {
            Assert.assertEquals(TrilhaState.EMPTY, state.getValue(-1, i));
            Assert.assertTrue(state.isEmpty(i));
        }
    }

    @Test
    public void testMakingOneMoveChangesState() {
        state = game.getResult(state, new XYLocation(-1, 0));
        Assert.assertEquals(TrilhaState.BRANCO, state.getValue(-1, 0));
        Assert.assertFalse(state.isEmpty(0));
        Assert.assertEquals(23, game.getActions(state).size());
        Assert.assertEquals(TrilhaState.PRETO, game.getPlayer(state));
    }

    @Test
    public void testMakingTwoMovesChangesState() {
        state = game.getResult(state, new XYLocation(-1, 0));
        state = game.getResult(state, new XYLocation(-1, 1));
        Assert.assertEquals(TrilhaState.PRETO, state.getValue(-1, 1));
        Assert.assertFalse(state.isEmpty(0));
        Assert.assertTrue(state.isEmpty(3));
        Assert.assertEquals(22, game.getActions(state).size());
        Assert.assertEquals(TrilhaState.BRANCO, game.getPlayer(state));
    }

    @Test
    public void testVerificaTrinca() {
        state.mark(-1, 0);
        state.mark(-1, 8);
        state.mark(-1, 7);
        Assert.assertFalse(state.verificaTrinca(15));
        state.mark(-1, 15);
        Assert.assertTrue(state.verificaTrinca(6));
    }

    @Test
    public void testRemovePeca() {
        state.mark(-1, 0);
        state.mark(-1, 8);
        state.mark(-1, 7);
        state.mark(-1, 15);
        state.mark(-1, 6);
        Assert.assertEquals(TrilhaState.BRANCO, state.getPlayerToMove());
        Assert.assertEquals("1", state.getValue(-1, 26));
        Assert.assertEquals(2, state.getMoves().size());
        state.mark(-1, 15);
        Assert.assertEquals(TrilhaState.EMPTY, state.getValue(-1, 15));
        Assert.assertEquals("0", state.getValue(-1, 26));
        Assert.assertEquals("3", state.getValue(-1, 24));
        Assert.assertEquals("2", state.getValue(-1, 25));
    }

    @Test
    public void testMovePeca() {
        for (int i = 0; i < 18; i++) {
            state.mark(-1, i);
        }
        state.mark(-1, 0);
        Assert.assertEquals(TrilhaState.BRANCO, state.getPlayerToMove());
        Assert.assertEquals(1, state.getMoves().size());
        Assert.assertEquals("9", state.getValue(-1, 24));
        state.mark(16, 23);
        Assert.assertEquals(TrilhaState.PRETO, state.getPlayerToMove());
        Assert.assertEquals(TrilhaState.EMPTY, state.getValue(-1, 16));
        Assert.assertEquals(TrilhaState.BRANCO, state.getValue(-1, 23));

    }

    @Test
    public void testMinmaxValueCalculation() {
        MinimaxSearch<TrilhaState, XYLocation, String> search = MinimaxSearch
                .createFor(game);
        Assert.assertTrue(epsilon > Math.abs(search.maxValue(state,
                TrilhaState.BRANCO) - 0.5));
        Assert.assertTrue(epsilon > Math.abs(search.minValue(state,
                TrilhaState.PRETO) - 0.5));

        // x o x
        // o o x
        // - - -
        // next move: x
        state.mark(0, 0); // x
        state.mark(1, 0); // o
        state.mark(2, 0); // x

        state.mark(0, 1); // o
        state.mark(2, 1); // x
        state.mark(1, 1); // o

        Assert.assertTrue(epsilon > Math.abs(search.maxValue(state,
                TrilhaState.BRANCO) - 1));
        Assert.assertTrue(epsilon > Math.abs(search.minValue(state,
                TrilhaState.PRETO)));
        XYLocation action = search.makeDecision(state);
        Assert.assertEquals(new XYLocation(2, 2), action);
    }

    @Test
    public void testMinmaxDecision() {
        MinimaxSearch<TrilhaState, XYLocation, String> search = MinimaxSearch
                .createFor(game);
        search.makeDecision(state);
        int expandedNodes = search.getMetrics().getInt("expandedNodes");
        Assert.assertEquals(549945, expandedNodes);
    }

}
