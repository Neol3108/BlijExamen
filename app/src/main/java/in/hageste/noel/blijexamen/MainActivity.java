package in.hageste.noel.blijexamen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBHelper.setInstance(this);
    }

    public void onEnter(View v) {
        startActivity(new Intent(this, RouteList.class));
    }
}
