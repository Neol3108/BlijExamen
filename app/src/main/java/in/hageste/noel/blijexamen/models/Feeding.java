package in.hageste.noel.blijexamen.models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.Calendar;

import in.hageste.noel.blijexamen.DBHelper;

public class Feeding {
    private int id;
    private Calendar time;
    private Calendar endTime;
    private int duration;
    private Animal animal;

    private Marker marker;

    private Feeding(int id, int duration, int time, Animal animal) {
        this.id = id;
        this.animal = animal;

        this.duration = duration;

        this.time = Calendar.getInstance();
        this.time.set(Calendar.HOUR_OF_DAY, (int) Math.floor(time / 60));
        this.time.set(Calendar.MINUTE, (int) Math.floor(time % 60));

        this.endTime = Calendar.getInstance();
        this.endTime.set(Calendar.HOUR_OF_DAY, (int) Math.floor((time + duration) / 60));
        this.endTime.set(Calendar.MINUTE, (int) Math.floor((time + duration) % 60));
    }

    public Animal getAnimal() {
        return animal;
    }

    public int getDuration() {
        return duration;
    }

    public Calendar getTime() {
        return time;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public Marker getMarker() {
        return marker;
    }

    static ArrayList<Feeding> route(int id) {
        ArrayList<Feeding> result = new ArrayList<>();
        SQLiteDatabase db = DBHelper.getInstance().getReadableDatabase();
        Cursor feedings = db.rawQuery("SELECT feedingtimes.id, animal_id, time, duration FROM route_feedingtimes JOIN feedingtimes ON feedingtimes.id = route_feedingtimes.feedingtime_id WHERE route_id = ? ORDER BY time", new String[]{String.valueOf(id)});

        while(feedings.moveToNext()) {
            result.add(
                    new Feeding(
                            feedings.getInt(feedings.getColumnIndex("id")),
                            feedings.getInt(feedings.getColumnIndex("duration")),
                            feedings.getInt(feedings.getColumnIndex("time")),
                            Animal.findById(feedings.getInt(feedings.getColumnIndex("animal_id"))))
            );
        }

        feedings.close();
        db.close();

        return result;
    }
}
