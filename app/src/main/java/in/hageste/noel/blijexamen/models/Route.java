package in.hageste.noel.blijexamen.models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import in.hageste.noel.blijexamen.DBHelper;

public class Route {
    private int id;
    private String name;
    private ArrayList<Feeding> feedings;

    public Route(int id, String name) {
        this.id = id;
        this.name = name;
        this.feedings = Feeding.route(id);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Feeding> getFeedings() {
        return feedings;
    }

    public String getFeedingsList() {
        StringBuilder sb = new StringBuilder();

        for (Feeding feeding : feedings) {
            sb.append(feeding.getAnimal().getName() + (feedings.indexOf(feeding) == feedings.size()-1 ? "" : ", "));
        }

        return sb.toString();
    }

    public static List<Route> all() {
        ArrayList<Route> data = new ArrayList<>();
        SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();
        Cursor routes = db.query("routes", new String[]{"id", "name"}, null, null, null, null, null);

        while(routes.moveToNext()) {
            Route route = new Route(
                    routes.getInt(routes.getColumnIndex("id")),
                    routes.getString(routes.getColumnIndex("name"))
            );

            data.add(route);
        }

        routes.close();
        db.close();

        return data;
    }

    public static Route findById(int id) {
        SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();
        Cursor routes = db.query("routes", new String[]{"id", "name"}, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        Route route = null;
        if(routes.moveToFirst()) {

            Log.i("ROUTEID", String.valueOf(routes.getInt(routes.getColumnIndex("id"))));
            route = new Route(
                    routes.getInt(routes.getColumnIndex("id")),
                    routes.getString(routes.getColumnIndex("name"))
            );
        }

        routes.close();
        db.close();

        return route;
    }

    public int getId() {
        return id;
    }
}
