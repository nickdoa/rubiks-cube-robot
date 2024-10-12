import java.util.*;

/***
 * This class is a custom data structure for handling the data of a 3x3 Rubik's cube, 
 * and representing configurations like "turning the cube" in this data structure.
 * 
 * This class will aid in the algorithm solving process of a Rubik's cube.
 */
public class Cube {

    /*
                WHITE
        RED     BLUE    ORANGE  GREEN
                YELLOW

        26 TOTAL PIECES
        6 CENTERS << these will be constant
        12 EDGES
        8 CORNERS

        how we will represent the cube

        >>>>CUBE_MAP<<<<

                    w0  w1  w2
                    w3  w   w4
                    w6  w7  w8                      
        r0  r1  r2  b0  b1  b2  o0  o1  o2  g0  g1  g2
        r3  r   r5  b3  b   b5  o3  o   o5  g3  g   g5
        r6  r7  r8  b6  b7  b8  o6  o7  o8  g6  g7  g8
                    y0  y1  y2
                    y3  y   y5
                    y6  y7  y8

        this acts like an unwrapped version of the 3D cube, like UV Unwrapping
        ...
        EXCEPT for the fact that GREEN is flipped then if it were like a paper cutout, bc/ the first column of GREEN
        will STILL correspond with the first column of WHITE.
    */

    public final int NUM_FACES = 6;
    public final int PIECES_PER_FACE = 9; // # of pieces on ONE face

    public enum Color{
        RED { @Override public String toString(){ return "R"; } },
        BLUE { @Override public String toString(){ return "B"; } },
        WHITE { @Override public String toString(){ return "W"; } },
        YELLOW { @Override public String toString(){ return "Y"; } },
        ORANGE { @Override public String toString(){ return "O"; } },
        GREEN { @Override public String toString(){ return "G"; } };

        public static Color fromInt(int idx){
            switch (idx){
                case 0: return RED;
                case 1: return BLUE;
                case 2: return WHITE;
                case 3: return YELLOW;
                case 4: return ORANGE;
                case 5: return GREEN;
                default: return null;
            }
        }

        public static Color fromString(String idx){
            switch (idx){
                case "R": return RED;
                case "B": return BLUE;
                case "W": return WHITE;
                case "Y": return YELLOW;
                case "O": return ORANGE;
                case "G": return GREEN;
                default: return null;
            }
        }
    }

    // the KEY will represent the side color aka the center color BC CENTER COLOR SHALL STAY CONSISTENT
    // the VALUE will be the pieces on this face according to the "CUBE_MAP"
    private Map<Color, Color[]> matrix;

    // make default cube
    public Cube(){
        toDefault();
    }

    public Cube(boolean scramble){
        this();
        if (scramble) this.scramble();
    }

    public Cube(int turns){
        this();
        if (turns <= 0) return;

        scramble(turns);
    }

    public Cube(Cube other){
        this.matrix.putAll(other.matrix);
    }

    public Cube(String sequence){
        this();
        this.scramble(sequence);
    }

    public Cube(String[] sequence){
        this();
        scramble(sequence);
    }

    // makes this cube the solved permutation
    public void toDefault(){
        matrix = makeDefault();
    }

    // converts this object into a solved cube
    private Map<Color, Color[]> makeDefault(){

        Map<Color, Color[]> cube = new HashMap<Color, Color[]>();

        Color[] colorOrder = {Color.RED, Color.BLUE, Color.WHITE, Color.YELLOW, Color.ORANGE, Color.GREEN};

        // make faces
        Color[] face; // buffer
        for (int i = 0; i < NUM_FACES; i++){
            face = fullColorFace(colorOrder[i]);
            cube.put(colorOrder[i], face);
        }

        return cube;
    }

    // make 3x3 color array of same color
    private Color[] fullColorFace(Color faceColor){

        Color[] face = new Color[PIECES_PER_FACE];

        for (int i = 0; i < PIECES_PER_FACE; i++){
            face[i] = faceColor;
        }

        return face;
    }

    // changes the cube map based on what face was turned
    // clockwise is considered looking at a face on the CUBE_MAP and rotating it clockwise
    public boolean turn(Color faceToturn, boolean clockwise){
        // i'm not smart enough to make code reusable for all 12 different turns

        faceturn(faceToturn, clockwise);
        switch (faceToturn){

            // CORNER SWAPPING = "alpha" and "beta" refer to the different sides of the corner, for r2 = "1:w6 & 2:b0", for r8 = "1:b6 & 2: y0"  
            case BLUE:
                // CORNERS
                ArraySwap.swapFour(matrix.get(Color.RED), 2, matrix.get(Color.WHITE), 8, matrix.get(Color.ORANGE), 6, matrix.get(Color.YELLOW), 0, clockwise); // alpha
                ArraySwap.swapFour(matrix.get(Color.WHITE), 6, matrix.get(Color.ORANGE), 0, matrix.get(Color.YELLOW), 2, matrix.get(Color.RED), 8, clockwise); // beta

                // EDGES
                ArraySwap.swapFour(matrix.get(Color.WHITE), 7, matrix.get(Color.ORANGE), 3, matrix.get(Color.YELLOW), 1, matrix.get(Color.RED), 5, clockwise);
                break;
            case GREEN:
                // CORNERS
                ArraySwap.swapFour(matrix.get(Color.ORANGE), 2, matrix.get(Color.WHITE), 0, matrix.get(Color.RED), 6, matrix.get(Color.YELLOW), 8, clockwise); // alpha
                ArraySwap.swapFour(matrix.get(Color.WHITE), 2, matrix.get(Color.RED), 0, matrix.get(Color.YELLOW), 6, matrix.get(Color.ORANGE), 8, clockwise); // beta

                // EDGES
                ArraySwap.swapFour(matrix.get(Color.WHITE), 1, matrix.get(Color.RED), 3, matrix.get(Color.YELLOW), 7, matrix.get(Color.ORANGE), 5, clockwise);
                break;
            case ORANGE:
                // CORNERS
                ArraySwap.swapFour(matrix.get(Color.BLUE), 2, matrix.get(Color.WHITE), 2, matrix.get(Color.GREEN), 6, matrix.get(Color.YELLOW), 2, clockwise); // alpha
                ArraySwap.swapFour(matrix.get(Color.WHITE), 8, matrix.get(Color.GREEN), 0, matrix.get(Color.YELLOW), 8, matrix.get(Color.BLUE), 8, clockwise); // beta

                // EDGES
                ArraySwap.swapFour(matrix.get(Color.WHITE), 5, matrix.get(Color.GREEN), 3, matrix.get(Color.YELLOW), 5, matrix.get(Color.BLUE), 5, clockwise);
                break;
            case RED:
                // CORNERS
                ArraySwap.swapFour(matrix.get(Color.GREEN), 2, matrix.get(Color.WHITE), 6, matrix.get(Color.BLUE), 6, matrix.get(Color.YELLOW), 6, clockwise); // alpha
                ArraySwap.swapFour(matrix.get(Color.WHITE), 0, matrix.get(Color.BLUE), 0, matrix.get(Color.YELLOW), 0, matrix.get(Color.GREEN), 8, clockwise); // beta

                // EDGES
                ArraySwap.swapFour(matrix.get(Color.WHITE), 3, matrix.get(Color.BLUE), 3, matrix.get(Color.YELLOW), 3, matrix.get(Color.GREEN), 5, clockwise);
                break;
            case WHITE:
                // CORNERS
                ArraySwap.swapFour(matrix.get(Color.RED), 0, matrix.get(Color.GREEN), 0, matrix.get(Color.ORANGE), 0, matrix.get(Color.BLUE), 0, clockwise); // alpha
                ArraySwap.swapFour(matrix.get(Color.GREEN), 2, matrix.get(Color.ORANGE), 2, matrix.get(Color.BLUE), 2, matrix.get(Color.RED), 2, clockwise); // beta

                // EDGES
                ArraySwap.swapFour(matrix.get(Color.GREEN), 1, matrix.get(Color.ORANGE), 1, matrix.get(Color.BLUE), 1, matrix.get(Color.RED), 1, clockwise);
                break;
            case YELLOW:
                // CORNERS
                ArraySwap.swapFour(matrix.get(Color.RED), 8, matrix.get(Color.BLUE), 8, matrix.get(Color.ORANGE), 8, matrix.get(Color.GREEN), 8, clockwise); // alpha
                ArraySwap.swapFour(matrix.get(Color.BLUE), 6, matrix.get(Color.ORANGE), 6, matrix.get(Color.GREEN), 6, matrix.get(Color.RED), 6, clockwise); // beta

                // EDGES
                ArraySwap.swapFour(matrix.get(Color.BLUE), 7, matrix.get(Color.ORANGE), 7, matrix.get(Color.GREEN), 7, matrix.get(Color.RED), 7, clockwise);
                break;

        }

        return true;
    }

    public boolean turn(String turn){

        // if this is NOT a valid turn string... return false
        if (turn.length() < 1 || turn.length() > 2) return false;
        if (Color.fromString(turn.substring(0, 1)) == null) return false;

        String turnType = turn.substring(1);

        if (!(turnType.equals("") || turnType.equals("\'") || turnType.equals("2"))) return false;

        // else... do the turn
        Color face = Color.fromString(turn.substring(0, 1));
        
        switch(turnType){
            case "": turn(face, true); break;
            case "\'": turn(face, false); break;
            case "2": turn(face, true); turn(face, true); break;
        }

        return true;
    }

    // turns just the face...
    private void faceturn(Color faceToturn, boolean clockwise){
        Color[] face = matrix.get(faceToturn);

        ArraySwap.swapFour(face, 0, 2, 8, 6, clockwise);
        ArraySwap.swapFour(face, 1, 5, 7, 3, clockwise);
    }

    // overload
    public boolean scramble(String sequence){
        return scramble(sequence.split(" ", -1));
    }
    // scrambles this cube based on the string array sequence of moves.  returns true or false if it was successful.
    public boolean scramble(String[] sequence){
        int length = sequence.length;

        /* 
            this for loop will purely check for preconditions
            it will check if this is a valid sequence
            so we now it's safe to start scrambling
        */
        for (int i = 0; i < length; i++){

            String curr = sequence[i];
            if (curr.length() < 1 || curr.length() > 2) return false;
            else if (curr.length() == 2){
                String type = curr.substring(1, 2);
                if (!type.equals("\'") && !type.equals("2")){
                    return false;
                }
            }
            Color face = Color.fromString(curr.substring(0, 1));
            if (face == null) return false;
        }

        // this loop will do the scrambling
        for (int i = 0; i < length; i++){

            String curr = sequence[i];
            Color face = Color.fromString(curr.substring(0, 1));

            switch (curr.substring(1)){
                case "":
                    turn(face, true);
                    break;
                case "\'":
                    turn(face, false);
                    break;
                case "2":
                    turn(face, true);
                    turn(face, true);
                    break;
            }
        }

        return true;
    }

    // scrambles this cube for a certain amount of turns
    public boolean scramble(int turns){
        return scramble(turns, turns);
    }

    public boolean scramble(int turns, boolean optimized){
        return scramble(turns, turns, optimized);
    }

    // default scramble for 5 to 20 moves
    public boolean scramble(){
        return scramble(5, 20);
    }

    public boolean scramble(boolean optimized){
        return scramble(5, 20, optimized);
    }

    public boolean scramble(int min, int max){
        return scramble(min, max, true);
    }
    
    /**
     * scrambles between min-max moves. 
     * @param boolean optimized
     * if true, the turns generated will not repeat tunring the same face as the previous move.
     * if false, duplicate face turns are not checked.
     */ 
    public boolean scramble(int min, int max, boolean optimized){

        // setup randoms
        int numTurns = (int)((max - min + 1) * Math.random()) + min;
        int turnFace = 0, turnType = 0;
        Color face = null;

        // scramble sequence
        String[] scrambleSeq = new String[numTurns];

        for (int t = 0; t < numTurns; t++){

            // setup random
            turnFace = (int)(6 * Math.random());
            turnType = (int)(3 * Math.random());
            face = Color.fromInt(turnFace);

            // if we're optimizing the turns...
            if (optimized){
                // if we have at least one turn && the previous move and this move have the same turn face...
                if (t > 0 && scrambleSeq[t-1].substring(0,1).equals(face.toString())){
                    // try doing this move again.
                    t--;
                    continue;
                }
            }

            String currTurn = "";
            scrambleSeq[t] = currTurn;

            scrambleSeq[t] += face.toString();

            switch (turnType){
                case 1:
                    scrambleSeq[t] += "\'";
                    break;
                case 2:
                    scrambleSeq[t] += "2";
                    break;
            }
        }

        System.out.println(Arrays.toString(scrambleSeq));
        return scramble(scrambleSeq);
    }

    // THE THING WE'VE ALL BEEN WAITING FOR.  This function gives a sequence of turns that would solve this cube.
    public String[] solve(){
        return null;
    }

    @Override public String toString(){
        return getCubeMap();
    }

    public boolean equals(Cube other){
        // check if the keyset is the same for both matrices AND the keyset is the enums of colors (it should, but just in case)
        if (!this.matrix.keySet().equals(other.matrix.keySet()) && Set.of(Color.values()).equals(this.matrix.keySet())) return false;

        // check if the values of both matrices are the same...
        for (Color c : Color.values()){
            if (!Arrays.equals(matrix.get(c), other.matrix.get(c))) return false;
        }

        return true;
    }

    public String getCubeMap(){
        // MAKES THE VISUAL CUBE MAP
        String msg = "";

        // white
        Color[] white = matrix.get(Color.WHITE);
        for (int w = 0; w < 3; w++){
            msg = msg + "\t" + white[3*w] + " " + white[3*w+1] + " " + white[3*w+2] + "\n";
        }

        // other colors
        Color[] red = matrix.get(Color.RED);
        Color[] blue = matrix.get(Color.BLUE);
        Color[] orange = matrix.get(Color.ORANGE);
        Color[] green = matrix.get(Color.GREEN);
        String redS = "", blueS = "", orangeS = "", greenS = "";
        for (int c = 0; c < 3; c++){

            redS = red[3*c].toString() + " " + red[3*c+1] + " " + red[3*c+2];
            blueS = blue[3*c].toString() + " " + blue[3*c+1] + " " + blue[3*c+2];
            orangeS = orange[3*c].toString() + " " + orange[3*c+1] + " " + orange[3*c+2];
            greenS = green[3*c].toString() + " " + green[3*c+1] + " " + green[3*c+2];
            
            msg = msg + redS + "\t" + blueS + "\t" + orangeS + "\t" + greenS + "\n";
        }

        // yellow
        Color[] yellow = matrix.get(Color.YELLOW);
        for (int y = 0; y < 3; y++){
            
            msg = msg + "\t" + yellow[3*y] + " " + yellow[3*y+1] + " " + yellow[3*y+2] + "\n";
        }

        return msg;
    }

}