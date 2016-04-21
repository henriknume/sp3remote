package io.hnnt.sp3remote;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Bind (R.id.log_textview) TextView      logTextView;
    @Bind (R.id.info_button) Button         infoButton;
    @Bind (R.id.show_date_button) Button    showDateButton;
    @Bind (R.id.logga_button) Button        loggaButton;
    @Bind (R.id.show_lon_button) Button     showLonButton;
    @Bind (R.id.show_lat_button) Button     showLatButton;
    @Bind (R.id.auto_button) Button         autoButton;
    @Bind (R.id.set_date_button) Button     setDateButton;
    @Bind (R.id.up_button) Button           upButton;
    @Bind (R.id.left_button) Button         leftButton;
    @Bind (R.id.right_button) Button        rightButton;
    @Bind (R.id.down_button) Button         downButton;
    @Bind (R.id.stop_button) Button         stopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.info_button)
    public void onInfoButton() {

    }

    @OnClick(R.id.show_date_button)
    public void onShowDateButton() {

    }

    @OnClick(R.id.logga_button)
    public void onLoggaButton() {

    }

    @OnClick(R.id.show_lon_button)
    public void onShowLonButton() {

    }

    @OnClick(R.id.show_lat_button)
    public void onShowLatButton() {

    }

    @OnClick(R.id.auto_button)
    public void onAutoButton() {

    }

    @OnClick(R.id.set_date_button)
    public void onSetDateButton() {

    }

    @OnClick(R.id.up_button)
    public void onUpButton() {

    }

    @OnClick(R.id.left_button)
    public void onLeftButton() {

    }

    @OnClick(R.id.right_button)
    public void onRightButton() {

    }

    @OnClick(R.id.down_button)
    public void onDownButton() {

    }

    @OnClick(R.id.stop_button)
    public void onStopButton() {

    }

}
