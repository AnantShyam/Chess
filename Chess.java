package chess;

public class Chess {

    public static Piece[][] board;

    public Chess() {

    }

    public static void main(String[] args) {
        board= init_board();
        System.out.println("debugging");
    }

    public static void display_board(Piece[][] b) {
        for (int i= 0; i < b.length; i++ ) {

            for (int j= 0; j < b[i].length; j++ ) {

                Piece p= b[i][j];

                if (p != null) {
                    System.out.println(i + " , " + j);
                    System.out.println(p.full_name());
                } else {
                    System.out.println("Blank");
                }
            }
        }
    }

    public static boolean isOccupied(Piece[][] b, int x, int y) {
        return b[x][y] != null;
    }

    public static boolean isInBounds(Piece[][] b, int x, int y) {
        return x >= 0 && x <= b.length - 1 && y >= 0 && y <= b.length - 1;
    }

    public static boolean pawnValid(Piece[][] b, int xi, int yi, int xf, int yf) {
        // Precondition: All coordinates are in bounds. Don't handle checks for right now
        if (xf == xi && yi == yf) return false;
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
        if (xi == xf || yi == yf) return false;
        int deltx= xf - xi;
        int delty= yf - yi;
        int slope= delty / deltx;
        if (slope != 1 && slope != -1) return false;

        int xptr= xi + 1;
        int yptr= yi + 1;

        if (deltx > 0 && delty > 0) {
            while (xptr < xf && yptr < yf) {
                if (b[xptr][yptr] != null) return false;
                xptr++ ;
                yptr++ ;
            }
        } else if (deltx < 0 && delty < 0) {
            xptr= xi - 1;
            yptr= yi - 1;
            while (xptr > xf && yptr > yf) {
                if (b[xptr][yptr] != null) return false;
                xptr-- ;
                yptr-- ;
            }
        } else if (deltx > 0 && delty < 0) {
            yptr= yi - 1;
            while (xptr < xf && yptr > yf) {
                if (b[xptr][yptr] != null) return false;
                xptr++ ;
                yptr-- ;
            }
        } else if (deltx < 0 && delty > 0) {
            xptr= xi - 1;
            while (xptr > xf && yptr < yf) {
                if (b[xptr][yptr] != null) return false;
                xptr-- ;
                yptr++ ;
            }
        }

        if (isOccupied(b, xf, yf)) { return b[xf][yf].get_color() != b[xi][yi].get_color(); }
        return true;

    }

    public static boolean rookValid(Piece[][] b, int xi, int yi, int xf, int yf) {

        if (yf - yi != 0 && xf - xi != 0 ||
            yf == yi && xf == xi) return false;

        if (xi == xf) {
            int yptr= yi + 1;
            if (yi < yf) {
                while (yptr < yf) {
                    if (b[xi][yptr] != null) return false;
                    yptr++ ;
                }
            } else if (yi > yf) {
                yptr= yi - 1;
                while (yptr > yf) {
                    if (b[xi][yptr] != null) return false;
                    yptr-- ;
                }
            }

        } else if (yi == yf) {
            int xptr= xi + 1;
            if (xf < xi) {
                xptr= xi - 1;
                while (xptr > xf) {
                    if (b[xptr][yi] != null) return false;
                    xptr-- ;
                }
            } else if (xf > xi) {
                while (xptr < xf) {
                    if (b[xptr][yi] != null) return false;
                    xptr++ ;
                }
            }
        }

        if (isOccupied(b, xf, yf)) { return b[xf][yf].get_color() != b[xi][yi].get_color(); }
        return true;

    }

    public static boolean knightValid(Piece[][] b, int xi, int yi, int xf, int yf) {
        int xdiff= Math.abs(xf - xi);
        int ydiff= Math.abs(yf - yi);
        if (xdiff == 2 && ydiff == 1 || xdiff == 1 && ydiff == 2) {
            if (isOccupied(b, xf, yf)) { return b[xf][yf].get_color() != b[xi][yi].get_color(); }
            return true;
        }
        return false;

    }

    public static boolean queenValid(Piece[][] b, int xi, int yi, int xf, int yf) {
        return bishopValid(b, xi, yi, xf, yf) || rookValid(b, xi, yi, xf, yf);
    }

    public static boolean kingValid(Piece[][] b, int xi, int yi, int xf, int yf) {

        int deltx= Math.abs(xf - xi);
        int delty= Math.abs(yf - yi);

        System.out.println("change in x : " + deltx);
        System.out.println("change in y : " + delty);

        if (deltx != 1 && delty != 1) return false;
        if (isOccupied(b, xf, yf)) {
            if (b[xf][yf].get_color() == b[xi][yi].get_color()) { return false; }
        }
        Piece p= b[xi][yi];

        System.out.println(p.full_name());
        b[xf][yf]= p;
        b[xi][yi]= null;
        return !inCheck(b, p.get_color());
    }

    public static boolean inCheck(Piece[][] b, String c) {
        // get position of king of color c

        int king_x= -1;
        int king_y= -1;

        for (int i= 0; i < b.length; i++ ) {
            for (int j= 0; j < b[i].length; j++ ) {
                Piece p= b[i][j];
                if (p != null && p.get_color() == c && p.get_name() == "King") {
                    king_x= i;
                    king_y= j;
                }
            }
        }
        // traverse through the entire board and check the valid moves from pieces
        // of opposite color
        for (int i= 0; i < b.length; i++ ) {
            for (int j= 0; j < b[i].length; j++ ) {
                Piece p= b[i][j];
                if (p != null && p.get_color() != c && p.get_name() != "King") {
                    if (isValidMove(b, i, j, king_x, king_y)) { return true; }
                }
            }
        }
        return false;
    }

    public static boolean isValidMove(Piece[][] b, int xi, int yi, int xf, int yf) {

        if (!isInBounds(b, xf, yf)) return false;

        Piece p= b[xi][yi];
        String name= p.get_name();

        if (name == "Pawn") {
            return pawnValid(b, xi, yi, xf, yf);
        } else if (name == "Knight") {
            return knightValid(b, xi, yi, xf, yf);
        } else if (name == "Bishop") {
            return bishopValid(b, xi, yi, xf, yf);
        } else if (name == "Queen") {
            return queenValid(b, xi, yi, xf, yf);
        } else if (name == "Rook") { return rookValid(b, xi, yi, xf, yf); }

        return false;

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

    public String full_name() {
        return color + " " + name;
    }
}
