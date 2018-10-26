package voicerecognition.cheongmin.com.voicerecognition;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Intent intent;
    SpeechRecognizer mRecognizer;
    TextView textView1, textView2, textView3;
    boolean check = false;
    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_RECORD_AUDIO
                );
            }
        }

        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");

        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(recognitionListener);
        if(SpeechRecognizer.isRecognitionAvailable(this)){
            Log.d("SpeechRecognizer","True");
        }
        else{
            Log.d("SpeechRecognizer","False");
        }


        textView1 = (TextView) findViewById(R.id.tv1);
        textView2 = (TextView) findViewById(R.id.tv2);
        textView3 = (TextView) findViewById(R.id.tv3);

        Button button = (Button) findViewById(R.id.btn1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecognizer.startListening(intent);
            }
        });

    }

    private RecognitionListener recognitionListener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) {
            Log.d("SpeechRecognizer","onReadyForSpeech");
        }

        @Override
        public void onBeginningOfSpeech() {
            Log.d("SpeechRecognizer","onReadyForSpeech");
        }

        @Override
        public void onRmsChanged(float v) {
            Log.d("SpeechRecognizer","onRmsChanged");
        }

        @Override
        public void onBufferReceived(byte[] bytes) {
            Log.d("SpeechRecognizer","onBufferReceived");
        }

        @Override
        public void onEndOfSpeech() {
            Log.d("SpeechRecognizer","onEndOfSpeech");
        }

        @Override
        public void onError(int i) {
            Log.d("SpeechRecognizer","onError");
            textView3.setText("너무 늦게 말하면 오류뜹니다");
        }

        @Override
        public void onResults(Bundle bundle) {
            Log.d("SpeechRecognizer","onResults");
            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = bundle.getStringArrayList(key);

            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);

            KoreanAnalyzer answer = new KoreanAnalyzer("안녕하세요");
            KoreanAnalyzer stt_result = new KoreanAnalyzer(rs[0]);

            textView1.setText(answer.getText());
            textView2.setText(stt_result.getText());
            textView3.setText(Double.toString((int)(answer.getSimilarity(stt_result)*1000)/(double)10)+"%");
        }

        @Override
        public void onPartialResults(Bundle bundle) {
            Log.d("SpeechRecognizer","onPartialResults");
        }

        @Override
        public void onEvent(int i, Bundle bundle) {
            Log.d("SpeechRecognizer","onEvent");
        }
    };
}
