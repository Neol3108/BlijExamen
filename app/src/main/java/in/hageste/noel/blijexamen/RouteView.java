package in.hageste.noel.blijexamen;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import in.hageste.noel.blijexamen.adapters.FeedingsListAdapter;
import in.hageste.noel.blijexamen.models.Feeding;
import in.hageste.noel.blijexamen.models.Route;

public class RouteView extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private Route route;
    private GeoApiContext geoApiContext;
    private GoogleMap gMap;
    private PolylineOptions po;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Default Android shit
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_view);

        // Start map
        SupportMapFragment smf = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (smf != null) {
            smf.getMapAsync(this);
        }

        // Start location manager
        LocationManager locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if(locManager != null) {
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1L, 0F, this);
        }


        geoApiContext = new GeoApiContext.Builder()
                .apiKey(getString(R.string.MAPS_API_KEY))
                .build();

        // Find route
        int id = getIntent().getIntExtra("id", -1);
        if(id != -1) {
            route = Route.findById(id);

            if(route == null) finish();

            TextView name = findViewById(R.id.name);
            name.setText(route.getName());

            RecyclerView feedingListView = findViewById(R.id.feedingList);
            // Decorate routeList
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            feedingListView.setLayoutManager(layoutManager);
            DividerItemDecorator dividerItemDecoration = new DividerItemDecorator(ContextCompat.getDrawable(RouteView.this, R.drawable.divider));
            feedingListView.addItemDecoration(dividerItemDecoration);

            // Set routeList adapter
            FeedingsListAdapter feedingsListAdapter = new FeedingsListAdapter(this, route.getFeedings());
            feedingListView.setAdapter(feedingsListAdapter);
            feedingsListAdapter.setOnItemClickListener(feeding -> feeding.getMarker().showInfoWindow());

            // RequestDirections
            requestDirections();

            if(gMap != null) {
                gMap.addPolyline(decoratePolyline(po));
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        gMap = map;

        if(po != null) {
            map.addPolyline(decoratePolyline(po));
        }

        map.getUiSettings().setMapToolbarEnabled(false);
        map.getUiSettings().setScrollGesturesEnabled(false);
        map.getUiSettings().setMyLocationButtonEnabled(false);

        route.setMapSettings(map);

        map.setOnMarkerClickListener(marker -> {
            marker.showInfoWindow();
            return true;
        });
        map.setMyLocationEnabled(true);

        int nr = 1;
        for(Feeding feeding : route.getFeedings()) {
            LatLng loc = feeding.getAnimal().getLocation();
            Marker marker = map.addMarker(new MarkerOptions().position(new com.google.android.gms.maps.model.LatLng(loc.lat, loc.lng)).title(feeding.getAnimal().getName()));
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(getIconBitmap(nr)));
            feeding.setMarker(marker);
            nr++;
        }
    }

    private void requestDirections() {
        DirectionsApiRequest request = DirectionsApi.newRequest(geoApiContext).mode(TravelMode.WALKING).departureTime(Instant.now());
        int index = 0;
        for(Feeding feeding : route.getFeedings()) {
            LatLng loc = feeding.getAnimal().getLocation();
            if(index == 0) {
                request.origin(loc);
            } else if(index == route.getFeedings().size()-1) {
                request.destination(loc);
            } else {
                request.waypoints(loc);
            }
            index++;
        }

        try {
            DirectionsResult result = request.await();
            po = new PolylineOptions();
            for (DirectionsRoute dr : result.routes) {
                po.addAll(PolyUtil.decode(dr.overviewPolyline.getEncodedPath()));
            }
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static PolylineOptions decoratePolyline(PolylineOptions po) {
        return po.color(Color.rgb(64, 64, 255)).pattern(getDirectionPattern()).width(5);
    }

    private static Bitmap getIconBitmap(int nr) {
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

    private static List<PatternItem> getDirectionPattern() {
        return Arrays.asList(new Gap(10), new Dash(20));
    }

    @Override
    public void onLocationChanged(Location location) { }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}
}
