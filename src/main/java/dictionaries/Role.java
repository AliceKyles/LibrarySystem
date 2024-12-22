package dictionaries;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Role {
    public static int ADMIN = 1;
    public static int LIBRARIAN = 2;
    public static int READER = 3;

    public static Map<Integer, String> ROLE_NAMES = Map.of(ADMIN, "Admin", LIBRARIAN, "Librarian", READER, "Reader");

    public static List<Integer> CAN_EDIT_USERS = Arrays.asList(ADMIN, LIBRARIAN);
}
