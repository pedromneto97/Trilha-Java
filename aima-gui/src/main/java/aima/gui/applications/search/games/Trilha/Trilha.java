/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aima.gui.applications.search.games.Trilha;

import aima.core.environment.Trilha.TrilhaGame;
import aima.core.environment.Trilha.TrilhaState;
import aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch;
import aima.core.util.datastructure.XYLocation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Paulo
 */
public class Trilha extends JFrame {

    JPanel panel = new JPanel();
    JPanelWithBackground background = null;
    JButton buttons[] = new JButton[24];
    ImageIcon icon = new ImageIcon("tabuleiro.png");
    JLabel label = new JLabel(icon);
    private Handler handler = new Handler();
    private TrilhaGame game;
    private TrilhaState state;
    private int anterior;
    private IterativeDeepeningAlphaBetaSearch<TrilhaState, XYLocation, String> search;

    public Trilha() {
        //Instancia o jogo
        this.game = new TrilhaGame();
        //Pega o estado inicial
        this.state = this.game.getInitialState();
        //Cria a pesquisa, alterar o último parâmetro, altera o tempo de busca da IA
        this.search = IterativeDeepeningAlphaBetaSearch.createFor(game, 0.0, 1.0, 120);
        this.anterior = -1;
        setLayout(null);
        for (int i = 0; i < 24; i++) {
            buttons[i] = new JButton("");
            buttons[i].addActionListener(handler);
            add(buttons[i]);
            buttons[i].setFont(new Font("Arial", Font.PLAIN, 12));
            buttons[i].setMargin(new Insets(0, 0, 0, 0));
        }
        background = new JPanelWithBackground("tabuleiro.png");
        background.setSize(700, 700);
        getContentPane().add(background);
        buttons[0].setBounds(65, 65, 35, 35);
        buttons[1].setBounds(337, 65, 35, 35);
        buttons[2].setBounds(610, 65, 35, 35);
        buttons[3].setBounds(610, 336, 35, 35);
        buttons[4].setBounds(610, 609, 35, 35);
        buttons[5].setBounds(337, 609, 35, 35);
        buttons[6].setBounds(65, 609, 35, 35);
        buttons[7].setBounds(65, 336, 35, 35);
        buttons[8].setBounds(163, 162, 35, 35);
        buttons[9].setBounds(337, 162, 35, 35);
        buttons[10].setBounds(512, 162, 35, 35);
        buttons[11].setBounds(512, 336, 35, 35);
        buttons[12].setBounds(512, 512, 35, 35);
        buttons[13].setBounds(337, 512, 35, 35);
        buttons[14].setBounds(163, 512, 35, 35);
        buttons[15].setBounds(163, 336, 35, 35);
        buttons[16].setBounds(260, 259, 35, 35);
        buttons[17].setBounds(337, 259, 35, 35);
        buttons[18].setBounds(415, 259, 35, 35);
        buttons[19].setBounds(415, 336, 35, 35);
        buttons[20].setBounds(415, 414, 35, 35);
        buttons[21].setBounds(337, 414, 35, 35);
        buttons[22].setBounds(260, 414, 35, 35);
        buttons[23].setBounds(260, 336, 35, 35);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        setTitle("Trilha");
        setVisible(true);
        validate();
    }

    public static void main(String[] args) {
        Trilha frameComBackground = new Trilha();
        frameComBackground.setVisible(true);
        frameComBackground.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public class Handler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JButton) {
                for (int i = 0; i < 24; i++) {
                    if (e.getSource() == buttons[i]) {
                        XYLocation move;
                        if (!state.getMoves().isEmpty() && state.getMoves().get(0).getXCoOrdinate() != -1) {
                            if (anterior == -1 && ((JButton) e.getSource()).getText() == "B") {
                                anterior = i;
                                return;
                            } else {
                                move = new XYLocation(anterior, i);
                                anterior = -1;
                            }
                        } else {
                            move = new XYLocation(anterior, i);
                        }

                        if (state.getMoves().contains(move)) {
                            if (move.getXCoOrdinate() != -1)
                                buttons[move.getXCoOrdinate()].setText("");
                            if (state.getValue(-1, 26) == "1") {
                                ((JButton) e.getSource()).setText("");
                            } else {
                                ((JButton) e.getSource()).setText("B");
                            }
                            state.mark(move);
                            while (state.getPlayerToMove() == TrilhaState.PRETO) {
                                XYLocation ac = search.makeDecision(state);
                                if (ac.getXCoOrdinate() != -1)
                                    buttons[ac.getXCoOrdinate()].setText("");

                                if (state.getValue(-1, 26) == "1") {
                                    buttons[ac.getYCoOrdinate()].setText("");
                                } else {
                                    buttons[ac.getYCoOrdinate()].setText("P");
                                }

                                state.mark(ac);
                            }
                        }
                        return;
                    }
                }
            }
        }
    }

}
