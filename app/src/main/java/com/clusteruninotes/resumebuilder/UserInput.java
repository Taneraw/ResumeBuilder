package com.clusteruninotes.resumebuilder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class UserInput extends AppCompatActivity implements View.OnClickListener{
    EditText name,contact,email,institute,percentage,branch,skills,pTitle,pDescription;
    String Name,Contact,Email,Institute,Percentage,Branch,Skills,PTitle,PDescription;
    Button backBtn,nextBtn;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_input);
        name = findViewById(R.id.et_name);
        contact = findViewById(R.id.et_contact);
        email = findViewById(R.id.et_email);
        institute = findViewById(R.id.et_college);
        percentage = findViewById(R.id.et_percentage);
        branch = findViewById(R.id.et_branch);
        skills = findViewById(R.id.et_skill);
        pTitle = findViewById(R.id.et_project_title);
        pDescription = findViewById(R.id.et_project_description);

        backBtn = findViewById(R.id.buttonBack);
        nextBtn = findViewById(R.id.buttonNext);
        backBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);

        id=0;
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            id=bundle.getInt("ID");
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonBack:
                finish();
                break;
            case R.id.buttonNext:
                CheckInputField();
                break;
        }
    }

    private void CheckInputField() {
        Name = name.getText().toString().trim();
        Contact = contact.getText().toString().trim();
        Email = email.getText().toString().trim();
        Institute = institute.getText().toString().trim();
        Percentage = percentage.getText().toString().trim();
        Branch = branch.getText().toString().trim();
        Skills = skills.getText().toString().trim();
        PTitle = pTitle.getText().toString().trim();
        PDescription = pDescription.getText().toString().trim();

        if(TextUtils.isEmpty(Name)||TextUtils.isEmpty(Contact)
        || TextUtils.isEmpty(Email)||TextUtils.isEmpty(Institute)||TextUtils.isEmpty(Percentage)
        || TextUtils.isEmpty(Branch)||TextUtils.isEmpty(Skills)||TextUtils.isEmpty(PTitle)||TextUtils.isEmpty(PDescription))
            Toast.makeText(this, "field can't be empty", Toast.LENGTH_SHORT).show();
        else{
            try {
                createPdf();
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void createPdf() throws FileNotFoundException, DocumentException {
        File appFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),"RESUME");
        if (!appFolder.exists())
            appFolder.mkdir();
        Date date = new Date();
        String fileName= new SimpleDateFormat("ddMMyyyyHHmmss", Locale.getDefault()).format(date);
        File pdfFile = new File(appFolder+"/"+fileName+".pdf");

        OutputStream outputStream = new FileOutputStream(pdfFile);
        Document document = new Document();
        PdfWriter.getInstance(document,outputStream);
        document.open();

        document.setPageSize(PageSize.A4);
        document.addCreationDate();
        document.addAuthor("SimpleMad");
        document.addTitle("Resume");

        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0,0,0,68));
        try {
           // BaseFont baseFont = BaseFont.createFont("assets/fonts/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED);
            Font heading = new Font(Font.FontFamily.TIMES_ROMAN,14,Font.BOLD);
            Font bold = new Font(Font.FontFamily.TIMES_ROMAN,12,Font.BOLD);
            Font normal = new Font(Font.FontFamily.TIMES_ROMAN,12,Font.NORMAL);

            Paragraph blank = new Paragraph("\n");
            Paragraph mDetails = new Paragraph();
            Phrase mDetailsName = new Phrase(Name.toUpperCase(),bold);
            Phrase mDetailsPhrase = new Phrase("\n"+Email.toLowerCase()+"\n"+Contact+"\n",normal);
            mDetails.add(mDetailsName);
            mDetails.add(mDetailsPhrase);

            Paragraph EducationDetails = new Paragraph();
            Phrase EducationDetailsHeading = new Phrase("EDUCATION",heading);
            Phrase EducationDetailsPhrase = new Phrase("\n"+Institute.toUpperCase()
                    +"\n"+Branch.toUpperCase()+"\n"+"Percentage : "+Percentage+"%",normal);

            EducationDetails.add(EducationDetailsHeading);
            EducationDetails.add(EducationDetailsPhrase);

            Paragraph mSkillsPara = new Paragraph();
            Phrase mSkills = new Phrase("SKILLS  : ",heading);
            Phrase mSkillsPhrase = new Phrase(String.format("%20s",Skills),normal);

            mSkillsPara.add(mSkills);
            mSkillsPara.add(mSkillsPhrase);

            Paragraph Project = new Paragraph();
            Phrase mProject = new Phrase("PROJECT",heading);
            Phrase mProjectTitle = new Phrase("\n"+PTitle,bold);
            Phrase mProjectDesc = new Phrase("\n"+PDescription,normal);

            Project.add(mProject);
            Project.add(mProjectTitle);
            Project.add(mProjectDesc);

            if(id==1){
                mDetails.setAlignment(Element.ALIGN_LEFT);
            }else if(id==2){
                mDetails.setAlignment(Element.ALIGN_CENTER);
            }
            document.add(mDetails);
            document.add(blank);
            document.add(lineSeparator);
            document.add(blank);

            document.add(EducationDetails);
            document.add(blank);
            document.add(lineSeparator);
            document.add(blank);

            document.add(mSkillsPara);
            document.add(blank);
            document.add(lineSeparator);
            document.add(blank);

            document.add(Project);
            document.add(blank);

        }catch(Exception e) {
            e.printStackTrace();
        }
        document.close();
        viewPdf(pdfFile);
    }

    private void viewPdf(File pdfFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(FileProvider.getUriForFile(UserInput.this,
                BuildConfig.APPLICATION_ID+".provider",
                pdfFile
        ),"application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(intent);
    }

}
