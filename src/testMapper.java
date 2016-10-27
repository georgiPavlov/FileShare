import com.GP.MultithreadImplementation.LockEntry;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;



import java.io.IOException;

/**
 * Created by user on 10/25/16.
 */
public class testMapper {
    public static void main(String[] args) {
        LockEntry e = new LockEntry("hello");
        LockEntry b = new LockEntry("hello");
        System.out.println(e.equals(b));
    }

}
