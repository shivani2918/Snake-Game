package project;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class Board extends JFrame {
    public Board() {
        add(new GameBoard());
        setTitle("Simple Snake Game");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame ex = new Board();
            ex.setVisible(true);
        });
    }
}

class GameBoard extends JPanel implements ActionListener {
    private final int TILE_SIZE = 20;
    private final int WIDTH = 20;
    private final int HEIGHT = 20;
    private final int BOARD_SIZE = WIDTH * HEIGHT;
    private final ArrayList<Point> snake = new ArrayList<>();
    private Point apple;
    private int direction = KeyEvent.VK_RIGHT;
    private boolean growing = false;
    private boolean gameOver = false;
    private Timer timer;

    public GameBoard() {
        setPreferredSize(new Dimension(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int newDirection = e.getKeyCode();
                if ((newDirection == KeyEvent.VK_LEFT && direction != KeyEvent.VK_RIGHT) ||
                    (newDirection == KeyEvent.VK_RIGHT && direction != KeyEvent.VK_LEFT) ||
                    (newDirection == KeyEvent.VK_UP && direction != KeyEvent.VK_DOWN) ||
                    (newDirection == KeyEvent.VK_DOWN && direction != KeyEvent.VK_UP)) {
                    direction = newDirection;
                }
            }
        });

        initGame();
    }

    private void initGame() {
        snake.clear();
        snake.add(new Point(WIDTH / 2, HEIGHT / 2));
        spawnApple();
        timer = new Timer(100, this);
        timer.start();
    }

    private void spawnApple() {
        Random rand = new Random();
        apple = new Point(rand.nextInt(WIDTH), rand.nextInt(HEIGHT));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gameOver) {
            showGameOver(g);
            return;
        }

        g.setColor(Color.RED);
        g.fillRect(apple.x * TILE_SIZE, apple.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
    }

    private void showGameOver(Graphics g) {
        String msg = "Game Over";
        g.setColor(Color.WHITE);
        g.drawString(msg, getWidth() / 2 - g.getFontMetrics().stringWidth(msg) / 2, getHeight() / 2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) return;

        Point head = new Point(snake.get(0));
        switch (direction) {
            case KeyEvent.VK_LEFT: head.x--; break;
            case KeyEvent.VK_RIGHT: head.x++; break;
            case KeyEvent.VK_UP: head.y--; break;
            case KeyEvent.VK_DOWN: head.y++; break;
        }

        if (head.equals(apple)) {
            growing = true;
            spawnApple();
        }

        if (head.x < 0 || head.x >= WIDTH || head.y < 0 || head.y >= HEIGHT || snake.contains(head)) {
            gameOver = true;
            timer.stop();
            repaint();
            return;
        }

        snake.add(0, head);
        if (!growing) {
            snake.remove(snake.size() - 1);
        } else {
            growing = false;
        }

        repaint();
    }
}
