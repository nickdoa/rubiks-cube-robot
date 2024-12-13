// main class
public class Main{

    public static void main(String[] args){

        /**
         * scrambles that got into g-prime
         * 
         * move sequences r done from left to right
         * 
         * [Y', B2, G2, W', Y', G2, B2, Y', R2, W', Y, R', G, B, O'] - [O, G', B', R]
         * [W, Y2, W', R, Y', R, O', G] - [G, O', R, W', R]
         * [B', G', Y2, B', Y2, B2, G2, O', G', R] - [R', G, O, W2, G]
         * [R, Y, W, G, B2, W] - [W', G, Y', W', O]
         * 
         */

        /**
         * g-prime scrambles that get solved
         * B2 W' Y O2 B2 Y' B2 - (O2 Y' W B2 R2 Y R2 - B2 R2 Y' W G2 Y R2) (i somehow got 2 diff solutions AND THEY BOTH WORK?????)
         * O2, B2, G2, Y2, R2, W', Y', G2 - B2 R2 W2 B2 W' Y' B2 R2
         * [Y', G2, B2, R2, W, G2, Y2, O2, W, B2] - O2 B2 Y O2 Y2 B2 W B2 O2 W
         * R2, B2, R2, W, Y', G2, B2, R2, G2, R2, G2 - R2 B2 Y W' B2 R2 B2
         * R2, O2, R2, Y', R2, O2, R2, G2, W', Y', W', R2, O2 - O2 R2 Y W2 G2 O2 Y O2
         * Y, O2, B2, G2, O2, W - Y' B2 R2 O2 null??  (null in this case was B2 W') // FIXED!!!
         * B2, W2, B2, W', O2, W2, Y2, O2, Y', R2, G2, W - W' G2 R2 W B2 G2 Y G2 Y2 B2
         * O2, R2, G2, W2, G2, O2, G2, B2, W, Y, B2 - B2 Y' W' G2 B2 R2 B2 Y2 B2
         * O2, W2, O2, R2, B2, G2, R2, G2, W', Y2, R2, O2, R2, Y, R2, G2 - G2 R2 Y' O2 Y2 W G2 O2 B2 G2 W2 O2 (this one took a while... LIKE 1-5 MINS)
         */

        /**
         * full solves:
         * B2, Y2, B, O', R, B' - B O R' B B2 Y Y B2 (double y because that was the connection point) (8 move solve)
         * B2, Y, W', O, R2, O', Y', W, G', W, G2 - G2 W' G Y W' R2 W Y' B2 (9 move solve)
         * 
         * HS: GRGRRRGBGOGBGBGOBBYWYWWYYYYWYWWYWWYWRROOOOROOBGRBGBBOR - O2 Y' G2 B2 W B2 W G2 O2 Y2 R2 Y' O2 // this one is interesting bc this is a gprime scramble i made by intuitively putting
         * the edges in, and dong OLL's to match the tops, and it got into GPRIME... it also took WAY TOO LONG to sort the arrays, so im consdiering making an insertion sort instead.
         */

        //CubeAlgorithm alg = new CubeAlgorithm(null);

        /*Cube cube = new Cube("R Y W G B2 W");
        System.out.println(cube);
        System.out.println(alg.solveToG_PRIME(cube));
        System.out.println(alg.scramble.sequence);*/

        /*Cube gprime = new Cube();
        //Cube.scramble(gprime, "Y O2 B2 G2 O2 W");
        gprime = CubeAlgorithm.makeG_PRIME(6, 20);
        System.out.println(gprime);

        System.out.println(alg.solveG_PRIME(gprime));
        System.out.println(alg.gSequence);*/

        //Cube cube = Cube.fromStringHash("OGBWRWYBOOGYGBBWYRBRROWYWWOGOYWYRGRWGGBYOOBBRWYYBGRGGR");
        //Cube.scramble(cube, 6, 20);
        //System.out.println(cube);

        //System.out.println(alg.kociemba_solve(cube));
        //System.out.println(alg.finalSeq);

        /*Cube cube = Cube.fromStringHash("GRGRRRGBGOGBGBGOBBYWYWWYYYYWYWWYWWYWRROOOOROOBGRBGBBOR");
        System.out.println(cube);
        System.out.println(CubeAlgorithm.isG_PRIME(cube));

        alg.solveG_PRIME(cube);

        System.out.println(alg.gSequence);
        System.out.println(alg.finalSeq);*/

        /**
         * cfop scrambles that were interesting
         * 
         * B', O', R2, O, R2, Y', W', G', B2 -> reduces to -> B' Y' W' G' B2   solve: B2 G W B B' Y B (literally the best what the sigma, except reducing for B & B')
         * 
         * 
         * pre solved olls
         * B2, W2, B', O2, R2, B2, G2
         */

        CFOPAlg alg = new CFOPAlg(null);

        // cfop testings
        Cube cube = new Cube();
        Cube.scramble(cube, 6, 20);

        System.out.println(cube);

        alg.solveCross(cube);

        System.out.println(cube);

        alg.solveF2L(cube);

        System.out.println(cube);

        alg.solveOLL(cube);

        System.out.println(cube);

    }
}