import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.FileWriter;

/**
 * In Main, we scan the input data from the file, distribute pieces according their type and color and consider answers.
 * We report error messages if input data is invalid or the input file does not exist,
 * otherwise we count moves and captures and output them into the output file
 */
public class Main {
    /**
     * We create a constructor as Main should not have public constructors or default constructors.
     */
    private Main main = new Main();
    /**
     * We create our future chess board.
     */
    private static Board chessBoard;

    /**
     * In this method we check whether the input file exists, count the number of line in the file at first.
     * Then get board size, number of pieces and receive pieces data, switch every piece to its type and "arrange"
     * them on the board, for every piece we count its number of moves and captures and put it in the output file
     * If some input data is invalid we use exceptions and issue a warning message
     *
     * @param args contains the command-line arguments passed to the Java program upon invocation
     */
    public static void main(String[] args) {
        int lines = 0;
        try {
            File file = new File("input.txt");
            FileInputStream fileStream = new FileInputStream(file);
            InputStreamReader input = new InputStreamReader(fileStream);
            BufferedReader reader = new BufferedReader(input);
            String line;
            while ((line = reader.readLine()) != null) {
                lines += 1;
            }
        } catch (Exception e) {
            System.exit(0);
        }

        Scanner sc = null;
        PrintWriter pw = null;

        try {
            sc = new Scanner(new File("input.txt"));
            pw = new PrintWriter(new FileWriter("output.txt"));
        } catch (Exception exception) {
            System.exit(0);
        }
        try {
            try {
                File file = new File("input.txt");
                if (!file.isFile()) {
                    throw new InvalidInputException();
                }
            } catch (Exception e) {
                throw new InvalidInputException();
            }
        } catch (Exception exception) {
            pw.println(exception.getMessage());
            pw.close();
        }
        try {
            String n = sc.nextLine();
            String m = sc.nextLine();
            int boardSize;
            try {
                boardSize = Integer.parseInt(n);
                final int minBoardSize = 3;
                final int maxBoardSize = 1000;
                if (boardSize < minBoardSize || boardSize > maxBoardSize) {
                    throw new InvalidBoardSizeException();
                }
            } catch (Exception e) {
                throw new InvalidBoardSizeException();
            }

            int numberOfPieces;
            try {
                numberOfPieces = Integer.parseInt(m);
                if (numberOfPieces < 2 || numberOfPieces > boardSize * boardSize) {
                    throw new InvalidNumberOfPiecesException();
                }
                if (lines - 2 != numberOfPieces) {
                    throw new InvalidNumberOfPiecesException();
                }
            } catch (Exception e) {
                throw new InvalidNumberOfPiecesException();
            }
            chessBoard = new Board(boardSize);
            String piece;
            String color;
            int positionX;
            int positionY;
            PiecePosition positionOfPiece;
            PieceColor colorOfPiece;
            ChessPiece chessPieces;
            PiecePosition[] arrayOfPositions = new PiecePosition[numberOfPieces];
            int numberOfWhiteKings = 0;
            int numberOfBlackKings = 0;
            for (int i = 0; i < numberOfPieces; i++) {
                piece = sc.next();
                color = sc.next();
                try {
                    positionX = Integer.parseInt(sc.next());
                    positionY = Integer.parseInt(sc.next());
                    if (positionY < 1 || positionY > boardSize || positionX < 1 || positionX > boardSize) {
                        throw new InvalidPiecePositionException();
                    }
                } catch (Exception e) {
                    throw new InvalidPiecePositionException();
                }
                positionOfPiece = new PiecePosition(positionX, positionY);
                arrayOfPositions[i] = positionOfPiece;
                colorOfPiece = PieceColor.parse(color);
                switch (piece) {
                    case "Knight":
                        chessPieces = new Knight(positionOfPiece, colorOfPiece);
                        break;
                    case "King":
                        chessPieces = new King(positionOfPiece, colorOfPiece);
                        if (colorOfPiece == PieceColor.WHITE) {
                            numberOfWhiteKings += 1;
                        }
                        if (colorOfPiece == PieceColor.BLACK) {
                            numberOfBlackKings += 1;
                        }
                        break;
                    case "Pawn":
                        chessPieces = new Pawn(positionOfPiece, colorOfPiece);
                        break;
                    case "Bishop":
                        chessPieces = new Bishop(positionOfPiece, colorOfPiece);
                        break;
                    case "Rook":
                        chessPieces = new Rook(positionOfPiece, colorOfPiece);
                        break;
                    case "Queen":
                        chessPieces = new Queen(positionOfPiece, colorOfPiece);
                        break;
                    default:
                        throw new InvalidPieceNameException();
                }
                chessBoard.addPiece(chessPieces);
            }
            if (numberOfWhiteKings != 1 || numberOfBlackKings != 1) {
                throw new InvalidGivenKingsException();
            }
            ChessPiece newPiece;
            for (int i = 0; i < numberOfPieces; i++) {
                newPiece = chessBoard.getPiece(arrayOfPositions[i]);
                pw.print(chessBoard.getPiecePossibleMovesCount(newPiece));
                pw.print(" ");
                pw.println(chessBoard.getPiecePossibleCapturesCount(newPiece));
            }
            pw.close();
            sc.close();
        } catch (Exception exception) {
            pw.println(exception.getMessage());
            pw.close();
        }
    }
}

/**
 * Class which extends Exception and contains a method with string warning related to board size.
 */
class InvalidBoardSizeException extends Exception {
    /**
     * Method which returns a string with warning message.
     *
     * @return "Invalid board size" string in case of illegal board size.
     */
    public String getMessage() {
        return "Invalid board size";
    }
}

/**
 * Class which extends Exception and contains a method with string warning related to number of pieces on the board.
 */
class InvalidNumberOfPiecesException extends Exception {
    /**
     * Method which returns a string with warning message.
     *
     * @return "Invalid number of pieces" string in case of illegal number of pieces on this board
     */
    public String getMessage() {
        return "Invalid number of pieces";
    }
}

/**
 * Class which extends Exception and contains a method with string warning related to invalid names of given pieces.
 */
class InvalidPieceNameException extends Exception {
    /**
     * Method which returns a string with warning message.
     *
     * @return "Invalid piece name" string in case of wrong piece name
     */
    public String getMessage() {
        return "Invalid piece name";
    }
}

/**
 * Class which extends Exception and contains a method with string warning related to invalid colors of given pieces.
 */
class InvalidPieceColorException extends Exception {
    /**
     * Method which returns a string with warning message.
     *
     * @return "Invalid piece color" string in case of illegal piece color.
     */
    public String getMessage() {
        return "Invalid piece color";
    }
}

/**
 * Class which extends Exception and contains a method with string warning related to invalid positions of given pieces.
 */
class InvalidPiecePositionException extends Exception {
    /**
     * Method which returns a string with warning message.
     *
     * @return "Invalid piece position" string in case of illegal piece positions
     */
    public String getMessage() {
        return "Invalid piece position";
    }
}

/**
 * Class which extends Exception and contains a method with string warning related to invalid inputs with kings.
 */
class InvalidGivenKingsException extends Exception {
    /**
     * Method which returns a string with warning message.
     *
     * @return "Invalid given Kings" string in case of illegal kings input
     */
    public String getMessage() {
        return "Invalid given Kings";
    }
}

/**
 * Class which extends Exception and contains a method with string warning related to invalid inputs.
 */
class InvalidInputException extends Exception {
    /**
     * Method which returns a string with warning message.
     *
     * @return "Invalid input" string
     */
    public String getMessage() {
        return "Invalid input";
    }
}

/**
 * Class which is used to process given pieces: add their positions on a board, count their possible moves and captures.
 * It has also a method, which can return a chess piece using its position
 */
class Board {
    /**
     * map which contains squares occupied by given chess pieces.
     * Key - coordinates of taken squares in string representation
     * Value - chess piece, which is on this position
     */
    private Map<String, ChessPiece> positionsToPieces = new HashMap<>();
    /**
     * the size of the board, which is represented as the number of squares vertically and horizontally.
     */
    private int size;

    /**
     * The method receives board size and assigns it to "size" private field.
     *
     * @param boardSize the number of squares on the board horizontally or vertically
     */
    Board(int boardSize) {
        this.size = boardSize;
    }

    /**
     * Method which returns the number of possible moves from the current position.
     *
     * @param piece currently processed chess piece
     * @return integer value of the number of all possible moves on the board
     */
    public int getPiecePossibleMovesCount(ChessPiece piece) {
        return piece.getMovesCount(positionsToPieces, size);
    }

    /**
     * Method which returns the number of possible captures from the current position.
     *
     * @param piece currently processed chess piece
     * @return integer value of the number of all possible captures on the board
     */
    public int getPiecePossibleCapturesCount(ChessPiece piece) {
        return piece.getCapturesCount(positionsToPieces, size);
    }

    /**
     * Method which adds a new chess piece into the map with all given ones and their positions or returns an exception.
     *
     * @param piece new chess piece
     * @throws InvalidPiecePositionException is used in case of another chess piece already standing on the board
     */
    public void addPiece(ChessPiece piece) throws InvalidPiecePositionException {
        if (positionsToPieces.containsKey(piece.getPosition().toString())) {
            throw new InvalidPiecePositionException();
        }
        positionsToPieces.put(piece.getPosition().toString(), piece);
    }

    /**
     * Method which returns the piece by its position.
     *
     * @param position coordinates of the piece
     * @return ChessPiece
     */
    public ChessPiece getPiece(PiecePosition position) {
        return positionsToPieces.get(position.toString());
    }
}

/**
 * The class processes chess piece position.
 * It can return horizontal and vertical coordinates separately as integers or together in string representation
 */
class PiecePosition {
    /**
     * contains the horizontal position of the piece.
     */
    private int x;
    /**
     * contains the vertical position of the piece.
     */
    private int y;

    /**
     * The method receives two coordinates of the piece and assigns them to "x" and "y" fields.
     *
     * @param onX horizontal coordinate
     * @param onY vertical coordinate
     */
    PiecePosition(int onX, int onY) {
        this.x = onX;
        this.y = onY;
    }

    /**
     * The method is a getter for field "x".
     *
     * @return the value of field "x",in other words, the horizontal coordinate of a piece
     */
    public int getX() {
        return this.x;
    }

    /**
     * The method is a getter for field "y".
     *
     * @return the value of field "y", in other words, the vertical coordinate of a piece
     */
    public int getY() {
        return this.y;
    }

    /**
     * The method converts the coordinates of a piece to string representation.
     *
     * @return a string with the coordinates of a piece
     */
    public String toString() {
        return (this.x + " " + this.y);
    }

}

/**
 * ChessPiece abstract class contains methods for counting possible steps and captures.
 * It is also a parent to Knight, King, Pawn, Bishop, Rook and Queen
 */
abstract class ChessPiece {
    /**
     * field with position of a chess piece.
     */
    protected PiecePosition position;
    /**
     * field with color of a chess piece.
     */
    protected PieceColor color;

    /**
     * the method receives position and color of a piece and assigns them to "position" and "color" protected fields.
     *
     * @param piecePosition position of a piece, which consists of horizontal and vertical coordinates
     * @param pieceColor    color of a piece, which is either white, or black
     */
    ChessPiece(PiecePosition piecePosition, PieceColor pieceColor) {
        this.position = piecePosition;
        this.color = pieceColor;
    }

    /**
     * The method is a getter for field "position".
     *
     * @return the value of field "position", the coordinates of a piece
     */
    public PiecePosition getPosition() {
        return this.position;
    }

    /**
     * The method is a getter for field "color".
     *
     * @return the value of field "color", either white, or black
     */
    public PieceColor getColor() {
        return this.color;
    }

    /**
     * Abstract method for counting the possible number of moves from the current position of a piece.
     *
     * @param positions map with taken board positions in string representation as key and ChessPiece as value
     * @param boardSize chess board size,which is represented as the number of squares vertically and horizontally
     * @return an integer value, the number of all possible moves from the current position
     */
    public abstract int getMovesCount(Map<String, ChessPiece> positions, int boardSize);

    /**
     * Abstract method for counting the possible number of pieces that can be eaten by the given one.
     *
     * @param positions map with taken board positions in string representation as key and ChessPiece as value
     * @param boardSize chess board size,which is represented as the number of squares vertically and horizontally
     * @return an integer value, the number of all possible captures from the current position
     */
    public abstract int getCapturesCount(Map<String, ChessPiece> positions, int boardSize);
}

/**
 * Class which counts potential moves and captures of the given knight.
 * It is a child of ChessPiece
 */
class Knight extends ChessPiece {
    /**
     * Method which inherits position and color fields from ChessPiece, in this case they belong to knight.
     *
     * @param position the coordinates of the current knight
     * @param color    the color of the current knight
     */
    Knight(PiecePosition position, PieceColor color) {
        super(position, color);
    }

    /**
     * The method counts all possible moves by the knight.
     * It processes all positions on which the knight can step and counts legal ones
     *
     * @param positions map with taken board positions in string representation as key and ChessPiece as value
     * @param boardSize chess board size,which is represented as the number of squares vertically and horizontally
     * @return integer value of all possible moves from this position
     */
    @Override
    public int getMovesCount(Map<String, ChessPiece> positions, int boardSize) {
        int countOfSteps = 0;
        String newPosition1;
        final int minMove = -2;
        final int maxMove = 2;
        for (int i = minMove; i <= maxMove; i++) {
            for (int j = minMove; j <= maxMove; j++) {
                if (Math.abs(i) != Math.abs(j) && i != 0 && j != 0) {
                    if (this.position.getX() + i >= 1 && this.position.getX() + i <= boardSize
                            && this.position.getY() + j >= 1 && this.position.getY() + j <= boardSize) {
                        newPosition1 = (this.position.getX() + i) + " " + (this.position.getY() + j);
                        if (!positions.containsKey(newPosition1)
                                || positions.get(newPosition1).getColor() != this.color) {
                            countOfSteps += 1;
                        }
                    }
                }
            }
        }
        return countOfSteps;
    }

    /**
     * The method counts all possible captures by the knight.
     * It processes all possible squares the current knight can step on
     * And counts the number of all pieces that can be eaten by the knight
     *
     * @param positions map with taken board positions in string representation as key and ChessPiece as value
     * @param boardSize chess board size,which is represented as the number of squares vertically and horizontally
     * @return integer value of all possible captures from this position
     */
    @Override
    public int getCapturesCount(Map<String, ChessPiece> positions, int boardSize) {
        int countOfCaptures = 0;
        String newPosition1;
        final int minMove = -2;
        final int maxMove = 2;
        for (int i = minMove; i <= maxMove; i++) {
            for (int j = minMove; j <= maxMove; j++) {
                if (Math.abs(i) != Math.abs(j) && i != 0 && j != 0) {
                    if (this.position.getX() + i >= 1 && this.position.getX() + i <= boardSize
                            && this.position.getY() + j >= 1 && this.position.getY() + j <= boardSize) {
                        newPosition1 = (this.position.getX() + i) + " " + (this.position.getY() + j);
                        if (positions.containsKey(newPosition1)
                                && positions.get(newPosition1).getColor() != this.color) {
                            countOfCaptures += 1;
                        }
                    }
                }
            }
        }
        return countOfCaptures;
    }
}

/**
 * Class which counts potential moves and captures of the given king.
 * It is a child of ChessPiece
 */
class King extends ChessPiece {
    /**
     * Method which inherits position and color fields from ChessPiece, in this case they belong to king.
     *
     * @param position the coordinates of the current king
     * @param color    the color of the current king
     */
    King(PiecePosition position, PieceColor color) {
        super(position, color);
    }

    /**
     * The method counts all possible moves by the king.
     * It processes all positions on which the king can step and counts legal ones
     *
     * @param positions map with taken board positions in string representation as key and ChessPiece as value
     * @param boardSize chess board size,which is represented as the number of squares vertically and horizontally
     * @return integer value of all possible moves from this position
     */
    @Override
    public int getMovesCount(Map<String, ChessPiece> positions, int boardSize) {
        int countOfSteps = 0;
        String newPosition1;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (this.position.getX() + i <= boardSize && this.position.getX() + i >= 1
                        && this.position.getY() + j <= boardSize && this.position.getY() + j >= 1) {
                    newPosition1 = (this.position.getX() + i) + " " + (this.position.getY() + j);
                    if ((!positions.containsKey(newPosition1))
                            || (positions.get(newPosition1).getColor() != this.color)) {
                        countOfSteps += 1;
                    }
                }
            }
        }
        return countOfSteps;
    }

    /**
     * The method counts all possible captures by the king.
     * It processes all possible squares the current king can step on
     * And counts the number of all pieces that can be eaten by the king
     *
     * @param positions map with taken board positions in string representation as key and ChessPiece as value
     * @param boardSize chess board size,which is represented as the number of squares vertically and horizontally
     * @return integer value of all possible captures from this position
     */
    @Override
    public int getCapturesCount(Map<String, ChessPiece> positions, int boardSize) {
        int countOfCaptures = 0;
        String newPosition1;
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (this.position.getX() + i <= boardSize && this.position.getX() + i >= 1
                        && this.position.getY() + j <= boardSize && this.position.getY() >= 1) {
                    newPosition1 = (this.position.getX() + i) + " " + (this.position.getY() + j);
                    if (positions.containsKey(newPosition1) && (positions.get(newPosition1).getColor() != this.color)) {
                        countOfCaptures += 1;
                    }
                }
            }
        }
        return countOfCaptures;
    }
}

/**
 * Class which counts potential moves and captures of the given pawn.
 * It is a child of ChessPiece
 */
class Pawn extends ChessPiece {
    /**
     * Method which inherits position and color fields from ChessPiece, in this case they belong to pawn.
     *
     * @param position the coordinates of the current pawn
     * @param color    the color of the current color
     */
    Pawn(PiecePosition position, PieceColor color) {
        super(position, color);
    }

    /**
     * The method counts all possible moves by the pawn.
     * It processes all positions on which the pawn can step and counts legal ones
     *
     * @param positions map with taken board positions in string representation as key and ChessPiece as value
     * @param boardSize chess board size,which is represented as the number of squares vertically and horizontally
     * @return integer value of all possible moves from this position
     */
    @Override
    public int getMovesCount(Map<String, ChessPiece> positions, int boardSize) {
        int countOfSteps = 0;
        String newPosition1;
        int colorForSteps = 0;
        if (this.color == PieceColor.WHITE) {
            colorForSteps = 1;
        } else if (this.color == PieceColor.BLACK) {
            colorForSteps = -1;
        }
        if (this.position.getY() + colorForSteps <= boardSize && this.position.getY() + colorForSteps >= 1) {
            newPosition1 = (this.position.getX()) + " " + (this.position.getY() + colorForSteps);
            if (!positions.containsKey(newPosition1)) {
                countOfSteps += 1;
            }
        }
        if (this.position.getX() - colorForSteps >= 1 && this.position.getX() - colorForSteps <= boardSize
                && this.position.getY() + colorForSteps <= boardSize && this.position.getY() + colorForSteps >= 1) {
            newPosition1 = (this.position.getX() - colorForSteps) + " " + (this.position.getY() + colorForSteps);
            if (positions.containsKey(newPosition1) && positions.get(newPosition1).getColor() != this.color) {
                countOfSteps += 1;
            }
        }
        if (this.position.getX() + colorForSteps >= 1 && this.position.getX() + colorForSteps <= boardSize
                && this.position.getY() + colorForSteps >= 1 && this.position.getY() + colorForSteps <= boardSize) {
            newPosition1 = (this.position.getX() + colorForSteps) + " " + (this.position.getY() + colorForSteps);
            if (positions.containsKey(newPosition1) && positions.get(newPosition1).getColor() != this.color) {
                countOfSteps += 1;
            }
        }
        return countOfSteps;
    }

    /**
     * The method counts all possible captures by the pawn.
     * It processes all possible squares the current pawn can step on
     * And counts the number of all pieces that can be eaten by the pawn
     *
     * @param positions map with taken board positions in string representation as key and ChessPiece as value
     * @param boardSize chess board size,which is represented as the number of squares vertically and horizontally
     * @return integer value of all possible captures from this position
     */
    @Override
    public int getCapturesCount(Map<String, ChessPiece> positions, int boardSize) {
        int countOfCaptures = 0;
        String newPosition2;
        int colorForCaptures = 0;
        if (this.color == PieceColor.WHITE) {
            colorForCaptures = -1;
        } else if (this.color == PieceColor.BLACK) {
            colorForCaptures = 1;
        }
        if (this.position.getX() + colorForCaptures >= 1 && this.position.getX() + colorForCaptures <= boardSize
                && this.position.getY() - colorForCaptures <= boardSize
                && this.position.getY() - colorForCaptures >= 1) {
            newPosition2 = (this.position.getX() + colorForCaptures) + " " + (this.position.getY() - colorForCaptures);
            if (positions.containsKey(newPosition2) && positions.get(newPosition2).getColor() != this.color) {
                countOfCaptures += 1;
            }
        }
        if (this.position.getX() - colorForCaptures >= 1 && this.position.getX() - colorForCaptures <= boardSize
                && this.position.getY() - colorForCaptures <= boardSize
                && this.position.getY() - colorForCaptures >= 1) {
            newPosition2 = (this.position.getX() - colorForCaptures) + " " + (this.position.getY() - colorForCaptures);
            if (positions.containsKey(newPosition2) && positions.get(newPosition2).getColor() != this.color) {
                countOfCaptures += 1;
            }
        }
        return countOfCaptures;
    }
}

/**
 * Class which counts potential moves and captures of the given bishop.
 * It is a child of ChessPiece
 */
class Bishop extends ChessPiece implements BishopMovement {
    /**
     * Method which inherits position and color fields from ChessPiece, in this case they belong to bishop.
     *
     * @param position the coordinates of the current bishop
     * @param color    the color of the current bishop
     */
    Bishop(PiecePosition position, PieceColor color) {
        super(position, color);
    }

    /**
     * The method counts all possible moves by the bishop.
     * It processes all positions on which the bishop can step and counts legal ones
     *
     * @param positions map with taken board positions in string representation as key and ChessPiece as value
     * @param boardSize chess board size,which is represented as the number of squares vertically and horizontally
     * @return integer value of all possible moves from this position
     */
    @Override
    public int getMovesCount(Map<String, ChessPiece> positions, int boardSize) {
        return getDiagonalMovesCount(this.position, this.color, positions, boardSize);
    }

    /**
     * The method counts all possible captures by the bishop.
     * It processes all possible squares the current bishop can step on
     * And counts the number of all pieces that can be eaten by the bishop
     *
     * @param positions map with taken board positions in string representation as key and ChessPiece as value
     * @param boardSize chess board size,which is represented as the number of squares vertically and horizontally
     * @return integer value of all possible captures from this position
     */
    @Override
    public int getCapturesCount(Map<String, ChessPiece> positions, int boardSize) {
        return getDiagonalCapturesCount(this.position, this.color, positions, boardSize);
    }
}

/**
 * Class which counts potential moves and captures of the given rook.
 * It is a child of ChessPiece
 */
class Rook extends ChessPiece implements RookMovement {
    /**
     * Method which inherits position and color fields from ChessPiece, in this case they belong to rook.
     *
     * @param position the coordinates of the current rook
     * @param color    the color of the current rook
     */
    Rook(PiecePosition position, PieceColor color) {
        super(position, color);
    }

    /**
     * The method counts all possible moves by the rook.
     * It processes all positions on which the rook can step and counts legal ones
     *
     * @param positions map with taken board positions in string representation as key and ChessPiece as value
     * @param boardSize chess board size,which is represented as the number of squares vertically and horizontally
     * @return integer value of all possible moves from this position
     */
    @Override
    public int getMovesCount(Map<String, ChessPiece> positions, int boardSize) {
        return getOrthogonalMovesCount(this.position, this.color, positions, boardSize);
    }

    /**
     * The method counts all possible captures by the rook.
     * It processes all possible squares the current rook can step on
     * And counts the number of all pieces that can be eaten by the rook
     *
     * @param positions map with taken board positions in string representation as key and ChessPiece as value
     * @param boardSize chess board size,which is represented as the number of squares vertically and horizontally
     * @return integer value of all possible captures from this position
     */
    @Override
    public int getCapturesCount(Map<String, ChessPiece> positions, int boardSize) {
        return getOrthogonalCapturesCount(this.position, this.color, positions, boardSize);
    }
}

/**
 * Class which counts potential moves and captures of the given queen.
 * It is a child of ChessPiece
 */
class Queen extends ChessPiece implements RookMovement, BishopMovement {
    /**
     * Method which inherits position and color fields from ChessPiece, in this case they belong to queen.
     *
     * @param position the coordinates of the current queen
     * @param color    the color of the current queen
     */
    Queen(PiecePosition position, PieceColor color) {
        super(position, color);
    }

    /**
     * The method counts all possible moves by the queen.
     * It processes all positions on which the queen can step and counts legal ones
     *
     * @param positions map with taken board positions in string representation as key and ChessPiece as value
     * @param boardSize chess board size,which is represented as the number of squares vertically and horizontally
     * @return integer value of all possible moves from this position
     */
    @Override
    public int getMovesCount(Map<String, ChessPiece> positions, int boardSize) {
        return getDiagonalMovesCount(this.position, this.color, positions, boardSize)
                + getOrthogonalMovesCount(this.position, this.color, positions, boardSize);
    }

    /**
     * The method counts all possible captures by the queen.
     * It processes all possible squares the current queen can step on
     * And counts the number of all pieces that can be eaten by the queen
     *
     * @param positions map with taken board positions in string representation as key and ChessPiece as value
     * @param boardSize chess board size,which is represented as the number of squares vertically and horizontally
     * @return integer value of all possible captures from this position
     */
    @Override
    public int getCapturesCount(Map<String, ChessPiece> positions, int boardSize) {
        return getDiagonalCapturesCount(this.position, this.color, positions, boardSize)
                + getOrthogonalCapturesCount(this.position, this.color, positions, boardSize);
    }
}

/**
 * Interface which counts potential diagonal moves and captures from the piece position.
 */
interface BishopMovement {
    /**
     * The method counts all possible moves by the piece diagonally.
     * It processes all positions on which the piece can step and counts legal ones
     *
     * @param position  the coordinates of the current piece
     * @param color     the color of the current piece
     * @param positions map with taken board positions in string representation as key and ChessPiece as value
     * @param boardSize chess board size,which is represented as the number of squares vertically and horizontally
     * @return integer value of all possible moves from this position
     */
    default int getDiagonalMovesCount(PiecePosition position, PieceColor color,
                                      Map<String, ChessPiece> positions, int boardSize) {
        int countOfSteps = 0;
        int limit1 = Math.min(boardSize - position.getX(), boardSize - position.getY());
        int limit2 = Math.min(boardSize - position.getX(), position.getY() - 1);
        int limit3 = Math.min(position.getX() - 1, position.getY() - 1);
        int limit4 = Math.min(boardSize - position.getY(), position.getX() - 1);
        int limitOfSteps = Math.max(limit1, limit2);
        limitOfSteps = Math.max(limitOfSteps, limit3);
        limitOfSteps = Math.max(limitOfSteps, limit4);
        boolean flag1 = false;
        boolean flag2 = false;
        boolean flag3 = false;
        boolean flag4 = false;

        String newPosition1;
        for (int i = 1; i <= limitOfSteps; i++) {
            if (!flag1 && position.getX() - i >= 1 && position.getY() - i >= 1) {
                newPosition1 = (position.getX() - i) + " " + (position.getY() - i);
                if (positions.containsKey(newPosition1)) {
                    if (positions.get(newPosition1).getColor() != color) {
                        countOfSteps += 1;
                    }
                    flag1 = true;
                } else {
                    countOfSteps += 1;
                }
            } else {
                flag1 = true;
            }

            if (!flag2 && position.getX() - i >= 1 && position.getY() + i <= boardSize) {
                newPosition1 = (position.getX() - i) + " " + (position.getY() + i);
                if (positions.containsKey(newPosition1)) {
                    if (positions.get(newPosition1).getColor() != color) {
                        countOfSteps += 1;
                    }
                    flag2 = true;
                } else {
                    countOfSteps += 1;
                }
            } else {
                flag2 = true;
            }

            if (!flag3 && position.getX() + i <= boardSize && position.getY() + i <= boardSize) {
                newPosition1 = (position.getX() + i) + " " + (position.getY() + i);
                if (positions.containsKey(newPosition1)) {
                    if (positions.get(newPosition1).getColor() != color) {
                        countOfSteps += 1;
                    }
                    flag3 = true;
                } else {
                    countOfSteps += 1;
                }
            } else {
                flag3 = true;
            }

            if (!flag4 && position.getX() + i <= boardSize && position.getY() - i >= 1) {
                newPosition1 = (position.getX() + i) + " " + (position.getY() - i);
                if (positions.containsKey(newPosition1)) {
                    if (positions.get(newPosition1).getColor() != color) {
                        countOfSteps += 1;
                    }
                    flag4 = true;
                } else {
                    countOfSteps += 1;
                }
            } else {
                flag4 = true;
            }
        }
        return countOfSteps;
    }

    /**
     * The method counts all possible captures by moving diagonally.
     * It processes all possible squares the current piece can step on
     * And counts the number of all pieces that can be eaten by the piece
     *
     * @param position  the coordinates of the current piece
     * @param color     the color of the current piece
     * @param positions map with taken board positions in string representation as key and ChessPiece as value
     * @param boardSize chess board size,which is represented as the number of squares vertically and horizontally
     * @return integer value of all possible captures from this position
     */
    default int getDiagonalCapturesCount(PiecePosition position, PieceColor color,
                                         Map<String, ChessPiece> positions, int boardSize) {
        int countOfCaptures = 0;
        int limit1 = Math.min(boardSize - position.getX(), boardSize - position.getY());
        int limit2 = Math.min(boardSize - position.getX(), position.getY() - 1);
        int limit3 = Math.min(position.getX() - 1, position.getY() - 1);
        int limit4 = Math.min(boardSize - position.getY(), position.getX() - 1);
        int limitOfSteps = Math.max(limit1, limit2);
        limitOfSteps = Math.max(limitOfSteps, limit3);
        limitOfSteps = Math.max(limitOfSteps, limit4);
        boolean flag1 = false;
        boolean flag2 = false;
        boolean flag3 = false;
        boolean flag4 = false;

        String newPosition1;
        for (int i = 1; i <= limitOfSteps; i++) {
            if (!flag1 && position.getX() - i >= 1 && position.getY() - i >= 1) {
                newPosition1 = (position.getX() - i) + " " + (position.getY() - i);
                if (positions.containsKey(newPosition1)) {
                    if (positions.get(newPosition1).getColor() != color) {
                        countOfCaptures += 1;
                    }
                    flag1 = true;
                }
            } else {
                flag1 = true;
            }

            if (!flag2 && position.getX() - i >= 1 && position.getY() + i <= boardSize) {
                newPosition1 = (position.getX() - i) + " " + (position.getY() + i);
                if (positions.containsKey(newPosition1)) {
                    if (positions.get(newPosition1).getColor() != color) {
                        countOfCaptures += 1;
                    }
                    flag2 = true;
                }
            } else {
                flag2 = true;
            }

            if (!flag3 && position.getX() + i <= boardSize && position.getY() + i <= boardSize) {
                newPosition1 = (position.getX() + i) + " " + (position.getY() + i);
                if (positions.containsKey(newPosition1)) {
                    if (positions.get(newPosition1).getColor() != color) {
                        countOfCaptures += 1;
                    }
                    flag3 = true;
                }
            } else {
                flag3 = true;
            }

            if (!flag4 && position.getX() + i <= boardSize && position.getY() - i >= 1) {
                newPosition1 = (position.getX() + i) + " " + (position.getY() - i);
                if (positions.containsKey(newPosition1)) {
                    if (positions.get(newPosition1).getColor() != color) {
                        countOfCaptures += 1;
                    }
                    flag4 = true;
                }
            } else {
                flag4 = true;
            }
        }
        return countOfCaptures;
    }
}

/**
 * Interface which counts potential orthogonal (up, down, left, right) moves and captures from the piece position.
 */
interface RookMovement {
    /**
     * The method counts all possible moves up, down, left and right.
     * It processes all orthogonal positions on which a piece can step and counts legal ones
     *
     * @param position  the coordinates of the current piece
     * @param color     the color of the current piece
     * @param positions map with taken board positions in string representation as key and ChessPiece as value
     * @param boardSize chess board size,which is represented as the number of squares vertically and horizontally
     * @return integer value of all possible moves from this position
     */
    default int getOrthogonalMovesCount(PiecePosition position, PieceColor color,
                                        Map<String, ChessPiece> positions, int boardSize) {
        int sizeOfSteps = 0;
        String newPosition1;
        int limit = Math.max(position.getX() - 1, position.getY() - 1);
        limit = Math.max(limit, boardSize - position.getX());
        limit = Math.max(limit, boardSize - position.getY());
        boolean flag1 = false;
        boolean flag2 = false;
        boolean flag3 = false;
        boolean flag4 = false;

        for (int i = 1; i <= limit; i++) {
            if (!flag1 && (position.getY() + i <= boardSize)) {
                newPosition1 = (position.getX()) + " " + (position.getY() + i);
                if (positions.containsKey(newPosition1)) {
                    if (positions.get(newPosition1).getColor() != color) {
                        sizeOfSteps += 1;
                    }
                    flag1 = true;
                } else {
                    sizeOfSteps += 1;
                }
            } else {
                flag1 = true;
            }
            if (!flag2 && (position.getY() - i >= 1)) {
                newPosition1 = (position.getX()) + " " + (position.getY() - i);
                if (positions.containsKey(newPosition1)) {
                    if (positions.get(newPosition1).getColor() != color) {
                        sizeOfSteps += 1;
                    }
                    flag2 = true;
                } else {
                    sizeOfSteps += 1;
                }
            } else {
                flag2 = true;
            }
            if (!flag3 && (position.getX() + i <= boardSize)) {
                newPosition1 = (position.getX() + i) + " " + (position.getY());
                if (positions.containsKey(newPosition1)) {
                    if (positions.get(newPosition1).getColor() != color) {
                        sizeOfSteps += 1;
                    }
                    flag3 = true;
                } else {
                    sizeOfSteps += 1;
                }
            } else {
                flag3 = true;
            }
            if (!flag4 && (position.getX() - i >= 1)) {
                newPosition1 = (position.getX() - i) + " " + (position.getY());
                if (positions.containsKey(newPosition1)) {
                    if (positions.get(newPosition1).getColor() != color) {
                        sizeOfSteps += 1;
                    }
                    flag4 = true;
                } else {
                    sizeOfSteps += 1;
                }
            } else {
                flag4 = true;
            }
        }
        return sizeOfSteps;
    }

    /**
     * The method counts all possible captures by moving up, down, left and right.
     * It processes all possible squares the current piece can step on orthogonally
     * And counts the number of all pieces that can be eaten by the piece
     *
     * @param position  the coordinates of the current piece
     * @param color     the color of the current piece
     * @param positions map with taken board positions in string representation as key and ChessPiece as value
     * @param boardSize chess board size,which is represented as the number of squares vertically and horizontally
     * @return integer value of all possible captures from this position
     */
    default int getOrthogonalCapturesCount(PiecePosition position, PieceColor color,
                                           Map<String, ChessPiece> positions, int boardSize) {
        int sizeOfCaptures = 0;
        String newPosition1;
        int limit = Math.max(position.getX() - 1, position.getY() - 1);
        limit = Math.max(limit, boardSize - position.getX());
        limit = Math.max(limit, boardSize - position.getY());
        boolean flag1 = false;
        boolean flag2 = false;
        boolean flag3 = false;
        boolean flag4 = false;

        for (int i = 1; i <= limit; i++) {
            if (!flag1 && (position.getY() + i <= boardSize)) {
                newPosition1 = (position.getX()) + " " + (position.getY() + i);
                if (positions.containsKey(newPosition1)) {
                    if (positions.get(newPosition1).getColor() != color) {
                        sizeOfCaptures += 1;
                    }
                    flag1 = true;
                }
            } else {
                flag1 = true;
            }
            if (!flag2 && (position.getY() - i >= 1)) {
                newPosition1 = (position.getX()) + " " + (position.getY() - i);
                if (positions.containsKey(newPosition1)) {
                    if (positions.get(newPosition1).getColor() != color) {
                        sizeOfCaptures += 1;
                    }
                    flag2 = true;
                }
            } else {
                flag2 = true;
            }
            if (!flag3 && (position.getX() + i <= boardSize)) {
                newPosition1 = (position.getX() + i) + " " + (position.getY());
                if (positions.containsKey(newPosition1)) {
                    if (positions.get(newPosition1).getColor() != color) {
                        sizeOfCaptures += 1;
                    }
                    flag3 = true;
                }
            } else {
                flag3 = true;
            }
            if (!flag4 && (position.getX() - i >= 1)) {
                newPosition1 = (position.getX() - i) + " " + (position.getY());
                if (positions.containsKey(newPosition1)) {
                    if (positions.get(newPosition1).getColor() != color) {
                        sizeOfCaptures += 1;
                    }
                    flag4 = true;
                }
            } else {
                flag4 = true;
            }
        }
        return sizeOfCaptures;
    }
}

/**
 * PieceColor enum contains types of chess pieces possible colors and "parse" method.
 */
enum PieceColor {
    /**
     * WHITE means that the given chess piece is white.
     */
    WHITE,
    /**
     * BLACK means that the given chess piece is black.
     */
    BLACK;

    /**
     * In "parse" we get a string with the color of the given chess piece and switch it on PieceColor enum.
     * If the color is neither BLACK, nor WHITE, then we return an exception
     *
     * @param stepanovIsTheBestTA string with the color of the current chess piece
     * @return the result of our input color switch on PieceColor enum or an exception
     * @throws InvalidPieceColorException exception which means that the color of current piece is invalid
     */
    public static PieceColor parse(String stepanovIsTheBestTA) throws InvalidPieceColorException {
        switch (stepanovIsTheBestTA) {
            case "White":
                return WHITE;
            case "Black":
                return BLACK;
            default:
                throw new InvalidPieceColorException();
        }
    }
}
