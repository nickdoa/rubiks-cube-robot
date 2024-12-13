import java.util.*;

/***
 * This class will handle ALL algorithm logic for solving a cube, or possibly
 * returning a sequence of moves that reach a desired outcome of a certain
 * permutation.
 * 
 * The purpose of this class is to return a sequence of moves to do on
 * the specified Cube instantiated within it.
 */
public class CubeAlgorithm {
    protected static Cube solved;
    public Cube scramble;

    protected int maxItrs = 6; // 6 makes me run out of memory...
    protected int maxInstances = 1000000; // take 2726049 instances is max memory LOL
    public String gSequence;
    public String sequence;
    public String finalSeq = null;

    

    public CubeAlgorithm(Cube other){
        scramble = other;
        solved = new Cube();
    }

    public static boolean isSolved(Cube instance){
        return instance.equals(solved);
    }

    // returns the common object between the arrays.. null if not found
    public static Cube[] hasCommons(LinkedList<Cube> arr1, LinkedList<Cube> arr2){

        // INCREASE EFFICIENCY WITH BINARY SEARCH AND MERGE SORT
        // BECAUSE RN THIS IS LEGIT JUST LINEAR SEARCH

        System.out.println("sorting... sizes:" + arr1.size() + " " + arr2.size());

        // sort arrays
        cube_mergeSort(arr1);
        cube_mergeSort(arr2);

        System.out.println("searching...");

        int commonIdx = -1;
        for (int i = 0; i < arr2.size(); i++){

            commonIdx = cube_binarySearch(arr1, 0, arr1.size(), arr2.get(i));
            //System.out.println(commonIdx);

            if (commonIdx >= 0){
                System.out.println("finished...!");
                return new Cube[]{arr1.get(commonIdx), arr2.get(i)};
            }
        }

        System.out.println("finished...");
        return null;
    }

    public static Cube makeG_PRIME(int min, int max){

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

            // modified for gprime set
            switch(face){
                case RED:
                case BLUE:
                case GREEN:
                case ORANGE:
                    turnType = 2;
                    break;
                case WHITE:
                case YELLOW:
                default: break;
            }

            // optimizing turns
            // if we have at least one turn && the previous move and this move have the same turn face...
            if (t > 0 && scrambleSeq[t-1].substring(0,1).equals(face.toString())){
                // try doing this move again.
                t--;
                continue;
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

        return new Cube(scrambleSeq);

    }

    public boolean kociemba_solve(Cube instance){

        if (solveToG_PRIME(instance)){

            if (scramble == null) return false;
            sequence = scramble.sequence;
            
            if (solveG_PRIME(scramble)){
                finalSeq = sequence + " " + gSequence;
                return true;
            } else {
                return false; // ggs, prolly shouldnt happen unless there's an error or memory oh nos
            }
        } else {
            // do cfop solve
            return false;
        }
    }

    public boolean solveG_PRIME(Cube instance){
        if (!isG_PRIME(instance)){ return false; }
        if (instance.equals(solved)) return true;

        // SOLVING SETUP
        instance.sequence = "";
        instance.prev = null; // IMPORTANT

        // QUEUES THAT REPRESENT POSSIBLE PERMUTAITON OF GPRIME AND SOLVED IN n MOVES
        LinkedList<Cube> q = new LinkedList<>();
        q.addLast(instance);

        LinkedList<Cube> sq = new LinkedList<>();
        sq.addLast(solved);

        // SETUP VAIRABLES
        int qItr = 0; // n for GPRIME
        int sqItr = 0; // n for SOLVED

        Cube clone = null;
        Cube copy = null;
        Cube[] common = null;

        // BREADTH-FIRST SEARCH ACROSS BOTH PERMUTATIONS
        while (!q.isEmpty() && q.size() < maxInstances && !sq.isEmpty() && sq.size() < maxInstances){

            // controls n permutations
            while (!q.isEmpty() && q.size() < maxInstances && q.peek().sequence.split(" ").length <= qItr){
                // get first in line to be iterated
                copy = q.poll();
                // add instances from this copy
                addinstances(copy, q, true, false, null);
            }
            qItr++;

            // COMPARE ne n permutation with solved cube queue
            common = null; // remove garbage value
            common = hasCommons(q, sq);
            if (common != null){ 
                gSequence = common[0].sequence + " " + Cube.reverseSequence(common[1].sequence);
                return true;
            }

            while (!sq.isEmpty() && sq.size() < maxInstances && sq.peek().sequence.split(" ").length <= sqItr){
                // get first sq
                copy = sq.poll();
                // add new instances of solved cube queue
                addinstances(copy, sq, true, false, null);
            }
            sqItr++;

            // compare with gprime cube queue
            common = null; // remove garbage value
            common = hasCommons(q, sq);

            if (common != null){
                gSequence = common[0].sequence + " " + Cube.reverseSequence(common[1].sequence);
                return true;
            }
        }


        scramble = clone;
        return false;
    }

    public boolean solveToG_PRIME(Cube instance){

        instance.prev = null; // IMPORTANT

        if (isG_PRIME(instance)){
            return true;
        }

        Queue<Cube> q = new LinkedList<>();
        q.add(instance);

        Cube copy = null;
        Cube clone = null;
        while (!q.isEmpty() && q.peek().sequence.split(" ").length < maxItrs && q.size() < maxInstances){
            copy = q.poll();
            if (addinstances(copy, q, false, true, null)) return true;
        }
        
        scramble = clone;
        return false;
    }

    // turns the cube to find solutions
    protected boolean addinstances(Cube copy, Queue<Cube> q, boolean g_prime, boolean check_g_prime, Color dontTurn){


        Cube clone = null;

        // for each face
        for (Color face : Color.values()){
            // optimizations
            if (dontTurn != null && face == dontTurn) continue;
            if (face == copy.prev) continue; 
            if (Color.opp(face) == copy.prev && Color.isDom(copy.prev)) continue;
            
            // last optimization i could think of
            // add if prev prev is this one && prev was opp
            if (copy.sequence.length() >= 3 && Color.opp(face) == copy.prev){ // if the sequence has at least 1 move...
                String[] seq = copy.sequence.split(" ");
                Color prevprev = Color.fromString(seq[seq.length-2].substring(0, 1));

                if (prevprev == face) continue;
            }

            // DO MOVESET
            // check for max memory exception

            
            if (!g_prime || (face == Color.WHITE || face == Color.YELLOW)){

                clone = new Cube(copy);
                Cube.turn(clone, face, true);
                if (clone.sequence.length() > 0) clone.sequence += " "; // prevent extra space at end
                clone.sequence += face.toString();
                if (check_g_prime && isG_PRIME(clone)){
                    scramble = clone;
                    return true;
                }
                q.add(clone);

                // check for max memory exception

                clone = new Cube(copy);
                Cube.turn(clone, face, false);
                if (clone.sequence.length() > 0) clone.sequence += " "; // prevent extra space at end
                clone.sequence += face.toString() + "\'";
                if (check_g_prime && isG_PRIME(clone)){
                    scramble = clone;
                    return true;
                }
                q.add(clone);

                // check for max memory exception
            }

            // double turn
            clone = new Cube(copy);
            Cube.turn(clone, face, true);
            Cube.turn(clone, face, true);
            if (clone.sequence.length() > 0) clone.sequence += " ";
            clone.sequence += face.toString() + "2";
            if (check_g_prime && isG_PRIME(clone)){
                scramble = clone;
                return true;
            }
            q.add(clone);

            
        }

        return !check_g_prime;
    }

    public static boolean isG_PRIME(Cube instance){

        // -= CHECK NON-CORNER TRIPLE PROPERTIES =-

        // *check yellow-white gamma properties*
        Piece[] topAndBottom = {Piece.R0G2W0, Piece.G1W1, Piece.O2G0W2, Piece.R1W3, Piece.O1W5, Piece.R2B0W6, Piece.B1W7, Piece.O0B2W8,
                                Piece.R6G8Y6, Piece.G7Y7, Piece.O8G6Y8, Piece.R7Y3, Piece.O7Y5, Piece.R8B6Y0, Piece.B7Y1, Piece.O6B8Y2};
        // for each top-bottom side of the yellow and white faces...
        for (Piece p : topAndBottom){
            Color gamma = Cube.getGamma(instance, p);
            // if the gamma of this piece is not yellow, white, or nothing... return false
            if (!(gamma == null || gamma == Color.YELLOW || gamma == Color.WHITE)) return false;
        }



        // -= CHECK 4 CORNER-TRIPLE PROPERTIES =-

        // check if middle section edges have matching alphas and betas throughout
        Piece[] middle = {Piece.R3G5, Piece.O5G3, Piece.R5B3, Piece.O3B5};
        for (int e = 0; e < middle.length; e++){
            // Check alpha
            Color alpha = Cube.getAlpha(instance, middle[e]);
            if (!(alpha == Color.RED || alpha == Color.ORANGE)){ return false; }
            // Check beta
            Color beta = Cube.getBeta(instance, middle[e]);
            if (!(beta == Color.BLUE || beta == Color.GREEN)){ return false; }
        }

        // *check positive-negative properties*

        // store each triplet corners
        Piece[] redGreen = new Piece[2];
        Piece[] redBlue = new Piece[2];
        Piece[] orangeBlue = new Piece[2];
        Piece[] orangeGreen = new Piece[2];
        int rg = 0, rb = 0, ob = 0, og = 0;

        // Identify locations of triplets.  Divised into corner triple groups RED-GREEN, RED-BLUE, ORANGE-GREEN, ORANGE-BLUE.
        Piece[] corners = {Piece.R0G2W0, Piece.O2G0W2, Piece.R2B0W6, Piece.O0B2W8, Piece.R6G8Y6, Piece.O8G6Y8, Piece.R8B6Y0, Piece.O6B8Y2};
        // for each corner of the cube... identify & store which corners are apart of which corner triple group...
        for (int c = 0; c < corners.length; c++){
            // get colors of the contested corner...
            Color alpha = Cube.getAlpha(instance, corners[c]);
            Color beta = Cube.getBeta(instance, corners[c]);

            // Classify this corner based off of their alpha & beta colors, and store it into the associated group...
            if (alpha == Color.RED || beta == Color.RED){
                if (alpha == Color.BLUE || beta == Color.BLUE){
                    if (rb > 1) return false; // failsafe... should generally never happen
                    redBlue[rb] = corners[c]; // store variable
                    rb++; // store another one later...
                } else if (alpha == Color.GREEN || beta == Color.GREEN){
                    if (rg > 1) return false;
                    redGreen[rg] = corners[c];
                    rg++;
                }
            } else if (alpha == Color.ORANGE || beta == Color.ORANGE){
                if (alpha == Color.BLUE || beta == Color.BLUE){
                    if (ob > 1) return false;
                    orangeBlue[ob] = corners[c];
                    ob++;
                } else if (alpha == Color.GREEN || beta == Color.GREEN){
                    if (og > 1) return false;
                    orangeGreen[og] = corners[c];
                    og++;
                }
            }
        }

        // if triplets weren't successful... return false
        if (!(rg == 2 && rb == 2 && ob == 2 && og == 2)) return false; // also shouldn't generally happen... but failsafe regardless

        // if any of the corner triples does not follow its charge property... return false
        if (!(followsChargeProperty(instance, redGreen[0], redGreen[1]) && followsChargeProperty(instance, redBlue[0], redBlue[1])
            && followsChargeProperty(instance, orangeBlue[0], orangeBlue[1]) && followsChargeProperty(instance, orangeGreen[0], orangeGreen[1]))) return false;
        // else... return true because all properties came out to be true!

        return true;
    }

    /**
     * @description
     * This method helps identify G_PRIME.  Checks if this corner triple has a negative or positive property defined in the function.
     * 
     * @preconditions
     * @param alphaC and @param betaC must ALREADY be checked to be apart of the same corner triple.  They're gammas must also already be checked to be yellow for one, and white for the ohter.
     * This allow us to say that the gamma of both pieces ON THE CUBE are ALWAYS different, to avoid further amibguity of checking this property again when it has been checked in isG_PRIME().
     * 
     * @param instance the cube where this contested triple is on...
     * @param alphaC corner1 of triple (order doesn't matter)
     * @param betaC corner2 of triple (order doesn't matter)
     * @return Returns true if this triple follows their associated charge property.  False otherwise.
     **/ 
    private static boolean followsChargeProperty(Cube instance, Piece alphaC, Piece betaC){

        /**
         * Check positive property = When alphaC & betaC are on the same face on the same side, or on opposing faces on opp. sides
         * ex. corners are right next to eachother... or corners are diagonally across the cube
         * Check negative property = When alphaC & betaC are on opposing faces but same side, or same face & opp. sides
         * ex. corners are diagonal from eachother on any same face
         **/

        // Identify type of property to check for...
        Color a_alpha = Piece.getAlpha(alphaC); // Piece.getAlpha(Piece p) differs from Cube.object.getAlpha(Piece p) as  Piece.getAlpha(Piece p) gets p's original alpha color on the solved cube.
        Color a_beta = Piece.getBeta(alphaC);
        Color a_gamma = Piece.getGamma(alphaC); // Piece.getGamma(Piece p) returns the original yellow-white color of p on the solved cube.

        Color b_alpha = Piece.getAlpha(betaC);
        Color b_beta = Piece.getBeta(betaC);
        Color b_gamma = Piece.getGamma(betaC);

        boolean positive = true;

        // CHECK if this triple is a NEGATIVE property...

        // check if one side is the same, and the other 2 are different...
        if (a_alpha == b_alpha){
            if (a_beta != b_beta && a_gamma != b_gamma){ positive = false; }
        } else if (a_beta == b_beta){
            if (a_alpha != b_alpha && a_gamma != b_gamma){ positive = false; }
        } else if (a_gamma == b_gamma){
            if (a_alpha != b_alpha && a_beta != b_beta){ positive = false; }
        }
        // if not... then this is a positive property


        // SEE if this triple FOLLOWS its corresponding +/- property on the instance...
        a_alpha = Cube.getAlpha(instance, alphaC); // change these variables to store the alpha & betas colors that it is CURRENTLY on @param Cube instance.
        a_beta = Cube.getBeta(instance, alphaC);

        b_alpha = Cube.getAlpha(instance, betaC);
        b_beta = Cube.getBeta(instance, betaC);
        // Check if the instance follows the +/- property accordingly to be apart of G_PRIME...
        if (positive){
            // if alphas are not the same as eachother or betas are not the same as easchother... return false as it does not follow the positive property
            if (a_alpha != b_alpha || a_beta != b_beta) return false;
        } else {
            // if alphas are the same as eachother or betas are the same as easchother... return false as it does not follow the negative property.
            if (a_alpha == b_alpha || a_beta == b_beta) return false;
        }
        // else... return true!

        return true;
    }

    // sorts in ascending order
    private static void cube_mergeSort(LinkedList<Cube> toSort){

        //System.out.println("\ntotal before");
        //for (int i = 0; i < toSort.size(); i++){ System.out.println(Cube.stringHash(toSort.get(i))); }

        if (toSort.size() < 2) return; // base case

        int r = toSort.size();
        int l = 0;
        int m = (l + r) / 2;

        LinkedList<Cube> left = new LinkedList<>();
        // copy left-half of array to left
        for (int i = 0; i < m; i++){ left.addLast(toSort.get(i)); }

        //System.out.println("\nleft before");
        //for (int i = 0; i < left.size(); i++){ System.out.println(Cube.stringHash(left.get(i))); }
        cube_mergeSort(left);
        //System.out.println("left after");
        //for (int i = 0; i < left.size(); i++){ System.out.println(Cube.stringHash(left.get(i))); }

        LinkedList<Cube> right = new LinkedList<>();
        // copy right-half of array to right
        for (int i = m; i < r; i++) { right.addLast(toSort.get(i)); }

        //System.out.println("\nright before");
        //for (int i = 0; i < right.size(); i++){ System.out.println(Cube.stringHash(right.get(i))); }
        cube_mergeSort(right);
        //System.out.println("right after");
        //for (int i = 0; i < right.size(); i++){ System.out.println(Cube.stringHash(right.get(i))); }

        
        // MERGE SORTED HALVES
        Cube l_cube = null;
        Cube r_cube = null;
        toSort.clear();
        for (int i = l; i < r; i++){
            if (left.isEmpty() && !right.isEmpty()){
                toSort.addLast(right.getFirst());
                right.removeFirst();
                continue;
            } else if (right.isEmpty() && !left.isEmpty()){
                toSort.addLast(left.getFirst());
                left.removeFirst();
                continue;
            } else if (left.isEmpty() && right.isEmpty()){
                break;
            }

            l_cube = left.getFirst();
            r_cube = right.getFirst();
            
            if (Cube.stringHash(l_cube).compareTo(Cube.stringHash(r_cube)) >= 0){
                toSort.addLast(r_cube);
                right.removeFirst();
            } else {
                toSort.addLast(l_cube);
                left.removeFirst();
            }
        }

        // done
        //System.out.println("total after");
        //for (int i = 0; i < toSort.size(); i++){ System.out.println(Cube.stringHash(toSort.get(i))); }
        
    }

    private static int cube_binarySearch(LinkedList<Cube> q, int l, int r, Cube key){

        int m = (l + r) / 2;
        int size = r-l;

        if (size < 2 && q.get(m).equals(key)) return m;
        if (size < 2 && !q.get(m).equals(key)) return -1;

        // do comparisons
        Cube test = q.get(m);
        int compareValue = Cube.stringHash(key).compareTo(Cube.stringHash(test));

        if (compareValue > 0){ // if key is to the right...
            return cube_binarySearch(q, m, r, key); // search right half
        } else if (compareValue < 0){ // if key is to the left...
            return cube_binarySearch(q, l, m, key); // search left half
        } else {
            return m;
        }
    }

    private static void cube_insertSorted(LinkedList<Cube> q, Cube insert){
        // insert cube into already sorted linked list

        // use binary search to find the appropriate location to insert
    }
}