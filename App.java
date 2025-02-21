import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class App {
    private static volatile boolean is_game_turned_on = false;
    public static void main(String[] args) throws Exception {
        //Creating a frame, where all graphical staff will happen
        JFrame frame = new JFrame("GameOfLife");
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        

        //Creating a plane, where game will work
        JPanel gridPanel = new JPanel(new GridLayout(50, 50));
         JToggleButton[][] buttons = new JToggleButton[50][50];
        for (int row = 0; row < 50; row++){ 
            for (int column = 0; column < 50; column++) {
                JToggleButton button = new JToggleButton();
    
                button.setOpaque(true);
                button.setBorderPainted(false); 
                button.setBackground(Color.black);
    
    
                button.addItemListener(new ItemListener() {

                    public void itemStateChanged(ItemEvent e) {
                        if (button.isSelected()) {
                            button.setBackground(Color.WHITE);
                        } else {
                            button.setBackground(Color.BLACK);
                        }
                    }
                });

                buttons[row][column] = button;
                gridPanel.add(button);
            }
        }

        frame.add(gridPanel, BorderLayout.CENTER);

        //Adding the play button
        JButton playButton = new JButton("Start");
        frame.add(playButton, BorderLayout.SOUTH);
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                is_game_turned_on = !is_game_turned_on;
                playButton.setText(is_game_turned_on ? "Pause" : "Start");
            }
        });

        //GAME loop
        frame.setVisible(true);
        while(true){
            if (is_game_turned_on){
                check_the_life(buttons);
            }

            Thread.sleep(1000);
        }
    }

    private static void check_the_life(JToggleButton[][] buttons) {
        // Marking cells to live or die
        for (int row = 0; row < 50; row++) {
            for (int column = 0; column < 50; column++) {
                int life_score = 0;
                JToggleButton checked_button = buttons[row][column];
                
                for (int x = -1; x < 2; x++) {
                    for (int y = -1; y < 2; y++) {
                        try {
                            if (x == 0 && y == 0) continue;
                            life_score += buttons[row + x][column + y].isSelected() ? 1 : 0;
                        } catch (ArrayIndexOutOfBoundsException e) {
                            continue;
                        }
                    }
                }
    
                // Determine whether the cell should live or die based on number of neighbours
                if (checked_button.isSelected()) {
                    checked_button.putClientProperty("Marked", (life_score == 2 || life_score == 3));
                } else {
                    checked_button.putClientProperty("Marked", life_score == 3);
                }
            }
        }
    
        // Applying the life or death change
        for (int row = 0; row < 50; row++) {
            for (int column = 0; column < 50; column++) {
                JToggleButton checked_button = buttons[row][column];
                Boolean mark = (Boolean) checked_button.getClientProperty("Marked");
                checked_button.setSelected(Boolean.TRUE.equals(mark));
                checked_button.putClientProperty("Marked", null);
            }
        }
    }

}

