// main class
public class Main{

    public static void main(String[] args){

        Cube cube = new Cube();
        Cube cube2 = cube;
        Cube cube3 = new Cube(cube);

        cube.turn("W'");

        System.out.println(cube);
        System.out.println(cube2);
        System.out.println(cube3);


        /*
        // showcase all 18 permutations
        for (Cube.Color c : Cube.Color.values()){

            cube.turn(c, true);
            System.out.println(c);
            System.out.println(cube);
            cube.toDefault();

            cube.turn(c, false);
            System.out.println(c + "\'");
            System.out.println(cube);
            cube.toDefault();

            cube.turn(c, true);
            cube.turn(c, true);
            System.out.println(c + "2");
            System.out.println(cube);
            cube.toDefault();
        }*/

        
    }
}