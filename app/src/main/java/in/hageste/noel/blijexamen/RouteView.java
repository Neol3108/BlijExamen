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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.model.LatLng;

import in.hageste.noel.blijexamen.adapters.FeedingsListAdapter;
import in.hageste.noel.blijexamen.models.Feeding;
import in.hageste.noel.blijexamen.models.Route;

public class RouteView extends AppCompatActivity implements OnMapReadyCallback, LocationListener {

    private Route route;
    private RecyclerView feedingListView;
    private FeedingsListAdapter feedingsListAdapter;
//    private GeoApiContext geoApiContext;

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

//        geoApiContext = new GeoApiContext()
//                .setQueryRateLimit(10)
//                .setApiKey(getString(R.string.MAPS_API_KEY))
//                .setConnectTimeout(1, TimeUnit.SECONDS)
//                .setReadTimeout(1, TimeUnit.SECONDS)
//                .setWriteTimeout(1, TimeUnit.SECONDS);

        // Find route
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
            feedingsListAdapter.setOnItemClickListener(new FeedingsListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Feeding feeding) {
                    feeding.getMarker().showInfoWindow();
                }
            });
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
            LatLng loc = feeding.getAnimal().getLocation();
            Marker marker = map.addMarker(new MarkerOptions().position(new com.google.android.gms.maps.model.LatLng(loc.lat, loc.lng)).title(feeding.getAnimal().getName()));
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(getIconBitmap(nr)));
            feeding.setMarker(marker);
            nr++;
        }

//        DirectionsApiRequest request = DirectionsApi.newRequest(geoApiContext).mode(TravelMode.WALKING).departureTime(new DateTime());
//        int index = 0;
//        for(Feeding feeding : route.getFeedings()) {
//            LatLng loc = feeding.getAnimal().getLocation();
//            if(index == 0) {
//                request.origin(loc);
//            } else if(index == route.getFeedings().size()-1) {
//                request.destination(loc);
//            } else {
//                request.waypoints(loc);
//            }
//            index++;
//        }
//
//        try {
//            DirectionsResult result = request.await();
//            if(result != null && result.routes != null) {
//                map.addPolyline(new PolylineOptions().addAll(PolyUtil.decode(result.routes[0].overviewPolyline.getEncodedPath())));
//            }
//
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ApiException e) {
//            e.printStackTrace();
//        }
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

    @Override
    public void onLocationChanged(Location location) { }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}
}
