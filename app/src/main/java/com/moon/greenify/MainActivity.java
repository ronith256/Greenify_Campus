package com.moon.greenify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.moon.greenify.birdsAI.BirdClassification;
import com.moon.greenify.birdsAI.BirdMain;
import com.moon.greenify.canteen.CanteensView;
import com.moon.greenify.canteen.FoodLoadingView;
import com.moon.greenify.carbon.QuestionView;
import com.moon.greenify.espM.ESPView;
import com.moon.greenify.espM.WaterLoadingScreen;
import com.moon.greenify.treesAI.TreeMain;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioRecord;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.tensorflow.lite.support.audio.TensorAudio;
import org.tensorflow.lite.support.label.Category;
import org.tensorflow.lite.task.audio.classifier.AudioClassifier;
import org.tensorflow.lite.task.audio.classifier.Classifications;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private final int REQUEST_RECORD_AUDIO = 9976;
    private Button birdIdentifierButton;
    String modelPath = "my_birds_model.tflite";
    float probabilityThreshold = 0.3f;
    AudioClassifier classifier;
    private TensorAudio tensor;
    private AudioRecord record;
    private TimerTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkAudioPermission();
        EditText search = findViewById(R.id.search_edit_text);
        Button carbonButton = findViewById(R.id.carbon_button);
        Intent carbonIntent = new Intent(this, QuestionView.class);
        Button waterGarbageButton = findViewById(R.id.water_garbage_button);
        Intent waterGarbageIntent = new Intent(this, WaterLoadingScreen.class);
        Button treeIdentifier = findViewById(R.id.trees_ai_button);
        Intent treeIdentifierIntent = new Intent(this, TreeMain.class);
        birdIdentifierButton = findViewById(R.id.bird_ai_button);
        Intent birdIdentifierIntent = new Intent(this, BirdMain.class);
        Button canteenAccess = findViewById(R.id.canteen_button);
        Intent canteenAccessIntent = new Intent(this, FoodLoadingView.class);


        carbonButton.setOnClickListener(e->startActivity(carbonIntent));
        waterGarbageButton.setOnClickListener(e->startActivity(waterGarbageIntent));
        treeIdentifier.setOnClickListener(e->startActivity(treeIdentifierIntent));
//        birdIdentifier.setOnClickListener(e->startActivity(birdIdentifierIntent));
        BirdClassification birdClassification = new BirdClassification();
//        birdIdentifierButton.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if(event.getAction() == MotionEvent.ACTION_DOWN) {
//                    onStopRecording();
//                } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                    onStartRecording();
//                }
//
//                return true;
//            }
//        });
        birdIdentifierButton.setOnLongClickListener(e-> {onStartRecording(); return false;});

//        birdIdentifierButton.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if(motionEvent.getAction() == MotionEvent.ACTION_BUTTON_RELEASE) {
//                    onStopRecording();
//                }
//                return true;
//            }
//        });
        canteenAccess.setOnClickListener(e->startActivity(canteenAccessIntent));
    }

    public void onStartRecording() {

        // Loading the model from the assets folder
        try {
            classifier = AudioClassifier.createFromFile(this, modelPath);
        } catch (IOException e) {
            Toast.makeText(this, "Model could not be loaded", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return;
        }

        // Creating an audio recorder
        tensor = classifier.createInputTensorAudio();

        // showing the audio recorder specification
        TensorAudio.TensorAudioFormat format = classifier.getRequiredTensorAudioFormat();
        String specs = "Number of channels: " + format.getChannels() + "\n"
                + "Sample Rate: " + format.getSampleRate();
//        specsTextView.setText(specs);

        // Creating and start recording
        record = classifier.createAudioRecord();
        record.startRecording();

        timerTask = new TimerTask() {
            @Override
            public void run() {
                // Classifying audio data
                // val numberOfSamples = tensor.load(record)
                // val output = classifier.classify(tensor)
                int numberOfSamples = tensor.load(record);
                List<Classifications> output = classifier.classify(tensor);

                // Filtering out classifications with low probability
                List<Category> finalOutput = new ArrayList<>();
                for (Classifications classifications : output) {
                    for (Category category : classifications.getCategories()) {
                        if (category.getScore() > probabilityThreshold) {
                            finalOutput.add(category);
                        }
                    }
                }

                // Sorting the results
                Collections.sort(finalOutput, (o1, o2) -> (int) (o1.getScore() - o2.getScore()));

                // Creating a multiline string with the filtered results
                StringBuilder outputStr = new StringBuilder();
                for (Category category : finalOutput) {
                    outputStr.append(category.getLabel())
                            .append(": ").append(category.getScore()).append("\n");
                }

                // Updating the UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (finalOutput.isEmpty()) {
                            System.out.println("Could not classify");
                        } else {
                            System.out.println(outputStr.toString());
                        }
                    }
                });
            }
        };

        new Timer().scheduleAtFixedRate(timerTask, 1, 500);
    }

    public void onStopRecording() {
        timerTask.cancel();
        record.stop();
    }


    private boolean checkAudioPermission(){
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
        }
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_DENIED;
    }
}