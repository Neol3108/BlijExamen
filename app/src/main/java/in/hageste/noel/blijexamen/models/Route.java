package in.hageste.noel.blijexamen.models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;

import in.hageste.noel.blijexamen.DBHelper;

public class Route {
    private int id;
    private String name;
    private ArrayList<Feeding> feedings;

    private double locLat;
    private double locLong;
    private float zoomMin;
    private float zoomMax;

    private Route(int id, String name, double la, double lo, float zoomMin, float zoomMax) {
        this.id = id;
        this.name = name;
        this.feedings = Feeding.route(id);

        this.locLat = la;
        this.locLong = lo;
        this.zoomMin = zoomMin;
        this.zoomMax = zoomMax;
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
            sb.append(feeding.getAnimal().getName()).append(feedings.indexOf(feeding) == feedings.size()-1 ? "" : ", ");
        }

        return sb.toString();
    }

    public Calendar getBeginTime() {
        Calendar res = null;
        for (Feeding feeding : feedings) {
            if (res == null) {
                res = feeding.getTime();
            } else if(feeding.getTime().compareTo(res) < 0) {
                res = feeding.getTime();
            }
        }
        return res;
    }

    public Calendar getEndTime() {
        Calendar res = null;
        for (Feeding feeding : feedings) {
            if (res == null) {
                res = feeding.getEndTime();
            } else if(feeding.getEndTime().compareTo(res) > 0) {
                res = feeding.getEndTime();
            }
        }
        return res;
    }

    public void setMapSettings(GoogleMap map) {
        this.setMapSettings(map, true);
    }

    private void setMapSettings(GoogleMap map, boolean zoomLimit) {
        double la = (locLat != 0 ? locLat : 51.926159);
        double lo = (locLong != 0 ? locLong : 4.446775);
        float zoomMin = (this.zoomMin != 0 ? this.zoomMin : 14.6f);
        float zoomMax = (this.zoomMax != 0 ? this.zoomMax : 15.1f);

        float zoom = zoomMin + ((zoomMax - zoomMin) / 2);

        if(zoomLimit) {
            map.setMinZoomPreference(zoomMin);
            map.setMaxZoomPreference(zoomMax);
        }
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(la, lo), zoom));
    }

    public static ArrayList<Route> all(boolean toBe) {
        ArrayList<Route> data = new ArrayList<>();
        SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();
        Cursor routes = db.query("routes", new String[]{"id", "name", "lat", "long", "zoomMin", "zoomMax"}, null, null, null, null, null);

        while(routes.moveToNext()) {
            Route route = new Route(
                    routes.getInt(routes.getColumnIndex("id")),
                    routes.getString(routes.getColumnIndex("name")),
                    routes.getDouble(routes.getColumnIndex("lat")),
                    routes.getDouble(routes.getColumnIndex("long")),
                    routes.getFloat(routes.getColumnIndex("zoomMin")),
                    routes.getFloat(routes.getColumnIndex("zoomMax"))
            );

            if(toBe) {
                if(route.getEndTime() != null && Calendar.getInstance().compareTo(route.getEndTime()) < 0) data.add(route);
            } else {
                data.add(route);
            }
        }

        routes.close();
        db.close();

        return data;
    }

    public static Route findById(int id) {
        SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();
        Cursor routes = db.query("routes", new String[]{"id", "name", "lat", "long", "zoomMin", "zoomMax"}, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        Route route = null;
        if(routes.moveToFirst()) {

            Log.i("ROUTEID", String.valueOf(routes.getInt(routes.getColumnIndex("id"))));
            route = new Route(
                    routes.getInt(routes.getColumnIndex("id")),
                    routes.getString(routes.getColumnIndex("name")),
                    routes.getDouble(routes.getColumnIndex("lat")),
                    routes.getDouble(routes.getColumnIndex("long")),
                    routes.getFloat(routes.getColumnIndex("zoomMin")),
                    routes.getFloat(routes.getColumnIndex("zoomMax"))
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
