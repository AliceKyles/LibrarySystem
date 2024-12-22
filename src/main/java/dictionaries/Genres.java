package dictionaries;

import java.util.Map;

public class Genres {

    public static int DETECTIVE = 1;
    public static int ROMANCE = 2;
    public static int FANTASY = 3;
    public static int ACTION = 4;
    public static int MYSTERY = 5;
    public static int COMEDY = 6;
    public static int POETRY = 7;
    public static int DRAMA = 8;
    public static int HISTORICAL = 9;

    public static Map<Integer, String> GENRE_NAMES = Map.of(DETECTIVE, "Detective", ROMANCE, "Romance", FANTASY, "Fantasy", ACTION, "Action", MYSTERY, "Mystery", COMEDY, "Comedy", POETRY, "Poetry", DRAMA, "Drama", HISTORICAL, "Historical");
}
