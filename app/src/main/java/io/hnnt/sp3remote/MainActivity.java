package io.hnnt.sp3remote;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Bind (R.id.textView) TextView textView;
    @Bind (R.id.info_button) Button infoButton;
    @Bind (R.id.show_date_button) Button showDateButton;
    @Bind (R.id.logga_button) Button loggaButton;
    @Bind (R.id.show_lon_button) Button showLonButton;
    @Bind (R.id.show_lat_button) Button showLatButton;
    @Bind (R.id.auto_button) Button autoButton;
    @Bind (R.id.set_date_button) Button setDateButton;
    @Bind (R.id.up_button) Button upButton;
    @Bind (R.id.left_button) Button leftButton;
    @Bind (R.id.right_button) Button rightButton;
    @Bind (R.id.down_button) Button downButton;
    @Bind (R.id.stop_button) Button stopButton;




}
