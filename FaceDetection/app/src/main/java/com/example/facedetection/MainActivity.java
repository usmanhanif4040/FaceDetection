package com.example.facedetection;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.media.FaceDetector;
import android.media.MediaRouter2;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;

import java.io.FileDescriptor;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final int READ_CODE_REQUEST =42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button LoadPicButton = findViewById(R.id.button);
        loadPicButton.setonClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                startActivity(intent, READ_CODE_REQUEST);
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == READ_CODE_REQUEST && resultCode== Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData !=null) {
                uri = resultData.getData();
                try{
                    getBitmapFromUriFromUri(uri);

                }catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void getBitmapFromUriFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        showImage(fileDescriptor);
    }

    public void showImage(FileDescriptor fileDescriptor) {
        //load image
        ImageView imageView = findViewById(R.id.imgview);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;

        //create paint object to draw a square
        Bitmap myBitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        Paint myREctPaint = new Paint();
        myREctPaint.setStrokewith(5);
        myREctPaint.setColor(Color.RED);
        myREctPaint.setStyle(Paint.style.STROKE);

        //create canvas to draw rectangle
        Bitmap tempBitmap = Bitmap.createBitmap(myBitmap.getWidth();
        myBitmap.getHeight(), Bitmap.Config.RGB_565);
        Canvas tempCanvas = new Canvas(tempBitmap);
        tempBitmap.drawBitmap(myBitmap, 0, 0, null);

        //create face detection
        FaceDetector faceDetector = new
                FaceDetector.Builder(getApplicationContext()).setTrackingEnabled(false).build();
        if (!faceDetector.isoperational()) {
            new AlertDialog.Builder(getApplicationContext()).setMassage("could not set up the file");
            return;
        }

        //detect faces
        Frame frame = new frame.builder() getBitmap(myBitmap).build();
        SparseArray<FaceDetector.Face> faces = faceDetector.detect(frame);
        for (int i = 0; i < faces.size(); i++) {
            Face thisFace = faces.valueAt(i);
            float y1 = thisFace.getposition().y;
            float x2 = x1 + getwitdth();
            float y2 = y1 + thisFace.getHeight();
            tempCanvas.drawRoundRect(new RectF(x1, y1, x2, y2), 2, 2, myRectPaint);
        }
        myImageView.setImageDrawable(new BitmapDrawable(getResources(), tempBitmap));
    }
    }
}
