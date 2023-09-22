package com.bpdev.amazonrekognition;

import static com.bpdev.amazonrekognition.config.Config.AWS_ACCESS_KEY_ID;
import static com.bpdev.amazonrekognition.config.Config.AWS_SECRET_ACCESS_KEY;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import com.amazonaws.services.rekognition.model.BoundingBox;
import com.amazonaws.services.rekognition.model.CompareFacesMatch;
import com.amazonaws.services.rekognition.model.CompareFacesRequest;
import com.amazonaws.services.rekognition.model.CompareFacesResult;
import com.amazonaws.services.rekognition.model.ComparedFace;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.util.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;


public class RekognitionActivity extends AppCompatActivity {

    ImageView imageView1;
    ImageView imageView2;

    Button button3;

    TextView textView1;

    String sourceUri;
    String targetUri;

    Uri filePath;

    String percent;

    AmazonRekognition rekognitionClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekognition);

        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        button3 = findViewById(R.id.button3);
        textView1 = findViewById(R.id.textView1);

        Drawable drawable = getResources().getDrawable(
                R.drawable.baseline_image_search_24);
        ImageView imageView1 = (ImageView) findViewById(R.id.imageView1);
        ImageView imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView1.setImageDrawable(drawable);
        imageView2.setImageDrawable(drawable);

        if (ContextCompat.checkSelfPermission(RekognitionActivity.this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            return;
        }

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setType("image/*");
                startActivityForResult(intent, 1000);
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent();
                intent1.setAction(Intent.ACTION_PICK);
                intent1.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent1.setType("image/*");
                startActivityForResult(intent1, 1001);

            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageView1 != null || imageView2 != null){
                    Log.i("확인해보자", "imageView1 != null || imageView2 != null");
                    compareFaces();

                }else {
                    Toast.makeText(RekognitionActivity.this,"이미지를 선택해주세요.",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000 && data != null && data.getData() != null){
            filePath = data.getData();
            sourceUri = GetUrI(filePath);
            imageView1.setImageURI(filePath);

            Log.i("확인해보자", "sourceUri : "+sourceUri +" / sourceUri : "+sourceUri);

        } else if (requestCode == 1001 && data != null && data.getData() != null) {
            filePath = data.getData();
            targetUri = GetUrI(filePath);
            imageView2.setImageURI(filePath);

            Log.i("확인해보자", "targetUri : "+targetUri +" / targetJpeg : ");
        }
    }
    private String GetUrI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }


    public void compareFaces() {

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Float similarityThreshold = 70F;

        ByteBuffer sourceImageBytes = null;
        ByteBuffer targetImageBytes = null;

        Log.i("확인해보자", "SSS1111");

        rekognitionClient = new AmazonRekognitionClient(new BasicAWSCredentials(AWS_ACCESS_KEY_ID, AWS_SECRET_ACCESS_KEY));

        //Load source and target images and create input parameters
        try (InputStream inputStream = new FileInputStream(new File(sourceUri))) {
            sourceImageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
        }
        catch(Exception e)
        {
            Log.i("확인해보자", "Failed to load source image " + sourceUri);
                    System.exit(1);
        }
        try (InputStream inputStream = new FileInputStream(new File(targetUri))) {
            targetImageBytes = ByteBuffer.wrap(IOUtils.toByteArray(inputStream));
        }
        catch(Exception e)
        {
            Log.i("확인해보자", "Failed to load target images: " + targetUri);
            System.exit(1);
        }

        Image source=new Image()
                .withBytes(sourceImageBytes);
        Image target=new Image()
                .withBytes(targetImageBytes);

        CompareFacesRequest request = new CompareFacesRequest()
                .withSourceImage(source)
                .withTargetImage(target)
                .withSimilarityThreshold(similarityThreshold);

        // Call operation
        CompareFacesResult compareFacesResult = rekognitionClient.compareFaces(request);

        // Display results
        List <CompareFacesMatch> faceDetails = compareFacesResult.getFaceMatches();
        for (CompareFacesMatch match: faceDetails){
            ComparedFace face= match.getFace();
            BoundingBox position = face.getBoundingBox();
            Log.i("확인해보자", position.getTop()
                    + " " + position.getLeft().toString()
                    + "얼굴을 비교하였을 때 " + face.getConfidence().toString()
                    + "%의 확률로 일치합니다.");
            textView1.setText("2명의 얼굴을 비교하였을 때, "
                    + face.getConfidence().toString()
                    + "%의 확률로 일치합니다.");
            percent = face.getConfidence().toString();
            return;
        }
        List<ComparedFace> uncompared = compareFacesResult.getUnmatchedFaces();
        textView1.setText("얼굴이 일치하지 않습니다.");
        Log.i("확인해보자", "uncompared : " + uncompared);


    }

}