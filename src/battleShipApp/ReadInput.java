package battleShipApp;
import java.io.*;


public class ReadInput {
    static int[] toIntArray(String[] arr) {
        int[] ints = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            ints[i] = Integer.parseInt(arr[i]);
        }
        return ints;
    }

    static int[] parseLineToIntArray(String line) {
        return toIntArray(line.split(","));
    }
    static int[][] inputPlacement (String path){
        File file = new File(path);
        BufferedReader reader = null;
        int [][] placements = new int[5][];
        int i =0;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                int [] input = parseLineToIntArray(line);
                placements[i++] = input;
            }

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            }
            catch (IOException e) {
            }
        }
        return placements;
    }

    static void validate ( int[][] placements) throws InvalidCountException{
        boolean[] appearances = new boolean[5];
        for (int count=0; count<5; count++){
            int type = placements[count][0];
            if(appearances[type-1]){
                throw new InvalidCountException("More than 1 ship of the same type");
            }
            else{
                appearances[type-1] = true;
            }
        }
    }
}
