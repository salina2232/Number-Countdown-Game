import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class CountDown implements ActionListener{
    private static final int BUTTON_NUMBER=16;// Define the number of buttons
    private int index = 0; // Current index to track if numbers are clicked in order
    private JLabel message;// Label for displaying messages
    private final Random random = new Random();// Random number generator
    private JFrame frame;// JFrame window
    private final List<Integer> checkNumber = new ArrayList<>();//List unchecked numbers
    private final List<JButton> buttonList = new ArrayList<>();//List to buttons' name
    private final List<Integer> sortedNumbers = new ArrayList<>();// List to store numbers in descending order
    private Timer time;// Timer for counting elapsed time
    private long startTime;// Record the start time of the game
    private JButton restartButton; // Button to restart the game

    public CountDown(){
        generateRandomNumbers();// Generate random numbers
        Board(); // Initialize the game board
        startTimer();// Start the timer
    }
    private void startTimer() {
        startTime = System.currentTimeMillis();// Record the start time of the game
        time = new Timer(1000, e -> { // Calculate elapsed time
            long elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000;
            frame.setTitle("Number Button Game - Time Elapsed: " + elapsedSeconds + " seconds"); // Update the window title to display elapsed time
        });
        time.start();// Start the timer
    }

    protected void Board(){
        frame = new JFrame("Number Button Game");// Create JFrame
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);// Set close operation
        frame.setSize(400,450);// Set window size

        GridLayout gridLayout = new GridLayout(4,4);
        JPanel container = new JPanel(gridLayout);// Create a panel container
        container.setPreferredSize(new Dimension(400,400));// Set container size

        JPanel longStrip = new JPanel();// Create a long strip panel
        message = new JLabel("Click the numbers in descending order", SwingConstants.CENTER); // Message label
        longStrip.setPreferredSize(new Dimension(400,50));// Set long strip panel size
        longStrip.add(message);// Add message label to the bottom panel


        // Create and add the restart button
        restartButton = new JButton("Restart");
        restartButton.addActionListener(this);
        longStrip.add(restartButton);

        // Add panels to the JFrame
        frame.add(container,BorderLayout.CENTER);// Add the container to the frame's center
        frame.add(longStrip,BorderLayout.SOUTH);// Add the long strip to the frame's south

        Font font = new Font("Arial Black", Font.PLAIN, 26);// Set font and color
        Color color = new Color(0, 129, 129);

        for(Integer number:checkNumber) {// Add buttons to the container
            JButton button = new JButton(number.toString());
            button.setFont(font);
            button.setForeground(color);
            button.addActionListener(this);
            container.add(button);
            buttonList.add(button);
        }

        frame.pack(); // Adjust the window size to fit the components
        frame.setVisible(true); // Set the window visible
    }

    private void generateRandomNumbers() {// Generate random numbers
        checkNumber.clear(); // Clear the list for new game
        while (checkNumber.size() < BUTTON_NUMBER) {
            int newNumber = random.nextInt(21) - 10;
            if (!checkNumber.contains(newNumber)) {
                checkNumber.add(newNumber);
            }
        }
        sortedNumbers.addAll(checkNumber);
        updateButtonTitles();
        sortedNumbers.sort(Collections.reverseOrder());// Sort numbers in descending order

    }

    private void updateButtonTitles() {
        for (int i = 0; i < buttonList.size(); i++) {
            buttonList.get(i).setText(sortedNumbers.get(i).toString());
        }
    }



    public static void main(String[] arg){
        new CountDown();//Create a CountDown object, start the game
    }

    public void actionPerformed(ActionEvent h){
        if (h.getSource() == time) {
            return; // Ignore timer events
        }

        if (h.getSource() == restartButton) {
            resetGame();
            return;
        }

        JButton clickButton = (JButton)h.getSource(); // Get the clicked button
        int clickNumber = Integer.parseInt(clickButton.getText()); // Get the number displayed on the button


        if(clickNumber == sortedNumbers.get(index)){// Check if the clicked number matches the next expected number
           index++;// If matched, update the index
           if(index == BUTTON_NUMBER){
               Win(); // If all numbers are clicked, game won
           }
       }else{
           GameOver();// If clicked number is incorrect, game over
       }
    }

    private void Win() {
        time.stop(); // Stop the timer
        long endTimeSeconds = (System.currentTimeMillis() - startTime) / 1000; // Calculate the time when the game ends
        for (JButton button : buttonList) {
            button.setEnabled(false);
        }
        message.setText("Congratulations! You finished in " + endTimeSeconds + " seconds. Click 'Restart' to play again.");
        // Player can click 'Restart' to play again
    }

    private void GameOver() {
        time.stop(); // Stop the timer
        for (JButton button : buttonList) {
            button.setEnabled(false);
        }
        message.setText("Wrong number clicked! Game Over. Click 'Restart' to play again.");
        // Player can click 'Restart' to play again
    }
    private void resetGame() {
        // Stop the current timer
        time.stop();

        // Reset the game state
        index = 0;
        checkNumber.clear();
        sortedNumbers.clear();
        buttonList.forEach(button -> button.setEnabled(true)); // Re-enable all buttons
        message.setText("Click the numbers in descending order."); // Reset the message

        // Regenerate random numbers and update the buttons with new numbers
        generateRandomNumbers();

        // Reset the start time to the current time
        startTime = System.currentTimeMillis();

        // Create a new timer to track the new game's elapsed time
        time = new Timer(1000, e -> {
            long elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000;
            frame.setTitle("Number Button Game - Time Elapsed: " + elapsedSeconds + " seconds");
        });

        // Start the new timer
        time.start();

        // Update the window title to reflect the start of a new game
        frame.setTitle("Number Button Game");
    }
}





