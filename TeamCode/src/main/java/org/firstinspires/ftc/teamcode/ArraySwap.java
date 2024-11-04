/**
 * This class swaps elements across or within the same array.
 * 
 * It also has a customized swap function that swaps across 4 specific elements
 * in a certain order (clockwise or counterclockwise) defined in the function.
 * 
 * The Cube data structure class uses this class in order to accomplish
 * the representation of configurations of a cube, like turning.
 */
import java.util.*;

public abstract class ArraySwap{
    // swaps these elements in the array
    public static void swap(Object[] array, int idx1, int idx2){
        swap(array, idx1, array, idx2);
    }

    // this overloaded method swaps across arrays
    public static void swap(Object[] array1, int idx1, Object[] array2, int idx2){
        Object buffer = array1[idx1]; // buffer still needed bc/ it could be the same array
        array1[idx1] = array2[idx2];
        array2[idx2] = buffer;
    }

    // overload with same array of swap four
    public static void swapFour(Object[] array, int idx1, int idx2, int idx3, int idx4, boolean clockwise){
        swapFour(array, idx1, array, idx2, array, idx3, array, idx4, clockwise);
    }

    // swaps across these 4 arrays in order
    public static void swapFour(Object[] arr1, int idx1, Object[] arr2, int idx2, Object[] arr3, int idx3, Object[] arr4, int idx4, boolean clockwise){
        /*
        clock wise:
            arr1 -> arr2
            
             ^        v
             
            arr4 <- arr3
        */
        // swapping clockwise in this function is defined as moving arr1 to arr2, arr2 to arr3... arr4 to arr1.
        if (clockwise){
            swap(arr1, idx1, arr2, idx2);
            swap(arr4, idx4, arr1, idx1);
            swap(arr3, idx3, arr4, idx4); 
        } else { 
            swap(arr4, idx4, arr1, idx1);
            swap(arr1, idx1, arr2, idx2);
            swap(arr2, idx2, arr3, idx3);
        }
    }
}