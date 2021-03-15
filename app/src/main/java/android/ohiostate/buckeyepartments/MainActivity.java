package android.ohiostate.buckeyepartments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private int buttonId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonId = R.id.button;
    }

    public void viewDatabase(View view) {
        if(view.getId() == buttonId) {
            Intent intent = new Intent(this, viewDatabaseActivity.class);
            startActivity(intent);
        }
    }
}
