package com.example;

/**
 * 
 * Defines the shared marker and move contract for a Tic-Tac-Toe player.
 */ 
public abstract class Player {
    protected final char marker;
    private String name;

    /**
     * Creates a player with the given marker.
     *
     * @param marker the player's marker, either {@code X} or {@code O}
     * @throws IllegalArgumentException if the marker is not {@code X} or {@code O}
     */
    protected Player(char marker) {
        this(marker, "Player #1");
    }

    /**
     * Creates a player with the given marker and default name.
     *
     * @param marker the player's marker, either {@code X} or {@code O}
     * @param defaultName the name assigned when the player is created
     * @throws IllegalArgumentException if the marker is not {@code X} or {@code O}
     */
    protected Player(char marker, String defaultName) {
        validateMarker(marker);
        this.marker = marker;
        setName(defaultName);
    }

    /** Validates a player marker before a subclass initializes its own state. */
    protected static void validateMarker(char marker) {
        if (marker != 'X' && marker != 'O') {
            throw new IllegalArgumentException("A player marker must be X or O");
        }
    }

    /**
     * Returns this player's marker.
     *
     * @return the player's marker
     */
    public char getMarker() {
        return marker;
    }

    /**
     * Returns this player's name.
     *
     * @return the player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets this player's name.
     *
     * @param name the new name
     * @throws IllegalArgumentException if {@code name} is null or blank
     */
    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("A player name must not be blank");
        }
        this.name = name.strip();
    }

    /**
     * Makes a move on the supplied board.
     *
     * @param board board on which to make the move
     */
    public abstract void makeMove(Board board);
}