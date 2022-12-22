package chess;

import java.util.Scanner;

public class Chess {

    public static Piece[][] board;

    public Chess() {

    }

    public static void main(String[] args) {
        board= init_board();
        board[4][4]= new Piece("White", 4, 4, "Bishop");

        Scanner s= new Scanner(System.in);
        System.out.println("Enter your positions: ");
        Integer x= s.nextInt();
        Integer y= s.nextInt();
        Integer z= s.nextInt();
        Integer a= s.nextInt();
        System.out.println(bishopValid(board, x, y, z, a));

    }

    public static void display_board(Piece[][] b) {
        for (int i= 0; i < b.length; i++ ) {

            for (Piece p : b[i]) {

                if (p != null) {
                    System.out.println(p.get_color() + p.get_name());
                } else {
                    System.out.println("Blank");
                }
            }
        }
    }

    public static boolean isOccupied(Piece[][] b, int x, int y) {
        return b[x][y] != null;
    }

    public boolean isInBounds(Piece[][] b, int x, int y) {
        return x >= 0 && x <= b.length - 1 && y >= 0 && y <= b.length - 1;
    }

    public static boolean pawnValid(Piece[][] b, int xi, int yi, int xf, int yf) {
        // Precondition: All coordinates are in bounds. Don't handle checks for right now

        Piece p= b[xi][yi];
        int xdiff= xf - xi;
        int ydiff= yf - yi;
        if (isOccupied(b, xf, yf)) {
            Piece q= b[xf][yf];
            if (p.get_color() == "White" && q.get_color() == "Black") {
                return (ydiff == -2 || ydiff == 2) && xdiff == 1;
            } else if (p.get_color() == "Black" && q.get_color() == "White") {
                return (ydiff == -2 || ydiff == 2) && xdiff == -1;
            }

            return false;
        } else {
            if (p.get_color() == "White") {
                System.out.println("xdiff: " + xdiff);
                System.out.println("ydiff: " + ydiff);
                if (xi == 1) {
                    return (xdiff == 2 || xdiff == 1) && ydiff == 0;
                } else {
                    return xdiff == 1 && ydiff == 0;
                }
            } else if (p.get_color() == "Black") {
                if (xi == 6) {
                    return (xdiff == -2 || xdiff == -1) && ydiff == 0;
                } else {
                    return xdiff == -1 && ydiff == 0;
                }
            }
            return false;
        }
    }

    public static boolean bishopValid(Piece[][] b, int xi, int yi, int xf, int yf) {
        int slope= (yf - yi) / (xf - xi);
        if (slope != 1 && slope != -1) return false;

        int xptr= xi + 1;
        int yptr= yi + 1;
        while (xptr < xf && yptr < yf) {
            if (b[xptr][yptr] != null) return false;
            xptr++ ;
            yptr++ ;
        }
        if (isOccupied(b, xf, yf)) { return b[xf][yf].get_color() != b[xi][yi].get_color(); }
        return true;

    }

    public boolean isValidMove(Piece[][] b, int xi, int yi, int xf, int yf) {

        return true;
    }

    public static Piece[][] init_board() {

        String[] back_pieces= { "Rook", "Knight", "Bishop", "King", "Queen",
                "Bishop", "Knight", "Rook"
        };

        Piece[] rowA= new Piece[8];
        Piece[] rowH= new Piece[8];

        // build the two ends of the board
        for (int i= 0; i < back_pieces.length; i++ ) {
            rowA[i]= new Piece("White", i, 0, back_pieces[i]);
            rowH[i]= new Piece("Black", i, 7,
                back_pieces[back_pieces.length - i - 1]);
        }

        // build the two initial rows of pawns
        Piece[] rowB= new Piece[8];
        Piece[] rowG= new Piece[8];
        for (int i= 0; i < 8; i++ ) {
            rowB[i]= new Piece("White", i, 1, "Pawn");
            rowG[i]= new Piece("Black", i, 6, "Pawn");
        }

        // build a middle row of initially empty squares
        Piece[] middle= new Piece[8];

        Piece[][] res= { rowA, rowB, middle,
                middle, middle, middle, rowG, rowH };
        return res;
    }

}

class Piece {

    private String color;

    // What subarray is the piece within board?
    private int xpos;

    // position of piece in subarray
    private int ypos;
    private String name;

    public Piece(String c, int x, int y, String nm) {
        color= c;
        xpos= x;
        ypos= y;
        name= nm;
    }

    public String get_color() {
        return color;
    }

    public int get_x() {
        return xpos;
    }

    public void set_x(int x) {
        xpos= x;
    }

    public int get_y() {
        return ypos;
    }

    public void set_y(int y) {
        ypos= y;
    }

    public String get_name() {
        return name;
    }

}
