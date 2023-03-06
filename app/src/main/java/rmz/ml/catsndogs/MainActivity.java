package rmz.ml.catsndogs;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import rmz.ml.catsndogs.ml.ModelManager;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 22;
    Button Capture;
    TextView Result;
    ImageView ImageForPrediction;

    ModelManager MlManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InitializeComponents();
    }

    public void InitializeComponents(){
        Capture = findViewById(R.id.CaptureButton);
        Capture.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View view) {
                Result.setText("Esperando imagen");

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, REQUEST_CODE);
            }
        });

        Result = findViewById(R.id.Result);
        ImageForPrediction = findViewById(R.id.ImageForPrediction);
        MlManager = new ModelManager(MainActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            Bitmap photo = (Bitmap)data.getExtras().get("data");

            ImageForPrediction.setImageBitmap(photo);
            Result.setText(MlManager.Predict(photo));
        }
        else{
            Toast.makeText(this, "No se pudo :(", Toast.LENGTH_SHORT).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}