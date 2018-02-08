package com.uix.roryfielding.paintx;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import java.util.UUID;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{

    private DrawingView drawView; //declare view
    private ImageButton currPaint, drawBtn, eraseBtn, newBtn, saveBtn; //declare buttons
    private float smallBrush, mediumBrush, largeBrush; //declare brush sizes
    private View.OnClickListener brushclick, eraseclick, newclick, saveclick; //declare listeners


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Setup(); //call set up
    }

    public void Setup(){

        drawView = (DrawingView)findViewById(R.id.drawing);
        drawBtn = (ImageButton)findViewById(R.id.draw_btn);
        eraseBtn = (ImageButton)findViewById(R.id.erase_btn);
        newBtn = (ImageButton) findViewById(R.id.new_btn);
        saveBtn = (ImageButton) findViewById(R.id.save_btn); //assign buttons in context

        LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors); //assign layout
        currPaint = (ImageButton)paintLayout.getChildAt(0);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        drawView.setBrushSize(mediumBrush); //initialise brush size

        brushclick = new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId()==R.id.draw_btn){
                    //draw button clicked
                }
                final Dialog brushDialog = new Dialog(drawView.getContext());
                brushDialog.setTitle("Brush size:");
                brushDialog.setContentView(R.layout.brush_chooser);

                ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
                smallBtn.setOnClickListener(new OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        drawView.setBrushSize(smallBrush);
                        drawView.setLastBrushSize(smallBrush);
                        drawView.setErase(false);
                        brushDialog.dismiss();
                    }
                });

                ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
                mediumBtn.setOnClickListener(new OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        drawView.setBrushSize(mediumBrush);
                        drawView.setLastBrushSize(mediumBrush);
                        drawView.setErase(false);
                        brushDialog.dismiss();
                    }
                });

                ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
                largeBtn.setOnClickListener(new OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        drawView.setBrushSize(largeBrush);
                        drawView.setLastBrushSize(largeBrush);
                        drawView.setErase(false);
                        brushDialog.dismiss();
                    }
                });
                brushDialog.show();
            }
        };
        drawBtn.setOnClickListener(brushclick);

        //new listener for eraser

        eraseclick = new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.erase_btn) {
                    //switch to erase - choose size
                }
                final Dialog brushDialog = new Dialog(drawView.getContext()); //declare dialog screen
                brushDialog.setTitle("Eraser size:");
                brushDialog.setContentView(R.layout.brush_chooser); //change layout to select brush

                //depending on brush size selected, switch to this size and set eraser to true

                ImageButton smallBtn = (ImageButton) brushDialog.findViewById(R.id.small_brush);
                smallBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawView.setErase(true);
                        drawView.setBrushSize(smallBrush);
                        brushDialog.dismiss();
                    }
                });
                ImageButton mediumBtn = (ImageButton) brushDialog.findViewById(R.id.medium_brush);
                mediumBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawView.setErase(true);
                        drawView.setBrushSize(mediumBrush);
                        brushDialog.dismiss();
                    }
                });
                ImageButton largeBtn = (ImageButton) brushDialog.findViewById(R.id.large_brush);
                largeBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawView.setErase(true);
                        drawView.setBrushSize(largeBrush);
                        brushDialog.dismiss();
                    }
                });
                brushDialog.show();
            }
        };

        eraseBtn.setOnClickListener(eraseclick);

        //new listener to create new canvas

        newclick = new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId()==R.id.new_btn){
                    //new button
                    AlertDialog.Builder newDialog = new AlertDialog.Builder(drawView.getContext());
                    newDialog.setTitle("New drawing");
                    newDialog.setMessage("Start new drawing (you will lose the current progress)?");
                    newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which){
                            drawView.startNew();
                            dialog.dismiss();
                        }
                    });
                    newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which){
                            dialog.cancel();
                        }
                    });
                    newDialog.show();
                }

            }
        };

        newBtn.setOnClickListener(newclick);

        //new listener to save the graphic

        saveclick = new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId()==R.id.save_btn){
                    //save drawing
                    AlertDialog.Builder saveDialog = new AlertDialog.Builder(drawView.getContext());
                    saveDialog.setTitle("Save drawing");
                    saveDialog.setMessage("Save drawing to device Gallery?");
                    saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which){
                            //save drawing
                            drawView.setDrawingCacheEnabled(true);
                            String imgSaved = MediaStore.Images.Media.insertImage(
                                    getContentResolver(), drawView.getDrawingCache(),
                                    UUID.randomUUID().toString()+".png", "drawing");
                            if(imgSaved!=null){
                                Toast savedToast = Toast.makeText(getApplicationContext(),
                                        "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                                savedToast.show();
                            }
                            else{
                                Toast unsavedToast = Toast.makeText(getApplicationContext(),
                                        "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                                unsavedToast.show();
                            }
                            drawView.destroyDrawingCache();
                        }
                    });
                    saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which){
                            dialog.cancel();
                        }
                    });
                    saveDialog.show();
                }

            }
        };

        saveBtn.setOnClickListener(saveclick);

    }

    //function to update the colour of the paint depending on the palette selected

    public void paintClicked(View view){
        drawView.setErase(false);
        drawView.setBrushSize(drawView.getLastBrushSize());
        //use chosen color
        if(view!=currPaint){
        //update color
            ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();
            drawView.setColor(color);
            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint=(ImageButton)view;
        }
    }


}
