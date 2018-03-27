package com.example.calvin.demo;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

public class DemoActivity extends Activity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        final DocumentView documentView = findViewById(R.id.documentView);
        Button backgroundColorButton = findViewById(R.id.backgroundColorButton);
        final EditText backgroundColorText = findViewById(R.id.backgroundColorText);
        Button widthButton = findViewById(R.id.widthButton);
        final EditText widthText = findViewById(R.id.widthText);
        Button heightButton = findViewById(R.id.heightButton);
        final EditText heightText = findViewById(R.id.heightText);
        Button borderColorButton = findViewById(R.id.borderColorButton);
        final EditText borderColorText = findViewById(R.id.borderColorText);
        Button borderWidthButton = findViewById(R.id.borderWidthButton);
        final EditText borderWidthText = findViewById(R.id.borderWidthText);
        Button decorColorButton = findViewById(R.id.decorColorButton);
        final EditText decorColorText = findViewById(R.id.decorColorText);
        Button decorSizeButton = findViewById(R.id.decorSizeButton);
        final EditText decorSizeText = findViewById(R.id.decorSizeText);
        Button titleColorButton = findViewById(R.id.titleColorButton);
        final EditText titleColorText = findViewById(R.id.titleColorText);
        Button titleButton = findViewById(R.id.titleButton);
        final EditText titleText = findViewById(R.id.titleText);
        Button titleSizeButton = findViewById(R.id.titleSizeButton);
        final EditText titleSizeText = findViewById(R.id.titleSizeText);
        Button subtitleColorButton = findViewById(R.id.subtitleColorButton);
        final EditText subtitleColorText = findViewById(R.id.subtitleColorText);
        Button subtitleButton = findViewById(R.id.subtitleButton);
        final EditText subtitleText = findViewById(R.id.subtitleText);
        Button subtitleSizeButton = findViewById(R.id.subtitleSizeButton);
        final EditText subtitleSizeText = findViewById(R.id.subtitleSizeText);

        backgroundColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String colorStr = backgroundColorText.getText().toString();
                    documentView.setBackgroundColor(Color.parseColor(colorStr));
                }catch (Exception e) {

                }
            }
        });

        widthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String widthStr = widthText.getText().toString();
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) documentView.getLayoutParams();
                    if (widthStr.compareToIgnoreCase("wrap") == 0) {
                        params.width = FrameLayout.LayoutParams.WRAP_CONTENT;
                    }else if (widthStr.compareToIgnoreCase("match") == 0) {
                        params.width = FrameLayout.LayoutParams.MATCH_PARENT;
                    }else {
                        params.width = Integer.valueOf(widthStr);
                    }
                    documentView.setLayoutParams(params);
                }catch (Exception e) {

                }
            }
        });

        heightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String heightStr = heightText.getText().toString();
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) documentView.getLayoutParams();
                    if (heightStr.compareToIgnoreCase("wrap") == 0) {
                        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
                    }else if (heightStr.compareToIgnoreCase("match") == 0) {
                        params.height = FrameLayout.LayoutParams.MATCH_PARENT;
                    }else {
                        params.height = Integer.valueOf(heightStr);
                    }
                    documentView.setLayoutParams(params);
                }catch (Exception e) {

                }
            }
        });

        borderColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String colorStr = borderColorText.getText().toString();
                    documentView.setBorderColor(Color.parseColor(colorStr));
                }catch (Exception e) {

                }
            }
        });

        borderWidthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String widthStr = borderWidthText.getText().toString();
                    documentView.setBorderWidth(Float.valueOf(widthStr));
                }catch (Exception e) {

                }
            }
        });

        decorColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String colorStr = decorColorText.getText().toString();
                    documentView.setDecorColor(Color.parseColor(colorStr));
                }catch (Exception e) {

                }
            }
        });

        decorSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String sizeStr = decorSizeText.getText().toString();
                    documentView.setDecorSize(Float.valueOf(sizeStr));
                }catch (Exception e) {

                }
            }
        });

        titleColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String colorStr = titleColorText.getText().toString();
                    documentView.setTitleColor(Color.parseColor(colorStr));
                }catch (Exception e) {

                }
            }
        });

        titleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String titleStr = titleText.getText().toString();
                    documentView.setTitle(titleStr);
                }catch (Exception e) {

                }
            }
        });

        titleSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String sizeStr = titleSizeText.getText().toString();
                    documentView.setTitleSize(Float.valueOf(sizeStr));
                }catch (Exception e) {

                }
            }
        });

        subtitleColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String colorStr = subtitleColorText.getText().toString();
                    documentView.setSubtitleColor(Color.parseColor(colorStr));
                }catch (Exception e) {

                }
            }
        });

        subtitleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String titleStr = subtitleText.getText().toString();
                    documentView.setSubtitle(titleStr);
                }catch (Exception e) {

                }
            }
        });

        subtitleSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String sizeStr = subtitleSizeText.getText().toString();
                    documentView.setSubtitleSize(Float.valueOf(sizeStr));
                }catch (Exception e) {

                }
            }
        });
    }
}
