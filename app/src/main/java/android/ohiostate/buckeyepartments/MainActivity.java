package android.ohiostate.buckeyepartments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private int dbButtonId;
    private int mapButtonId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbButtonId = R.id.db_button;
        mapButtonId = R.id.map_button;
    }

    public void viewDatabase(View view) {
        if(view.getId() == dbButtonId) {
            Intent intent = new Intent(this, viewDatabaseActivity.class);
            startActivity(intent);
        }
    }

    public void viewMap(View view) {
        if(view.getId() == mapButtonId) {
            Intent intent = new Intent(this, viewMapActivity.class);
            startActivity(intent);
        }
    }
}
