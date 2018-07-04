package whethermonitor.naveen.com.whethermonitor;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {
    private Handler mHandler = new Handler();
    TextView welcomeText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        welcomeText = (TextView)findViewById(R.id.welcome_text);
        Typeface typeface= Typeface.createFromAsset(getApplicationContext().getAssets(),"stripe_font.ttf");
        welcomeText.setTypeface(typeface);
        new Thread(){
            public void run(){
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        doInitialSetup();
                    }
                });
            }
        }.start();
      /*  if (getIntent().getBooleanExtra("EXIT", false))
        {
            finish();
        }*/
    }
    private void doInitialSetup(){

        Intent i = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(i);
        finish();


    }
}
