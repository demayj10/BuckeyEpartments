package android.ohiostate.buckeyepartments;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        pauseScreen();
    }
    private void pauseScreen() {
        Thread threadDelay = new Thread() {
            @Override
            public void run() {
                //try {
                    // sleep(6000); // 5 seconds delay (wtf????)
                    startActivity(new Intent(SplashScreen.this,MainActivity.class));
                    finish();
                //} catch (InterruptedException e) {
                //    e.printStackTrace();
                //}
            }
        };
        threadDelay.start();
    }
}