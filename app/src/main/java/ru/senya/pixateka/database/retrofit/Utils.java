package ru.senya.pixateka.database.retrofit;

public class Utils {
    public static String BASE_URL = "http://192.168.1.60:8000/api/";
    public static String TOKEN;
    public static String SESSION_ID;

    public static void setTOKEN(String TOKEN) {
        Utils.TOKEN = TOKEN;
    }

    public static void setSessionId(String sessionId) {
        SESSION_ID = sessionId;
    }
}
