import java.util.*;

public class CFOPAlg extends CubeAlgorithm {

    // list of F2L unorientated pairs on the yellow side
    /**
     * U11
     * 
     *   ^edge primary/secondary color on top
     * 
     *  ^edge location
     * 
     * ^
     * white face
     * 
     * primary color = alpha color if both colors are both dominant or recessive, beta color if one is dom and other recessive
     * secondary color = beta color if both colors are both dom or both recessive, alpha color if one is dom and other recessive
     * 
     * 
     */
    private enum Pair{
        U11,
        U12,
        U21,
        U22,
        U31,
        U32,
        U41,
        U42,
        B11,
        B12,
        B21,
        B22,
        B31,
        B32,
        B41,
        B42,
        L11,
        L12,
        L21,
        L22,
        L31,
        L32,
        L41,
        L42;

        public enum ColorType{
            PRIMARY {@Override public String toString() { return "PRIMARY"; }},
            SECONDARY {@Override public String toString() { return "SECONDARY"; }},
            NULL {@Override public String toString() { return "NULL"; }};

            public static ColorType getType(Cube instance, Piece p, Color c){

                if (c == Color.WHITE || c == Color.YELLOW) return ColorType.NULL;

                Color[] type = getPairType(instance, p);
                if (type == null) return null;

                boolean alphaIsPrimary = Color.isDom(type[0]) == Color.isDom(type[1]);

                if (alphaIsPrimary && (c == Color.RED || c == Color.ORANGE)) return ColorType.PRIMARY;
                if (!alphaIsPrimary && (c == Color.RED || c == Color.ORANGE)) return ColorType.SECONDARY;
                if (!alphaIsPrimary && (c == Color.BLUE || c == Color.GREEN)) return ColorType.PRIMARY;
                if (alphaIsPrimary && (c == Color.BLUE || c == Color.GREEN)) return ColorType.SECONDARY;

                return null;
            }

            public static Color getPrimaryColor(Cube instance, Piece p){

                Color[] type = getPairType(instance, p);
                if (type == null) return null;

                boolean alphaIsPrimary = Color.isDom(type[0]) == Color.isDom(type[1]);

                if (alphaIsPrimary) return type[0];
                else return type[1];
            }
        }

        // return the color to the left when yellow is on top
        private static Color getLeftColor(Color c){
            switch(c){
                case RED: return Color.BLUE;
                case GREEN: return Color.RED;
                case ORANGE: return Color.GREEN;
                case BLUE: return Color.ORANGE;
                default: return null;
            }
        }

        // return the color to the right when yellow is on top
        private static Color getRightColor(Color c){
            switch(c){
                case RED: return Color.GREEN;
                case GREEN: return Color.ORANGE;
                case ORANGE: return Color.BLUE;
                case BLUE: return Color.RED;
                default: return null;
            }
        }

        // return the color to the back when yellow is on top
        private static Color getBackColor(Color c){
            return Color.opp(c);
        }

        private static Pair identifyPair(Cube instance, Piece[] pairPieces){

            for (Pair p : Pair.values()){

                Piece[] pieces = getPieces(p);
                
                /*System.out.println("correct pieces: " + pieces[0].toString() + ":" + pieces[1].toString());
                System.out.println("current pieces: " + pairPieces[0].toString() + ":" + pairPieces[1].toString());*/

                // check if same pieces
                if (!Arrays.equals(pieces, pairPieces)) continue;

                // check if pieces are orientated the same way
                ColorType[] cornerOrientation = getPrimaryCornerColors(p);
                ColorType edgeOrientation = getPrimaryEdgeColor(p);

                // cube orientation
                ColorType[] cube_cornerOrient = getCornerColorTypes(instance, pairPieces[0]);
                ColorType cube_edgeOrient = getEdgeColorType(instance, pairPieces[1]);


                /*DEBUGGING
                System.out.println("Correct:");
                for (ColorType cT : cornerOrientation){
                    System.out.print(cT.toString() + " ");
                }
                System.out.println();
                System.out.println(edgeOrientation.toString());


                System.out.println("Current:");
                for (ColorType cT : cube_cornerOrient){
                    System.out.print(cT.toString() + " ");
                }
                System.out.println();
                System.out.println(cube_edgeOrient.toString());
                // END BUGGING */



                if (!Arrays.equals(cornerOrientation, cube_cornerOrient)) continue;
                if (edgeOrientation != cube_edgeOrient) continue;

                // success!!
                return p;
            }

            return null;
        }

        public static Pair identifyOrientatePair(Cube instance, Color[] colors){

            Cube copy = new Cube(instance);
            Piece[] pairPieces = null;
            Pair pairType = null;

            pairPieces = pairOnTop(copy, colors);
            pairType = identifyPair(copy, pairPieces);
            if (pairType != null) return pairType;

            copy = new Cube(instance);
            Cube.turn(copy, "Y");
            pairPieces = pairOnTop(copy, colors);
            pairType = identifyPair(copy, pairPieces);
            if (pairType != null) return pairType;

            copy = new Cube(instance);
            Cube.turn(copy, "Y'");
            pairPieces = pairOnTop(copy, colors);
            pairType = identifyPair(copy, pairPieces);
            if (pairType != null) return pairType;

            copy = new Cube(instance);
            Cube.turn(copy, "Y2");
            pairPieces = pairOnTop(copy, colors);
            pairType = identifyPair(copy, pairPieces);
            if (pairType != null) return pairType;

            System.out.println("Something went wrong identifying a pair.  Returned null");

            return null;
        }

        /**
         * returns sequence that is the @param String seq 
         * converted into a cube sequence
         * 
         * yellow is assumed to be on top
         *  */ 
        public static String convertSeq(String seq, Color frontFace){

            // frontFace must be a face color that's not white or yellow
            if (frontFace == Color.YELLOW || frontFace == Color.WHITE || frontFace == null) return null; 

            String left = getLeftColor(frontFace).toString();
            String right = getRightColor(frontFace).toString();
            String back = getBackColor(frontFace).toString();

            String result = "";

            for (int i = 0; i < seq.length(); i++){

                String c = seq.substring(i, i+1);
                
                if (c.equals("R")){
                    c = right;
                } else if (c.equals("B")){
                    c = back;
                } else if (c.equals("L")){
                    c = left;
                } else if (c.equals("F")){
                    c = frontFace.toString();
                } else if (c.equals("U")){
                    c = "Y";
                } else if (c.equals("D")){
                    c = "W";
                }

                result += c;
            }

            return result;
        }

        // returns all sequences that solve pair
        public static String[] getSequences(Pair pair){

            switch(pair){
                case U11: return new String[]{"L' U2 L U L' U' L"};
                case U12: return new String[]{"B L' B' L F U F'","L F' L' F U F U F'",};
                case U21: return new String[]{"L' U2 L U' L' U L"};
                case U22: return new String[]{"F R U2 R' F'"};
                case U31: return new String[]{"L' B' U2 B L"};
                case U32: return new String[]{"F U2 F2 L F L'"};
                case U41: return new String[]{"L U F U' F' L' F U' F'"};
                case U42: return new String[]{"F U2 F' U' F U F'"};
                case B11: return new String[]{"L' U L U' L' U' L"};
                case B12: return new String[]{"F' U F U' F U F'","L' U L U2 F U F'"};
                case B21: return new String[]{"L' U' L"};
                case B22: return new String[]{"F U2 F' U2 F U' F'"};
                case B31: return new String[]{"L U' L2 U' L","L' U' L U' L' U' L"};
                case B32: return new String[]{"F U F' U2 F U' F'"};
                case B41: return new String[]{"L U2 L2 U' L","F U2 F' U L' U' L"};
                case B42: return new String[]{"F U' F'"};
                case L11: return new String[]{"L' U L"};
                case L12: return new String[]{"F' U2 F2 U F'","L' U2 L U' F U F'"};
                case L21: return new String[]{"L' U' L U2 L' U L"};
                case L22: return new String[]{"F' U F2 U F'","F U F' U F U F'","F U F' U' F U F'"};
                case L31: return new String[]{"L' U2 L U2 L' U L"};
                case L32: return new String[]{"F U F'"};
                case L41: return new String[]{"L U' L' U L' U' L","F U' F' U2 L' U' L"};
                case L42: return new String[]{"F U' F' U F U F'"}; // fixed
            }

            return null;
        }

        // returns the pieces relative to a red-green pair
        public static Piece[] getPieces(Pair pair){

            switch(pair){
                case U11: 
                case U12:
                case B11: 
                case B12: 
                case L11: 
                case L12: return new Piece[]{Piece.R8B6Y0, Piece.B7Y1};
                case U21: 
                case U22:
                case B21: 
                case B22: 
                case L21:
                case L22: return new Piece[]{Piece.R8B6Y0, Piece.O7Y5};
                case U31:
                case U32:
                case B31: 
                case B32: 
                case L31: 
                case L32: return new Piece[]{Piece.R8B6Y0, Piece.G7Y7};
                case U41: 
                case U42:
                case B41: 
                case B42:
                case L41: 
                case L42: return new Piece[]{Piece.R8B6Y0, Piece.R7Y3};
            }

            return null;
        }

        private static ColorType[] getCornerColorTypes(Cube instance, Piece corner){

            // keeping it relative to one pair
            if (corner != Piece.R8B6Y0) return null;

            Color left = Cube.getAlpha(instance, corner);
            Color back = Cube.getBeta(instance, corner);
            Color top = Cube.getGamma(instance, corner);

            ColorType[] colorTypes = {ColorType.getType(instance, corner, left), ColorType.getType(instance, corner, back), ColorType.getType(instance, corner, top)};

            return colorTypes;
        }

        private static ColorType getEdgeColorType(Cube instance, Piece edge){
            // if not a pair edge on top... return null;
            if (edge != Piece.R7Y3 && edge != Piece.B7Y1 && edge != Piece.O7Y5 && edge != Piece.G7Y7) return null;

            Color topColor = Cube.getGamma(instance, edge);

            return ColorType.getType(instance, edge, topColor);
        }

        // returns the order of primary and secondary colors of the corner of the pair in order of L(left) B(back) U(up)
        // 1 = primary color is here
        // 2 = secondary color is here
        // 0 = white is here
        private static ColorType[] getPrimaryCornerColors(Pair pair){

            switch(pair){
                case U11: 
                case U12: 
                case U21: 
                case U22:
                case U31:
                case U32:
                case U41: 
                case U42: return new ColorType[]{ColorType.SECONDARY, ColorType.PRIMARY, ColorType.NULL};
                case B11: 
                case B12: 
                case B21: 
                case B22: 
                case B31: 
                case B32: 
                case B41: 
                case B42: return new ColorType[]{ColorType.PRIMARY, ColorType.NULL, ColorType.SECONDARY};
                case L11: 
                case L12: 
                case L21:
                case L22: 
                case L31: 
                case L32: 
                case L41: 
                case L42: return new ColorType[]{ColorType.NULL, ColorType.SECONDARY, ColorType.PRIMARY};
            }

            return null;
        }

        // returns TRUE if the primary color is on top of this edge piece for this pair type, false otherwise
        private static ColorType getPrimaryEdgeColor(Pair pair){

            switch(pair){
                case U12:
                case U22:
                case U32:
                case U42: 
                case B12: 
                case B22: 
                case B32: 
                case B42: 
                case L12: 
                case L22: 
                case L32: 
                case L42: return ColorType.SECONDARY;
                case U11: 
                case U21: 
                case U31:
                case U41: 
                case B11: 
                case B21: 
                case B31: 
                case B41: 
                case L11: 
                case L21:
                case L31: 
                case L41: return ColorType.PRIMARY;
            }

            return ColorType.NULL;
        }

        public static String getPairSeq(Cube instance, Pair type, int pairs, Color primaryColor){

            // store pair sequence (there may be more than one, aka L22 is the only special case)
            String[] orderSequences = getSequences(type);
            // convert the sequences
            String[] sequences = new String[orderSequences.length];
            for (int i = 0; i < orderSequences.length; i++){
                sequences[i] = convertSeq(orderSequences[i], primaryColor);
            }
            Cube copy = null;

            // for first move try each sequence
            for (int i = 0; i < sequences.length; i++){
                copy = new Cube(instance);

                Cube.scramble(copy, sequences[i]);

                // prints
                System.out.println(sequences[i]);
                System.out.println(copy);

                System.out.println(pairs(copy));
                if (pairs(copy) == pairs){
                    return sequences[i];
                }
            }

            // for the rest of moves try each sequence
            // orientate for 4 times....
                
            String[] moveSet = {"Y", "Y'", "Y2"};
            for (String move : moveSet){
                // do pair sequence
                for (int i = 0; i < sequences.length; i++){
                    // orientate
                    copy = new Cube(instance);
                    Cube.turn(copy, move);
    
                    // try solving sequence
                    Cube.scramble(copy, sequences[i]);

                    // prints
                    System.out.println(move + " " + sequences[i]);
                    System.out.println("pairs: " + pairs(copy));
                    System.out.println(copy);

                    // if we meet the pairs goal aka did it solve?
                    
                    if (pairs(copy) == pairs){
                        // return sequence done
                        return move + " " + sequences[i];
                    }
                }
            }                

            return null;
        }
    }

    public CFOPAlg(Cube other){
        super(other);
    }

    public boolean solveUsingCFOP(Cube instance) {
        // step 1: solve the cross
        if (!solveCross(instance)) {
            return false;
        }

        // step 2: solve the first two layers (F2L)
        if (!solveF2L(instance)) {
            return false;
        }

        // step 3: orient the last layer (OLL)
        if (!solveOLL(instance)) {
            return false;
        }

        // step 4: Permute the last layer (PLL)
        if (!solvePLL(instance)) {
            return false;
        }

        // if all steps succeed, set the sequence and return true
        this.sequence = instance.sequence;
        return true;
    }

    /**
     * solves the cross on the cube
     * returns true if successful, false if it fails to make progress
     */

    public boolean solveCross(Cube instance) {
        // Placeholder for the logic to solve the cross.
        // Implement actual cross-solving logic here.

        // Solve each edge of the cross
        String totalSeq = "";
        int crosses = 0;
        while(true){

            crosses = crossEdged(instance);
            if (crosses >= 4) break;

            // get solve to another part of the cross
            String seq = solveToCross(instance, crosses+1); // add method of breadth searcth to find solve

            // do solve
            Cube.scramble(instance, seq); // crosses should automatically update after

            totalSeq += seq + " ";
        }

        // orientate appropriately to centers
        int times = 0;
        while(true){
            
            String seq = "";

            // if centers are orientated
            if (crossCompleted(instance)){
                switch(times){
                    case 1: seq += "W"; break;
                    case 2: seq += "W2"; break;
                    case 3: seq += "W'"; break;
                }
                totalSeq += seq + " ";
                break;
            }

            times++;
            Cube.turn(instance, Color.WHITE, true);
        }

        System.out.println(totalSeq);

        return true;
    }

    // solve a certain amount of white piece edges
    private String solveToCross(Cube instance, int cross){

        if (crossEdged(instance) == cross) return null;

        // SOLVING SETUP
        instance.sequence = "";
        instance.prev = null; // IMPORTANT

        // QUEUES THAT REPRESENT POSSIBLE PERMUTAITON OF GPRIME AND SOLVED IN n MOVES
        LinkedList<Cube> q = new LinkedList<>();
        q.addLast(instance);

        // SETUP VAIRABLES
        int qItr = 0; // n for GPRIME

        Cube copy = null;

        // BREADTH-FIRST SEARCH ACROSS PERMUTATIONS
        while (!q.isEmpty() && q.size() < maxInstances && qItr < 5){

            // controls n permutations
            // clear out the queue of n permutations...
            while (!q.isEmpty() && q.size() < maxInstances && q.peek().sequence.split(" ").length <= qItr){
                // get first in line to be iterated
                copy = q.poll();

                // this method might be optimized later to find shorter solution where cfop edges can be solved during another one's

                // check for cross edges
                if (crossEdged(copy) == cross) return copy.sequence;

                // add instances from this copy
                addinstances(copy, q, false, false, null);
            }
            qItr++;

        }

        // according to my theory on solving the cross, this should NEVER return null...
        System.out.println("Something went wrong.  Returned null.");
        return null;
    }

    // returns the number of edges that are in the correct order for the cross, regardless of oritentation away from their center colors
    private int crossEdged(Cube instance){
        // num is between 1-4
        // checks if that many white cross edges have been solved

        // get alpha & beta
        // white-cross in order from yellow on top
        Color[] correctOrder = {Color.RED, Color.GREEN, Color.ORANGE, Color.BLUE};
        // get the white face
        Color[] order = {Cube.getAlpha(instance, Piece.R1W3), Cube.getBeta(instance, Piece.G1W1), Cube.getAlpha(instance, Piece.O1W5), Cube.getBeta(instance, Piece.B1W7)};
        Color[] gamma = {Cube.getGamma(instance, Piece.R1W3), Cube.getGamma(instance, Piece.G1W1), Cube.getGamma(instance, Piece.O1W5), Cube.getGamma(instance, Piece.B1W7)};

        // check for similarities
        int crosses = 0;
        int maxCrosses = 0;

        // use a cycling method to find the max matches
        for (int i = 0; i < 4; i++){

            crosses = 0;
            // check gamma if white
            if (gamma[0] == Color.WHITE && order[0] == correctOrder[i%4]) crosses++;
            if (gamma[1] == Color.WHITE && order[1] == correctOrder[(i+1)%4]) crosses++;
            if (gamma[2] == Color.WHITE && order[2] == correctOrder[(i+2)%4]) crosses++;
            if (gamma[3] == Color.WHITE && order[3] == correctOrder[(i+3)%4]) crosses++;

            if (maxCrosses < crosses) maxCrosses = crosses;
        }

        return maxCrosses;
    }

    private static boolean crossCompleted(Cube instance){

        // check each edge piece
        if (Cube.getGamma(instance, Piece.R1W3) != Color.WHITE || Cube.getAlpha(instance, Piece.R1W3) != Color.RED) return false;
        if (Cube.getGamma(instance, Piece.G1W1) != Color.WHITE || Cube.getBeta(instance, Piece.G1W1) != Color.GREEN) return false;
        if (Cube.getGamma(instance, Piece.O1W5) != Color.WHITE || Cube.getAlpha(instance, Piece.O1W5) != Color.ORANGE) return false;
        if (Cube.getGamma(instance, Piece.B1W7) != Color.WHITE || Cube.getBeta(instance, Piece.B1W7) != Color.BLUE) return false;

        return true;
    }

    // I might consider putting this in the Cube class instead.
    private static boolean hasColor(Cube instance, Piece p, Color color){

        // if any of the faces are colored "color", return true.. false if none of the faces are colored "color"
        if (Cube.getAlpha(instance, p) == color) return true;
        if (Cube.getBeta(instance, p) == color) return true;
        if (Cube.getGamma(instance, p) == color) return true;

        return false;
    }

    public static Color[] getPairType(Cube instance, Piece p){

        Color[] type = new Color[2];

        Color[] colors = {Cube.getAlpha(instance, p), Cube.getBeta(instance, p), Cube.getGamma(instance, p)};

        for (Color c : colors){
            if (c == Color.RED) type[0] = Color.RED;
            if (c == Color.ORANGE) type[0] = Color.ORANGE;
            if (c == Color.BLUE) type[1] = Color.BLUE;
            if (c == Color.GREEN) type[1] = Color.GREEN;
        }

        return type;
    }

    // checks the yellow face for any existing pairs
    private static Piece[] pairOnTop(Cube instance, int pairs){

        // check if cross has been maintained
        if (!crossCompleted(instance)) {
            return null;
        }
        if (pairs(instance) != pairs) {
            return null; // make sure current structure was maintained
        }


        // first check for white colors on yellow corners
        Piece[] corners = {Piece.R8B6Y0, Piece.O6B8Y2, Piece.O8G6Y8, Piece.R6G8Y6};
        Piece[] edges = {Piece.R7Y3, Piece.B7Y1, Piece.O7Y5, Piece.G7Y7};

        for (Piece c : corners){

            if (hasColor(instance, c, Color.WHITE)){

                // identify pair-colors
                Color[] type = getPairType(instance, c);

                // check if any edges match type
                for (Piece e : edges){
                    if (Arrays.equals(type, getPairType(instance, e))){
                        // found one!!
                        System.out.println("pair found of type: " + type[0].toString() + ":" + type[1].toString());

                        // return locations
                        Piece[] pieceLocs = {c, e};

                        return pieceLocs; // we should return the pair type
                    }
                }

            }
        }

        return null;
    }

    // checks for a pair of colors
    private static Piece[] pairOnTop(Cube instance, Color[] colors){

        // check if cross has been maintained
        if (!crossCompleted(instance)) {
            return null;
        }

        // first check for white colors on yellow corners
        Piece[] corners = {Piece.R8B6Y0, Piece.O6B8Y2, Piece.O8G6Y8, Piece.R6G8Y6};
        Piece[] edges = {Piece.R7Y3, Piece.B7Y1, Piece.O7Y5, Piece.G7Y7};

        for (Piece c : corners){

            if (hasColor(instance, c, Color.WHITE)){

                // identify pair-colors
                Color[] type = getPairType(instance, c);

                if (!Arrays.equals(type, colors)) continue;

                // check if any edges match type
                for (Piece e : edges){
                    if (Arrays.equals(type, getPairType(instance, e))){
                        // found one!!
                        System.out.println("pair found of type: " + type[0].toString() + ":" + type[1].toString());

                        // return locations
                        Piece[] pieceLocs = {c, e};

                        return pieceLocs; // we should return the pair type
                    }
                }

            }
        }

        return null;
    }

    // returns the number of completed pairs in the cube
    private static int pairs(Cube instance){

        // precondition of cross being completed
        if (!crossCompleted(instance)) return 0;

        // identify how many pairs are in place!!
        int numPairs = 0;
        Piece[][] pairs = {{Piece.R2B0W6, Piece.R5B3}, {Piece.O0B2W8, Piece.O3B5}, {Piece.O2G0W2, Piece.O5G3}, {Piece.R0G2W0, Piece.R3G5}};

        for(Piece[] pair : pairs){

            // check corner is in place
            if (Cube.getAlpha(instance, pair[0]) != Piece.getAlpha(pair[0])) continue;
            if (Cube.getBeta(instance, pair[0]) != Piece.getBeta(pair[0])) continue;
            if (Cube.getGamma(instance, pair[0]) != Piece.getGamma(pair[0])) continue;

            // check edge is in place
            if (Cube.getAlpha(instance, pair[1]) != Piece.getAlpha(pair[1])) continue;
            if (Cube.getBeta(instance, pair[1]) != Piece.getBeta(pair[1])) continue;

            numPairs++;
        }

        return numPairs;
    }

    /**
     * solves the first two layers (F2L) on the cube
     * returns true if successful, false if it fails to make progress
     */
    public boolean solveF2L(Cube instance) {
        // Placeholder for the logic to solve F2L.
        // Implement actual F2L-solving logic here.

        // preconditions: cross has to be done
        // Solve each pair of F2L
        String totalSeq = "";
        int pairs = 0;
        while(true){

            pairs = pairs(instance);
            System.out.println("pairs returned " + pairs);
            if (pairs >= 4) break;

            // get solve to another part of pairs
            String seq = solveToF2L(instance, pairs+1); // add method of breadth searcth to find solve

            System.out.println("seq received: \'" + seq + "\'");
            System.out.println(instance);

            // do solve
            boolean worked = Cube.scramble(instance, seq); // pairs should automatically update after

            System.out.println(worked + "\n" + instance);

            totalSeq += seq + " ";
        }

        System.out.println(totalSeq);

        return true;
    }

    private String solveToF2L(Cube instance, int pairs){
        
        if (pairs(instance) == pairs) return null;

        // SOLVING SETUP
        instance.sequence = "";
        instance.prev = null; // IMPORTANT

        // QUEUES THAT REPRESENT POSSIBLE PERMUTAITON OF GPRIME AND SOLVED IN n MOVES
        LinkedList<Cube> q = new LinkedList<>();
        q.addLast(instance);

        // SETUP VAIRABLES
        int qItr = 0; // n for GPRIME

        Cube copy = null;

        // BREADTH-FIRST SEARCH ACROSS PERMUTATIONS
        while (!q.isEmpty() && q.size() < maxInstances && qItr < 6){

            // controls n permutations
            // clear out the queue of n permutations...
            while (!q.isEmpty() && q.size() < maxInstances && q.peek().sequence.split(" ").length <= qItr){
                // get first in line to be iterated
                copy = q.poll();

                // can be optimized but idk where to start

                // check if pair on top w/o curr structure being broken
                Piece[] type = pairOnTop(copy, pairs-1); // THIS FAILS PLEASE TEST
                if (type != null){

                    System.out.println("pair on top!");
                    System.out.println(copy.sequence);
                    System.out.println(copy);

                    Color[] colors = getPairType(copy, type[0]);
                    // identify orientation
                    Pair pairType = Pair.identifyOrientatePair(copy, colors);

                    // get orientation sequence
                    Color primaryColor = Pair.ColorType.getPrimaryColor(copy, type[0]);
                    String pairSequence = Pair.getPairSeq(copy, pairType, pairs, primaryColor); // built-in orientation sequence with plug in sequence

                    System.out.println("final pair seq: " + pairSequence);

                    if (copy.sequence.length() < 1) return pairSequence;
                    return copy.sequence + " " + pairSequence;
                }

                // add instances from this copy
                addinstances(copy, q, false, false, Color.WHITE);
            }
            qItr++;

        }

        // according to my theory on solving the cross, this should NEVER return null...
        System.out.println("Something went wrong.  Returned null.");
        return null;
    }

    /**
     * solves the orientation of the last layer (OLL) on the cube
     * returns true if successful, false if it fails to make progress
     */

    private boolean solveOLL(Cube instance) {
        // Placeholder for the logic to solve OLL.
        // Implement actual OLL-solving logic here.

        

        return false;
    }

    /**
     * solves the permutation of the last layer (PLL) on the cube
     * returns true if successful, false if it fails to make progress
     */

    private boolean solvePLL(Cube instance) {
        // Placeholder for the logic to solve PLL.
        // Implement actual PLL-solving logic here.
        return false;
    }
}