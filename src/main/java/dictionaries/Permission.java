package dictionaries;

import java.util.HashMap;
import java.util.Map;

public class Permission {
    public static int VIEW_BOOKS = 1;
    public static int CHECK_OUT_BOOKS = 2;

    public static Map<Integer, String> PERMISSIONS = new HashMap<>() {{
        put(0, "No permissions");
        put(VIEW_BOOKS, "Can view books");
        put(CHECK_OUT_BOOKS, "Can check out books");
    }};
}
