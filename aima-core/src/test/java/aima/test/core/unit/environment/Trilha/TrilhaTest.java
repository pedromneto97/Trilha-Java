package aima.test.core.unit.environment.Trilha;

import aima.core.environment.Trilha.TrilhaGame;
import aima.core.environment.Trilha.TrilhaState;
import aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch;
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
    public void testFim() {
        state.mark(-1, 0);//Branco +1
        state.mark(-1, 8);//Preto +1
        state.mark(-1, 7);//Branco +2
        state.mark(-1, 15);//Preto +2
        state.mark(-1, 6);//Branco +3
        state.mark(-1, 15);//Branco Remove 8
        state.mark(-1, 15);//Preto +3
        state.mark(-1, 5);//Branco +4
        state.mark(-1, 9);//Preto +4
        state.mark(-1, 4);//Branco +5
        state.mark(-1, 9);//Branco Remove 7
        state.mark(-1, 9);//Preto +5
        state.mark(-1, 2);//Branco +6
        state.mark(-1, 12);//Preto +6
        state.mark(-1, 3);//Branco +7
        state.mark(-1, 12);//Branco Remove 6
        state.mark(-1, 12);//Preto +7
        state.mark(-1, 1);//Branco +8
        state.mark(-1, 12);//Branco Remove 5
        state.mark(-1, 12);//Preto +8
        state.mark(-1, 13);//Branco +9
        state.mark(-1, 20);//Preto +9
        state.mark(3, 11);//Branco Move
        state.mark(15, 14);//Preto Move
        state.mark(11, 3);//Branco Move
        state.mark(-1, 12);//Branco Remove 4
        state.mark(14, 15);//Preto Move
        state.mark(3, 11);//Branco Move
        state.mark(15, 14);//Preto Move
        state.mark(11, 3);//Branco Move
        state.mark(-1, 8);//Branco Remove 3
        state.mark(14, 15);//Preto Move
        state.mark(3, 11);//Branco Move
        state.mark(15, 14);//Preto Move
        state.mark(11, 3);//Branco Move
        state.mark(-1, 14);//Branco Remove 2
        //GG
        Assert.assertTrue(game.isTerminal(state));
    }

    @Test
    public void testIterativeDeepeningAlphaBetaDecision() {
        IterativeDeepeningAlphaBetaSearch<TrilhaState, XYLocation, String> search = IterativeDeepeningAlphaBetaSearch
                .createFor(game, 0.0, 1.0, 100);
        XYLocation ac = search.makeDecision(state);
        int expandedNodes = search.getMetrics().getInt("expandedNodes");
        System.out.println(ac);
    }

}
