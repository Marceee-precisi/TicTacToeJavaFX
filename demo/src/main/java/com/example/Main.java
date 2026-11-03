package com.example;

import java.util.Scanner;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/** JavaFX application for a human-versus-CPU game of Tic-Tac-Toe. */
public class Main extends Application {
    private final Board board = new Board();
    private final HumanPlayer human = new HumanPlayer('X', new Scanner(""));
    private final CpuPlayer cpu = new CpuPlayer('O');
    private final Button[][] cells = new Button[3][3];
    private Label statusLabel;
    private boolean gameOver;

    /** Creates and displays the JavaFX game window. */
    @Override
    public void start(Stage stage) {
        Label title = new Label("TIC-TAC-TOE");
        title.getStyleClass().add("title");

        statusLabel = new Label(currentTurnMessage());
        statusLabel.getStyleClass().add("status");

        GridPane boardView = createBoardView();

        Label hint = new Label(human.getName() + " is X; " + cpu.getName() + " is O");
        hint.getStyleClass().add("hint");
        Button restartButton = new Button("New game");
        restartButton.getStyleClass().add("restart-button");
        restartButton.setOnAction(event -> restartGame());
        HBox controls = new HBox(18, hint, restartButton);
        controls.setAlignment(Pos.CENTER);

        VBox layout = new VBox(18, title, statusLabel, boardView, controls);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(36));
        layout.getStyleClass().add("content");

        Scene scene = new Scene(layout, 520, 620);
        scene.getStylesheets().add(getClass().getResource("/app.css").toExternalForm());
        stage.setTitle("Tic-Tac-Toe");
        stage.setScene(scene);
        stage.setMinWidth(500);
        stage.setMinHeight(560);
        stage.show();
    }

    /** Starts the JavaFX application. */
    public static void main(String[] args) {
        launch(args);
    }

    private GridPane createBoardView() {
        GridPane boardView = new GridPane();
        boardView.setHgap(11);
        boardView.setVgap(11);
        boardView.setAlignment(Pos.CENTER);
        boardView.getStyleClass().add("board");

        for (int row = 0; row < cells.length; row++) {
            for (int column = 0; column < cells[row].length; column++) {
                int boardRow = row;
                int boardColumn = column;
                Button cell = new Button();
                cell.getStyleClass().add("cell");
                cell.setOnAction(event -> playMove(boardRow, boardColumn));
                cells[row][column] = cell;
                boardView.add(cell, column, row);
            }
        }
        return boardView;
    }

    private void playMove(int row, int column) {
        if (gameOver || !human.makeMove(board, row, column)) {
            return;
        }
        updateBoardView();
        if (finishIfGameOver(human)) {
            return;
        }

        statusLabel.setText(cpu.getName() + " is choosing...");
        cpu.makeMove(board);
        updateBoardView();
        if (!finishIfGameOver(cpu)) {
            statusLabel.setText(currentTurnMessage());
        }
    }

    private boolean finishIfGameOver(Player player) {
        if (board.checkWin()) {
            gameOver = true;
            statusLabel.setText(player.getName() + " wins!");
            updateBoardView();
            highlightWinningLine();
            return true;
        }
        if (board.isFull()) {
            gameOver = true;
            statusLabel.setText("It's a draw!");
            updateBoardView();
            return true;
        }
        return false;
    }

    private void updateBoardView() {
        for (int row = 0; row < cells.length; row++) {
            for (int column = 0; column < cells[row].length; column++) {
                char marker = board.getCell(row, column);
                Button cell = cells[row][column];
                cell.setText(marker == ' ' ? "" : Character.toString(marker));
                cell.setDisable(gameOver || marker != ' ');
                cell.getStyleClass().removeAll("x-cell", "o-cell", "winning-cell");
                if (marker == 'X') {
                    cell.getStyleClass().add("x-cell");
                } else if (marker == 'O') {
                    cell.getStyleClass().add("o-cell");
                }
            }
        }
    }

    private void highlightWinningLine() {
        int[][] winningLine = findWinningLine();
        for (int[] position : winningLine) {
            cells[position[0]][position[1]].getStyleClass().add("winning-cell");
        }
    }

    private int[][] findWinningLine() {
        for (int row = 0; row < 3; row++) {
            if (sameNonEmpty(board.getCell(row, 0), board.getCell(row, 1), board.getCell(row, 2))) {
                return new int[][]{{row, 0}, {row, 1}, {row, 2}};
            }
        }
        for (int column = 0; column < 3; column++) {
            if (sameNonEmpty(board.getCell(0, column), board.getCell(1, column), board.getCell(2, column))) {
                return new int[][]{{0, column}, {1, column}, {2, column}};
            }
        }
        if (sameNonEmpty(board.getCell(0, 0), board.getCell(1, 1), board.getCell(2, 2))) {
            return new int[][]{{0, 0}, {1, 1}, {2, 2}};
        }
        if (sameNonEmpty(board.getCell(0, 2), board.getCell(1, 1), board.getCell(2, 0))) {
            return new int[][]{{0, 2}, {1, 1}, {2, 0}};
        }
        throw new IllegalStateException("A winning board must contain a winning line");
    }

    private boolean sameNonEmpty(char first, char second, char third) {
        return first != ' ' && first == second && second == third;
    }

    private void restartGame() {
        board.initialize();
        gameOver = false;
        updateBoardView();
        statusLabel.setText(currentTurnMessage());
    }

    private String currentTurnMessage() {
        return human.getName() + " (X) - your turn";
    }
}
