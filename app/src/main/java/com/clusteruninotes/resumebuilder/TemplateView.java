package com.clusteruninotes.resumebuilder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class TemplateView extends AppCompatActivity implements View.OnClickListener{
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_view);

        ImageView templateView = findViewById(R.id.templateView);
        Button backBtn = findViewById(R.id.buttonBack);
        Button nextBtn = findViewById(R.id.buttonNext);
        backBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        id=0;
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            id=bundle.getInt("ID");
        }
        if(id==1)
            templateView.setImageResource(R.drawable.template1);
        else if(id==2)
            templateView.setImageResource(R.drawable.template2);
        else
            templateView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonBack:
                finish();
                break;
            case R.id.buttonNext:
                Intent intent = new Intent(this,UserInput.class);
                intent.putExtra("ID",id);
                startActivity(intent);
                finish();
                break;
        }
    }
}
