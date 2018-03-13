package iut.chronosprint;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Arrays;

//GESTION DE LA VUE : CREER UNE CLASSE METIER COMPORTANT LE COMPORTEMENT, CREER UNE CLASSE CHRONO GERANT LES ATTRIBUTS ET METHODES DU CHRONO

//BUG : Une valeur d'arret en debut de course? => Probleme de la methode emptycount
// + Calculer le temps d'arret
// PRoBLEME : NE S'ARRETE PAS EN CAS DE VAKUERS LEGERES (TROP POUR EMPTY COUNT PAS ASSEZ POUR SPRINT
// A FAIRE AVANT TEST : REGLER L'ARRET PAR TOPVALUES
public class MainWindows extends AppCompatActivity implements View.OnClickListener, SensorEventListener {

    //VIEW
    private Chrono mChrono;
    private Sensor mSensor;
    private Switch mSwitch;
    private Button mStart;
    private Button mStop;
    private Button mReset;
    private TextView mTest;
    private TextView mSpeed;

    //MODEL
    private float[] topValues = new float[20];
    private int i;
    public static String message;//DEBUG ONLY
    //private int emptyCount;

    private float amplitude=0;
    private float maxAmp=0f;
    private float minVal;
    private float maxVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if(sensorManager !=null) {
            mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
            int signalPeriod = 50;
            sensorManager.registerListener(this, mSensor, signalPeriod);
        }
        initView();
        // Redonne la valeur du chrono au besoin
        if(savedInstanceState != null){
            mChrono.getClock().setBase(savedInstanceState.getLong("ChronoTime"));
            mChrono.setStarted(savedInstanceState.getBoolean("ChronoState", false));
            if(mChrono.isStarted()) mChrono.getClock().start();
        }
    }

    private void initView() {
        setContentView(R.layout.activity_chrono);
        mChrono=new Chrono((Chronometer)findViewById(R.id.chronometer));
        mStart=findViewById(R.id.start_button);
        mStart.setOnClickListener(this);
        mStop=findViewById(R.id.stop_button);
        mStop.setOnClickListener(this);
        mReset = findViewById(R.id.reset_button);
        mReset.setOnClickListener(this);
        mTest = findViewById(R.id.test_zone);
        mTest.setText(R.string.mTestInit);
        message=mTest.getText().toString();
        mSpeed=findViewById(R.id.meanSpeed);
        mSwitch=findViewById(R.id.switchRM);
        mSwitch.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.start_button:
                if(!mChrono.isStarted())
                mChrono.startChrono();
                break;
            case R.id.stop_button:
                mChrono.stopChrono();
            break;
            case R.id.reset_button:
                mChrono.resetChrono();
                TextView mText=findViewById(R.id.test_zone);
                mText.setText(MainWindows.message);
                break;
            case R.id.switchRM:
                switchAction();
                break;
        }
    }

    private void switchAction() {
        if (mSwitch.isChecked()) {
            mChrono.setRunningMode(true);
            mStart.setEnabled(false);
            mStop.setEnabled(false);
        } else {
            mChrono.setRunningMode(false);
            mStart.setEnabled(true);
            mStop.setEnabled(true);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(mChrono.isRunningMode()) {
            //Log.i("Starting", "should start");
            mSensor = sensorEvent.sensor;
            float movement = sensorEvent.values[0];
            long time=SystemClock.elapsedRealtime()-mChrono.getClock().getBase();
            //Log.i("Accelerator Value : ", Float.toString(sensorEvent.values[0]));
            if (Math.abs(movement) > 10.0 && !mChrono.isStarted()) {
                mChrono.startChrono();
                Log.i("Start Time",String.valueOf(Math.abs(movement)));

               // int emptyCount = 0;
            }


            if(mChrono.isStarted()) {
                /*METHODE EMPTY COUNT

                if (Math.abs(movement) < 4) emptyCount++;

                if (emptyCount > 50 && mChrono.isStarted()) {
                    emptyCount = 0;
                    //topValues = new float[10];
                    Log.i("Stop chrono","Stopped by emptycount");
                    mChrono.stopChrono();
                    message=message + "\n Precedente Course : "+minutesFromMilis(time);
                    mTest.setText(message);
                }
                if(Math.abs(movement)>12) emptyCount=0;*/

                /* METHODE AMPLITUDE*/
                if (Math.abs(movement) > 1)  { //Ne s'arrete pas si il n'a pas recu 10 valeurs
                    //mTest.setText(mTest.getText()+ Float.toString(movement) + "   " );
                    topValues[i % 20] = movement;
                    if(i%20==0)maxAmp=maxAmp/2;
                    if(topValues.length!=1)
                    amplitude=(amplitude+calculateAmplitude(topValues))/2;
                    if(maxAmp==0)maxAmp=amplitude;
                    mSpeed.setText(String.valueOf(minutesFromMilis(time)));
                    Log.i("Speed",String.valueOf(movement));
                    i++;
                    Log.i("Amplitude","Amplitude max:"+maxAmp+" current : "+amplitude);
                    Log.i("values", "maxVal :"+maxVal+" minVal :"+minVal+"movement :"+movement);
                    if((movement>=maxVal || movement<=minVal)&&(amplitude>maxAmp)){
                        maxAmp=amplitude;

                    }
                    if(amplitude<0.42*maxAmp) {
                        mChrono.stopChrono();
                        Log.i("Stopped BY","Amplitude max:"+maxAmp+" current : "+amplitude);
                        message=message + "\n Precedente Course : "+minutesFromMilis(time);
                        mTest.setText(message);
                    }
                }//*/

                /*METHODE TOP VALUES
                if (topValues[9] != 0.0 && mChrono.isStarted()) {
                    float sum = 0;
                    for (float f : topValues)
                        sum = sum + f;
                    //Log.i("MeanSpeed",String.valueOf(sum/10));
                    if (sum / 10.0 < 5) {
                        //mChrono.stopChrono(); // Associer dynamiquement la valeur en fonction de la course?
                        Log.i("Stop Chrono", "Stopped by tabvalues");
                        topValues = new float[10];
                        //message = message + "\n Precedente Course : " + minutesFromMilis(time);
                       // mTest.setText(message);
                    }
                    //Log.i("TopValuesSum",String.valueOf(sum));
                }*/
            }
            else {
                maxAmp=0;
                //emptyCount=0;

            }
        }
    }

    private String secondsFromMilis(long time){
        return String.valueOf(time/1000)+"."+String.valueOf(time%1000);
    }

    private String minutesFromMilis(long time){
        if(time<60000) return secondsFromMilis(time);
        return String.valueOf(time/60000)+"'"+String.valueOf((time%60000)/1000)+"\""+String.valueOf(time%1000);
    }

    /**
     * Inutilisée
     * @param sensor accelerometre
     * @param accuracy niveau de precision
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    // Sauvegarde la valeur du chrono
    @Override
    public void onSaveInstanceState (Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putLong("ChronoTime", mChrono.getClock().getBase());
        savedInstanceState.putBoolean("ChronoState", mChrono.isStarted());
        mChrono.stopChrono();
    }

    public float calculateAmplitude(float [] tab){
        Arrays.sort(tab);
        maxVal=tab[tab.length-1];
        minVal=tab[0];
        amplitude=maxVal-minVal;
        return amplitude;
    }

}
