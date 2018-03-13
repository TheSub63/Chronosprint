package iut.chronosprint;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Chronometer;
import java.util.Timer;
import java.util.TimerTask;

public class Chrono extends AppCompatActivity{

    public Chrono(Chronometer c) {
        Clock=c;
    }

    public Chronometer getClock() {
        return Clock;
    }

    private Chronometer Clock;

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }

    private boolean isStarted;

    public boolean isRunningMode() {
        return isRunningMode;
    }

    public void setRunningMode(boolean runningMode) {
        isRunningMode = runningMode;
    }

    private boolean isRunningMode;

    private Timer t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timer t= new Timer();
        Log.i("ChronoActivity","Created");
    }

    public void startChrono(){
        isStarted=true;
        final long delay=1000;
        Clock.setBase(SystemClock.elapsedRealtime());
        Clock.start();
        //Toast.makeText(getBaseContext(),"GO",
        //Toast.LENGTH_LONG).show();
        //if(t==null) t=new Timer();
        //t.schedule(new TimerTask() {
        //    @Override
        //    public void run() {
        //        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        //        if (v != null && v.hasVibrator()) {
        //            v.vibrate(delay/2);
        //        }
        //    }
        //},delay);
    }

    public void stopChrono(){
        if(isStarted) {
            isStarted = false;
            Clock.stop();
            /*Toast.makeText(getBaseContext(), "GG",
                    Toast.LENGTH_LONG).show();*/
        }
    }

    public void resetChrono(){

    }
}
