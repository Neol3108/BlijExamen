package in.hageste.noel.blijexamen;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import in.hageste.noel.blijexamen.adapters.RouteListAdapter;
import in.hageste.noel.blijexamen.models.Route;

public class RouteList extends AppCompatActivity {

    private RecyclerView routeListView;
    private RouteListAdapter routeListAdapter;
    private TextView timeDisplay;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Default android shit
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_list);

        // Get routeList RecyclerView
        routeListView = findViewById(R.id.routeList);

        // Decorate routeList
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        routeListView.setLayoutManager(layoutManager);
        DividerItemDecorator dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(RouteList.this, R.drawable.divider));
        routeListView.addItemDecoration(dividerItemDecoration);

        // Set routeList adapter
        ArrayList<Route> routes = Route.all(true);
        routeListAdapter = new RouteListAdapter(this, routes, route -> {
            Intent intent = new Intent(RouteList.this, RouteView.class);
            intent.putExtra("id", route.getId());
            startActivity(intent);
        });
        routeListView.setAdapter(routeListAdapter);

        if(routes.size() <= 0) findViewById(R.id.noRoutes).setVisibility(View.VISIBLE);

        // Time display
        timeDisplay = findViewById(R.id.timeDisplay);
        timeDisplay.setText(getTime());

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                timeDisplay.setText(getTime());
                handler.postDelayed(this, 1000 * 60);
            }
        }, milliSecondsToNextMinute());
    }

    private static String getTime() {
        return new SimpleDateFormat("hh:mm", Locale.US).format(Calendar.getInstance().getTime());
    }

    private static Long milliSecondsToNextMinute() {
        Calendar now = Calendar.getInstance();
        Calendar then = Calendar.getInstance();
        then.set(Calendar.SECOND, 0);
        then.set(Calendar.MILLISECOND, 0);
        then.add(Calendar.MINUTE, 1);
        return then.getTime().getTime() - now.getTime().getTime();
    }
}
