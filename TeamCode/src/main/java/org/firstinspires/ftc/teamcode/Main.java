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
         * B2 W' Y O2 B2 Y' B2 - (O2 Y' W B2 R2 Y R2 - B2 R2 Y' WG2 Y R2) (i somehow got 2 diff solutions AND THEY BOTH WORK?????)
         * O2, B2, G2, Y2, R2, W', Y', G2 - B2 R2 W2 B2 W' Y' B2 R2
         */

         CubeAlgorithm alg = new CubeAlgorithm(null);

        Cube cube = new Cube("R Y W G B2 W");
        System.out.println(cube);
        System.out.println(alg.solveToG_PRIME(cube));
        System.out.println(alg.scramble.sequence);

        Cube gprime = new Cube();
        Cube.scramble(gprime, "B2 W' Y O2 B2 Y' B2");
        //gprime = CubeAlgorithm.makeG_PRIME(6, 20);
        System.out.println(gprime);

        System.out.println(alg.solveG_PRIME(gprime));
        System.out.println(alg.gSequence);
        
            
    }
}