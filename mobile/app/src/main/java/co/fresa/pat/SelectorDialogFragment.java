package co.fresa.pat;

import android.app.Instrumentation;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import co.fresa.pat.mundo.UserLogic;

public class SelectorDialogFragment extends DialogFragment implements View.OnClickListener, DialogInterface {
    int layer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.selector_dialog, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        layer = getArguments().getInt("layer");
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onStart() {
        ImageButton[] imageButtons = new ImageButton[6];
        imageButtons[0] = getView().findViewById(R.id.selectorButton1);
        imageButtons[1] = getView().findViewById(R.id.selectorButton2);
        imageButtons[2] = getView().findViewById(R.id.selectorButton3);
        imageButtons[3] = getView().findViewById(R.id.selectorButton4);
        imageButtons[4] = getView().findViewById(R.id.selectorButton5);
        imageButtons[5] = getView().findViewById(R.id.selectorButton6);

        int spriteId;
        Bitmap bitmap;
        String packageName = getActivity().getPackageName();

        for (int i = 0; i < 6; i++) {
            spriteId = getResources().getIdentifier("layer" + layer + "basesprite" + (i + 1), "drawable", packageName);
            imageButtons[i].setOnClickListener(this);
            imageButtons[i].setId(i);

            if (layer < 3) {
                imageButtons[i].setImageDrawable(getResources().getDrawable(spriteId));
            } else if (layer == 3) {
                bitmap = BitmapFactory.decodeResource(getResources(), spriteId);
                bitmap = Bitmap.createBitmap(bitmap, 0, bitmap.getHeight()/2, bitmap.getWidth(), bitmap.getHeight()-bitmap.getHeight()/2);
                imageButtons[i].setImageBitmap(bitmap);
            } else {
                bitmap = BitmapFactory.decodeResource(getResources(), spriteId);
                bitmap = Bitmap.createBitmap(bitmap, 0,0, bitmap.getWidth(), bitmap.getHeight()/4);
                imageButtons[i].setImageBitmap(bitmap);
            }
        }
        super.onStart();
    }

    @Override
    public void onClick(View view) {
        UserLogic.getInstance().setAvatarInfo(layer-1,(int)view.getId()+1);
        getTargetFragment().onActivityResult(322, 0,null);
        dismiss();
    }

    @Override
    public void cancel() {

    }
}