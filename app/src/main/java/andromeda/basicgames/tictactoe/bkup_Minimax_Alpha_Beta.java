package andromeda.basicgames.tictactoe;

public class bkup_Minimax_Alpha_Beta extends MainActivity{

    public static int findBestMove(char[] board) {
        int bestMove = -1; // Initialize to an invalid move
        int bestScore = Integer.MIN_VALUE;

        for (int i = 0; i < board.length; i++) {
            // Check if the spot is empty
            if (board[i] == ' ') {
                // Make the move for the maximizing player (computer)
                board[i] = 'X';

                // Call minimax with alpha and beta initialized
                int score = minimax(board, 0, false, Integer.MIN_VALUE, Integer.MAX_VALUE);

                // Undo the move
                board[i] = ' ';

                // Update the best move if the current score is better
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = i;
                }
            }
        }

        return bestMove;
    }

    private static int minimax(char[] board, int depth, boolean isMaximizing, int alpha, int beta) {
        // Check for terminal conditions: game over or depth limit reached
        int score = evaluate(board);

        if (score != 0) {
            return score;
        }

        if (isBoardFull(board) || depth == 5) {
            return 0;
        }

        // Maximizing player (computer)
        if (isMaximizing) {
            int maxEval = Integer.MIN_VALUE;

            for (int i = 0; i < board.length; i++) {
                if (board[i] == ' ') {
                    board[i] = 'X';
                    int eval = minimax(board, depth + 1, false, alpha, beta);
                    board[i] = ' ';

                    maxEval = Math.max(maxEval, eval);
                    alpha = Math.max(alpha, eval);

                    // Alpha-beta pruning
                    if (beta <= alpha) {
                        break;
                    }
                }
            }

            return maxEval;
        }
        // Minimizing player (opponent)
        else {
            int minEval = Integer.MAX_VALUE;

            for (int i = 0; i < board.length; i++) {
                if (board[i] == ' ') {
                    board[i] = 'O';
                    int eval = minimax(board, depth + 1, true, alpha, beta);
                    board[i] = ' ';

                    minEval = Math.min(minEval, eval);
                    beta = Math.min(beta, eval);

                    // Alpha-beta pruning
                    if (beta <= alpha) {
                        break;
                    }
                }
            }

            return minEval;
        }
    }

    private static int evaluate(char[] board) {
        // Check for a win
        if (isWinner(board, 'X')) {
            return 10; // High positive score for the maximizing player (computer)
        } else if (isWinner(board, 'O')) {
            return -10; // High negative score for the minimizing player (opponent)
        }

        // If no one has won, return 0 for a draw or continue evaluating based on other criteria

        // Example: prioritize having more 'X's in a row or column
        int score = 0;
        score += evaluateLine(board, 0, 1, 2); // top row
        score += evaluateLine(board, 3, 4, 5); // middle row
        score += evaluateLine(board, 6, 7, 8); // bottom row
        score += evaluateLine(board, 0, 3, 6); // left column
        score += evaluateLine(board, 1, 4, 7); // middle column
        score += evaluateLine(board, 2, 5, 8); // right column
        score += evaluateLine(board, 0, 4, 8); // diagonal \
        score += evaluateLine(board, 2, 4, 6); // diagonal /

        return score;
    }

    private static boolean isWinner(char[] board, char player) {
        // Check all possible winning combinations
        return (board[0] == player && board[1] == player && board[2] == player) ||
                (board[3] == player && board[4] == player && board[5] == player) ||
                (board[6] == player && board[7] == player && board[8] == player) ||
                (board[0] == player && board[3] == player && board[6] == player) ||
                (board[1] == player && board[4] == player && board[7] == player) ||
                (board[2] == player && board[5] == player && board[8] == player) ||
                (board[0] == player && board[4] == player && board[8] == player) ||
                (board[2] == player && board[4] == player && board[6] == player);
    }

    private static int evaluateLine(char[] board, int pos1, int pos2, int pos3) {
        int score = 0;

        // Evaluate a line based on the presence of 'X' and 'O'
        if (board[pos1] == 'X') {
            score = 1;
        } else if (board[pos1] == 'O') {
            score = -1;
        }

        if (board[pos2] == 'X') {
            if (score == 1) {
                score = 10; // Two 'X' in a line is a higher score
            } else if (score == -1) {
                return 0; // Opponent has a move in this line, not a good move
            } else {
                score = 1;
            }
        } else if (board[pos2] == 'O') {
            if (score == -1) {
                score = -10; // Two 'O' in a line is a lower score
            } else if (score == 1) {
                return 0; // Opponent has a move in this line, not a good move
            } else {
                score = -1;
            }
        }

        if (board[pos3] == 'X') {
            if (score > 0) {
                score *= 10; // Three 'X' in a line is the highest score
            } else if (score < 0) {
                return 0; // Opponent has a move in this line, not a good move
            } else {
                score = 1;
            }
        } else if (board[pos3] == 'O') {
            if (score < 0) {
                score *= 10; // Three 'O' in a line is the lowest score
            } else if (score > 0) {
                return 0; // Opponent has a move in this line, not a good move
            } else {
                score = -1;
            }
        }

        return score;
    }


    private static boolean isBoardFull(char[] board) {
        for (char c : board) {
            if (c == ' ') {
                return false; // There's an empty spot, the board is not full
            }
        }
        return true; // All spots are filled
    }
}

