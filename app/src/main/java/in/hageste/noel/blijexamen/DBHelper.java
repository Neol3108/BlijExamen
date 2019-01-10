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
                "duration INTEGER, " +
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
                "name VARCHAR(50), " +
                "lat DOUBLE, " +
                "long DOUBLE, " +
                "zoomMin FLOAT, " +
                "zoomMax FLOAT" +
                ")");

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

        ContentValues beheerders = new ContentValues();
        beheerders.put("name", "Beheerders");
        beheerders.put("lat", 51.854691);
        beheerders.put("long", 4.309319);

        ContentValues rekenen = new ContentValues();
        rekenen.put("name", "Reken docenten");
        rekenen.put("lat", 51.854653);
        rekenen.put("long", 4.309624);

        ContentValues admin = new ContentValues();
        admin.put("name", "Administratie");
        admin.put("lat", 51.854526);
        admin.put("long", 4.309197);

        db.insert("animals", null, lion);
        db.insert("animals", null, polar);
        db.insert("animals", null, dolphin);
        db.insert("animals", null, beheerders);
        db.insert("animals", null, rekenen);
        db.insert("animals", null, admin);


        ContentValues flion1 = new ContentValues();
        flion1.put("time", "600");
        flion1.put("duration", 5);
        flion1.put("animal_id", 1);

        ContentValues flion2 = new ContentValues();
        flion2.put("time", "845");
        flion2.put("duration", 5);
        flion2.put("animal_id", 1);


        ContentValues fpolar1 = new ContentValues();
        fpolar1.put("time", "605");
        fpolar1.put("duration", 5);
        fpolar1.put("animal_id", 2);


        ContentValues fdolphin1 = new ContentValues();
        fdolphin1.put("time", "610");
        fdolphin1.put("duration", 5);
        fdolphin1.put("animal_id", 3);

        ContentValues fdolphin2 = new ContentValues();
        fdolphin2.put("time", "840");
        fdolphin2.put("duration", 5);
        fdolphin2.put("animal_id", 3);


        ContentValues fbeheerders = new ContentValues();
        fbeheerders.put("time", "720");
        fbeheerders.put("duration", 10);
        fbeheerders.put("animal_id", 4);

        ContentValues frekenen = new ContentValues();
        frekenen.put("time", "735");
        frekenen.put("duration", 5);
        frekenen.put("animal_id", 5);

        ContentValues fadmin = new ContentValues();
        fadmin.put("time", "745");
        fadmin.put("duration", 10);
        fadmin.put("animal_id", 6);


        db.insert("feedingtimes", null, flion1);
        db.insert("feedingtimes", null, flion2);
        db.insert("feedingtimes", null, fpolar1);
        db.insert("feedingtimes", null, fdolphin1);
        db.insert("feedingtimes", null, fdolphin2);

        db.insert("feedingtimes", null, fbeheerders);
        db.insert("feedingtimes", null, frekenen);
        db.insert("feedingtimes", null, fadmin);


        ContentValues route1 = new ContentValues();
        route1.put("name", "Route 1");

        ContentValues route2 = new ContentValues();
        route2.put("name", "Route 2");

        ContentValues zadkineRoute = new ContentValues();
        zadkineRoute.put("name", "Tour de Zadkine");
        zadkineRoute.put("lat", 51.8545032);
        zadkineRoute.put("long", 4.3090097);
        zadkineRoute.put("zoomMin", 18.3f);
        zadkineRoute.put("zoomMax", 18.6f);

        db.insert("routes", null, route1);
        db.insert("routes", null, route2);
        db.insert("routes", null, zadkineRoute);


        ContentValues feed11 = new ContentValues();
        feed11.put("route_id", 1);
        feed11.put("feedingtime_id", 1);

        ContentValues feed13 = new ContentValues();
        feed13.put("route_id", 1);
        feed13.put("feedingtime_id", 3);

        ContentValues feed14 = new ContentValues();
        feed14.put("route_id", 1);
        feed14.put("feedingtime_id", 4);


        ContentValues feed22 = new ContentValues();
        feed22.put("route_id", 2);
        feed22.put("feedingtime_id", 2);

        ContentValues feed25 = new ContentValues();
        feed25.put("route_id", 2);
        feed25.put("feedingtime_id", 5);


        ContentValues feed36 = new ContentValues();
        feed36.put("route_id", 3);
        feed36.put("feedingtime_id", 6);

        ContentValues feed37 = new ContentValues();
        feed37.put("route_id", 3);
        feed37.put("feedingtime_id", 7);

        ContentValues feed38 = new ContentValues();
        feed38.put("route_id", 3);
        feed38.put("feedingtime_id", 8);


        db.insert("route_feedingtimes", null, feed11);
        db.insert("route_feedingtimes", null, feed13);
        db.insert("route_feedingtimes", null, feed14);
        db.insert("route_feedingtimes", null, feed22);
        db.insert("route_feedingtimes", null, feed25);

        db.insert("route_feedingtimes", null, feed36);
        db.insert("route_feedingtimes", null, feed37);
        db.insert("route_feedingtimes", null, feed38);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
}
