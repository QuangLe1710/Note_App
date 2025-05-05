package vn.haui.a250504_review;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditNote extends AppCompatActivity {
private TextView tvId;
private EditText edtTitle, edtContent;
private Button btnUpdate, btnClose;
Note objNote;
Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        myMapping();
        intent=getIntent();
        objNote= (Note) intent.getSerializableExtra("objNote");
        tvId.setText(""+objNote.getId());
        edtTitle.setText(objNote.getTitle());
        edtContent.setText(objNote.getContent());

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtTitle.getText().toString().trim().length()<3||edtContent.getText().toString().length()<3){
                    Toast.makeText(EditNote.this, "Du lieu nhap khong hop le", Toast.LENGTH_SHORT).show();
                }else{
                    objNote.setTitle(edtTitle.getText().toString());
                    objNote.setContent(edtContent.getText().toString());
                    intent.putExtra("objNoteUpdate",objNote);
                    setResult(888,intent);
                    finish();
                }
            }
        });
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void myMapping() {
        tvId=findViewById(R.id.tv_id_edit);
        edtTitle=findViewById(R.id.edt_title_edit);
        edtContent=findViewById(R.id.edt_content_edit);
        btnUpdate=findViewById(R.id.btn_update_edit);
        btnClose=findViewById(R.id.btn_close_edit);

    }
}