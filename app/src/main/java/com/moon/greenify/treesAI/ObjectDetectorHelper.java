//package com.moon.greenify.treesAI;
//import org.tensorflow.lite.gpu.CompatibilityList;
//import org.tensorflow.lite.support.image.ImageProcessor;
//import org.tensorflow.lite.support.image.TensorImage;
//import org.tensorflow.lite.support.image.ops.Rot90Op;
//import org.tensorflow.lite.task.core.BaseOptions;
//import org.tensorflow.lite.task.vision.detector.Detection;
//import org.tensorflow.lite.task.vision.detector.ObjectDetector;
//
//import android.content.Context;
//import android.graphics.Bitmap;
////import android.graphics.BitmapFactory;
////import android.widget.ImageView;
////
////import android.content.Context;
////import android.graphics.Bitmap;
////import android.os.SystemClock;
////import android.util.Log;
////import org.tensorflow.lite.gpu.CompatibilityList;
////import org.tensorflow.lite.support.image.ImageProcessor;
////import org.tensorflow.lite.support.image.TensorImage;
////import org.tensorflow.lite.support.image.ops.Rot90Op;
////import org.tensorflow.lite.task.core.BaseOptions;
////import org.tensorflow.lite.task.vision.detector.Detection;
////import org.tensorflow.lite.task.vision.detector.ObjectDetector;
////
////import androidx.appcompat.resources.Compatibility;
//
//import java.io.IOException;
//import java.util.List;
//
//public class ObjectDetectorHelper {
//    ObjectDetectorHelper(Context context){
//        this.context = context;
//    }
//    Context context;
//    Float threshold = 0.5f;
//    int maxResults = 3;
//    int threads = 2;
//    String modelName = "";
//    ObjectDetector objectDetector;
//    List<Detection> result;
//    DetectorListener objectDetectorListener;
//    private void setupObjectDetector(){
//        ObjectDetector.ObjectDetectorOptions.Builder options = ObjectDetector.ObjectDetectorOptions.builder()
//                .setScoreThreshold(threshold)
//                .setMaxResults(3);
//
//        BaseOptions.Builder baseOptionsBuilder = BaseOptions.builder().setNumThreads(2).useGpu();
//        modelName = "converted_model.tflite";
//
//        try{
//            objectDetector = ObjectDetector.createFromFileAndOptions(context, modelName, options.build());
//        } catch (IOException e) {
//            System.out.println("Object Detector Failed to Load");
//            e.printStackTrace();
//        }
//    }
//    public List<Detection> detect(Bitmap bitmap, int imageRotation){
//        if(objectDetector == null){
//            setupObjectDetector();
//        }
//
//        ImageProcessor imageProcessor = new ImageProcessor.Builder()
//                .add(new Rot90Op(-imageRotation/90))
//                .build();
//
//        TensorImage tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmap));
//
//        result= objectDetector.detect(tensorImage);
//        return result;
//    }
//
//    interface DetectorListener{
//        void onError(String error);
//        void onResults(List<Detection> result);
//    }
//}
