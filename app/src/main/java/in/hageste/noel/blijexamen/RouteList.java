package in.hageste.noel.blijexamen;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import in.hageste.noel.blijexamen.adapters.RouteListAdapter;
import in.hageste.noel.blijexamen.models.Route;

public class RouteList extends AppCompatActivity {

    private RecyclerView routeListView;
    private RouteListAdapter routeListAdapter;

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
        ArrayList<Route> routes = Route.all(false);
        routeListAdapter = new RouteListAdapter(this, routes, new RouteListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Route route) {
                Intent intent = new Intent(RouteList.this, RouteView.class);
                intent.putExtra("id", route.getId());
                startActivity(intent);
            }
        });
        routeListView.setAdapter(routeListAdapter);

        if(routes.size() <= 0) findViewById(R.id.noRoutes).setVisibility(View.VISIBLE);
    }
}
