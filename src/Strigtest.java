/**
 * Created by user on 10/24/16.
 */
public class Strigtest {
    public static void main(String[] args) {
        String a = "/home/user/FileShare/files/videos/The.Flash.2014.S03E02.XviD-AFG.avi";
        String[] b = a.split("\\/");
        System.out.println(b[b.length-1]);
    }
}
