package rmz.ml.catsndogs.ml;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;

import rmz.ml.catsndogs.ml.CatsAndDogs;

public class ModelManager {
    private Context _context;
    public ModelManager(Context context){
        _context = context;
    }
    public String Predict(Bitmap image){
        String result;
        try {
            CatsAndDogs model = CatsAndDogs.newInstance(_context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 80, 80, 1}, DataType.FLOAT32);

            image = Bitmap.createScaledBitmap(image, 80, 80, true);
            TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
            tensorImage.load(ToCatsDogsModel(image));

            inputFeature0.loadBuffer(tensorImage.getBuffer());

            // Runs model inference and gets result.
            CatsAndDogs.Outputs outputs = model.process(inputFeature0);

            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            result = GetMax(outputFeature0.getFloatArray()) + "";

            // Releases model resources if no longer used.
            model.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "Ha fallao, lo unico que se es que es una foto xd\n" + e.getMessage() + "\n" + e.getStackTrace();
        }
    }

    public Bitmap ToCatsDogsModel(Bitmap original){
        int size = 80;
        Bitmap grayScaleBitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(grayScaleBitmap);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(original, 0, 0, paint);
        return grayScaleBitmap;
    }

    private int GetMax(float[] arr){
        int max = 0;

        for (int i = 0; i < arr.length; i++){
            if (arr[i] > arr[max]) max = i;
        }
        return max;
    }
}
