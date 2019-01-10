package in.hageste.noel.blijexamen;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
        routeListAdapter = new RouteListAdapter(this, Route.all(), new RouteListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Route route) {
                Intent intent = new Intent(RouteList.this, RouteView.class);
                intent.putExtra("id", route.getId());
                startActivity(intent);
            }
        });
        routeListView.setAdapter(routeListAdapter);
    }
}
