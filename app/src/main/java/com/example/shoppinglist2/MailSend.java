package com.example.shoppinglist2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MailSend extends AppCompatActivity {

    EditText sentAdress;
    EditText header;
    EditText body;
    Button button_sent, attachment;
    TextView txtAttachment;
    Uri URI =null;
    private static final int PICK_FROM_GALLERY =101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_send);
        defineVaribales();
        defineListeners();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_FROM_GALLERY && resultCode==RESULT_OK){
            URI =data.getData();
            txtAttachment.setText(URI.getLastPathSegment());
            txtAttachment.setVisibility(View.VISIBLE);
        }
    }
    public void defineVaribales(){
        sentAdress =findViewById(R.id.sentAdress);
        header = findViewById(R.id.header);
        body = findViewById(R.id.body);
        button_sent = findViewById(R.id.button_sent);
        attachment = findViewById(R.id.attachment);
        txtAttachment = findViewById(R.id.tvAttachment);

    }
    public void defineListeners(){
        button_sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");

                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{sentAdress.getText().toString()});
                intent.putExtra(Intent.EXTRA_SUBJECT, header.getText().toString());
                intent.putExtra(Intent.EXTRA_TEXT, body.getText().toString());

                if (URI != null ){
                    intent.putExtra(Intent.EXTRA_STREAM, URI);
                }
                if(intent.resolveActivity(getPackageManager()) != null){
                    startActivity(Intent.createChooser(intent,"Send E-mail"));
                }
            }
        });
        attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("txt/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.putExtra("return-data",true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"),PICK_FROM_GALLERY);
            }
        });
    }
}
