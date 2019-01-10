package in.hageste.noel.blijexamen;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper sInstance;


    public static synchronized DBHelper setInstance(Context context) {
        if (sInstance == null) {
            sInstance = new DBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    public static DBHelper getInstance() {
        return sInstance;
    }

    private DBHelper(Context context) {
        super(context, "BLIJ", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                "animals " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name VARCHAR(50) NOT NULL, " +
                "lat DOUBLE, " +
                "long DOUBLE)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                "feedingtimes " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "time INTEGER, " +
                "animal_id INTEGER, " +
                "FOREIGN KEY(animal_id) REFERENCES animals(id) ON UPDATE CASCADE ON DELETE CASCADE)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                "route_feedingtimes " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "route_id INTEGER, " +
                "feedingtime_id INTEGER, " +
                "FOREIGN KEY(route_id) REFERENCES routes(id) ON UPDATE CASCADE ON DELETE CASCADE, " +
                "FOREIGN KEY(feedingtime_id) REFERENCES feedingtimes(id) ON UPDATE CASCADE ON DELETE CASCADE)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                "routes " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name VARCHAR(50))");

        ContentValues lion = new ContentValues();
        lion.put("name", "Leeuwen");
        lion.put("lat", 51.92703988);
        lion.put("long", 4.45007175);

        ContentValues polar = new ContentValues();
        polar.put("name", "Ijsberen");
        polar.put("lat", 51.92751955);
        polar.put("long", 4.44503456);

        ContentValues dolphin = new ContentValues();
        dolphin.put("name", "Dolfijnen");
        dolphin.put("lat", 51.92853179);
        dolphin.put("long", 4.44546372);

        db.insert("animals", null, lion);
        db.insert("animals", null, polar);
        db.insert("animals", null, dolphin);


        ContentValues flion1 = new ContentValues();
        flion1.put("time", "600");
        flion1.put("animal_id", 1);

        ContentValues flion2 = new ContentValues();
        flion2.put("time", "850");
        flion2.put("animal_id", 1);


        ContentValues fpolar1 = new ContentValues();
        fpolar1.put("time", "605");
        fpolar1.put("animal_id", 2);

        ContentValues fpolar2 = new ContentValues();
        fpolar2.put("time", "845");
        fpolar2.put("animal_id", 2);


        ContentValues fdolphin1 = new ContentValues();
        fdolphin1.put("time", "610");
        fdolphin1.put("animal_id", 3);

        ContentValues fdolphin2 = new ContentValues();
        fdolphin2.put("time", "840");
        fdolphin2.put("animal_id", 3);

        db.insert("feedingtimes", null, flion1);
        db.insert("feedingtimes", null, flion2);
        db.insert("feedingtimes", null, fpolar1);
        db.insert("feedingtimes", null, fpolar2);
        db.insert("feedingtimes", null, fdolphin1);
        db.insert("feedingtimes", null, fdolphin2);


        ContentValues route1 = new ContentValues();
        route1.put("name", "Route 1");

        ContentValues route2 = new ContentValues();
        route2.put("name", "Route 2");

        db.insert("routes", null, route1);
        db.insert("routes", null, route2);


        ContentValues feed11 = new ContentValues();
        feed11.put("route_id", 1);
        feed11.put("feedingtime_id", 1);

        ContentValues feed13 = new ContentValues();
        feed13.put("route_id", 1);
        feed13.put("feedingtime_id", 3);

        ContentValues feed15 = new ContentValues();
        feed15.put("route_id", 1);
        feed15.put("feedingtime_id", 5);


        ContentValues feed22 = new ContentValues();
        feed22.put("route_id", 2);
        feed22.put("feedingtime_id", 2);

        ContentValues feed24 = new ContentValues();
        feed24.put("route_id", 2);
        feed24.put("feedingtime_id", 4);

        ContentValues feed26 = new ContentValues();
        feed26.put("route_id", 2);
        feed26.put("feedingtime_id", 6);


        db.insert("route_feedingtimes", null, feed11);
        db.insert("route_feedingtimes", null, feed13);
        db.insert("route_feedingtimes", null, feed15);
        db.insert("route_feedingtimes", null, feed22);
        db.insert("route_feedingtimes", null, feed24);
        db.insert("route_feedingtimes", null, feed26);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
}
