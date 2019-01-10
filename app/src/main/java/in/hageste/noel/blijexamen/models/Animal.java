package in.hageste.noel.blijexamen.models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.SparseArray;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import in.hageste.noel.blijexamen.DBHelper;

public class Animal {

    private static SparseArray<Animal> cache = new SparseArray<>();

    private int id;
    private String name;
    private double locLat;
    private double locLong;

    public Animal(int id, String name, double locLat, double locLong) {
        this.id = id;
        this.name = name;
        this.locLat = locLat;
        this.locLong = locLong;

        cache.append(id, this);
    }

    public LatLng getLocation() {
        return new LatLng(locLat, locLong);
    }

    public String getName() {
        return name;
    }

    public static Animal findById(int id) {
        if(cache.get(id) != null) {
            return cache.get(id);
        } else {
            ArrayList<Route> data = new ArrayList<>();
            SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();
            Cursor animal = db.query("animals", new String[]{"id", "name", "lat", "long"}, "id=?", new String[]{String.valueOf(id)}, null, null, null);
            Animal result = null;

            if(animal.moveToFirst()) {
                result = new Animal(
                        animal.getInt(animal.getColumnIndex("id")),
                        animal.getString(animal.getColumnIndex("name")),
                        animal.getDouble(animal.getColumnIndex("lat")),
                        animal.getDouble(animal.getColumnIndex("long"))
                );
            }

            animal.close();
            db.close();
            return result;
        }
    }
}
