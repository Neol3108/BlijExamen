package in.hageste.noel.blijexamen;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import in.hageste.noel.blijexamen.adapters.FeedingsListAdapter;
import in.hageste.noel.blijexamen.models.Feeding;
import in.hageste.noel.blijexamen.models.Route;

public class RouteView extends AppCompatActivity implements OnMapReadyCallback {

    private Route route;
    private RecyclerView feedingListView;
    private FeedingsListAdapter feedingsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_view);

        SupportMapFragment smf = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if(smf != null) {
            smf.getMapAsync(this);
        }

        int id = getIntent().getIntExtra("id", -1);
        if(id != -1) {
            route = Route.findById(id);

            if(route == null) finish();

            TextView name = findViewById(R.id.name);
            name.setText(route.getName());

            feedingListView = findViewById(R.id.feedingList);
            // Decorate routeList
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            feedingListView.setLayoutManager(layoutManager);
            DividerItemDecorator dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(RouteView.this, R.drawable.divider));
            feedingListView.addItemDecoration(dividerItemDecoration);

            // Set routeList adapter
            feedingsListAdapter = new FeedingsListAdapter(this, route.getFeedings());
            feedingListView.setAdapter(feedingsListAdapter);
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setScrollGesturesEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);

        route.setMapSettings(map);

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return true;
            }
        });
        map.setMyLocationEnabled(true);

        int nr = 1;
        for(Feeding feeding : route.getFeedings()) {
            map.addMarker(new MarkerOptions().position(feeding.getAnimal().getLocation()).title(feeding.getAnimal().getName())).setIcon(BitmapDescriptorFactory.fromBitmap(getIconBitmap(nr)));
            nr++;
        }
    }

    private Bitmap getIconBitmap(int nr) {
        Bitmap bmp = Bitmap.createBitmap(50, 50, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);

        Paint color = new Paint();
        color.setColor(Color.rgb(63, 81, 181));

        canvas.drawRect(0, 0, 50, 50, color);


        Paint textColor = new Paint();
        textColor.setColor(Color.rgb(230, 230, 230));
        textColor.setTextSize(40F);
        canvas.drawText(String.valueOf(nr), 12, 40, textColor);

        return bmp;
    }
}
