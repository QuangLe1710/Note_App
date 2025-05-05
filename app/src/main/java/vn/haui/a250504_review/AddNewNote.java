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

public class AddNewNote extends AppCompatActivity {
    TextView tvId;
    EditText edtTitle, edtContent;
    Button btnSave, btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_new);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        myMapping();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                if ((edtTitle.getText().toString().trim().length() < 3) || (edtContent.getText().toString().trim().length() < 3)) {
                    Toast.makeText(AddNewNote.this, "Du lieu nhap phai lon hon 2 ky tu", Toast.LENGTH_SHORT).show();
                } else {
                    Note objNote = new Note(0, edtTitle.getText().toString(), edtContent.getText().toString());
                    intent.putExtra("objNewNote", objNote);
                    setResult(999, intent);
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
        tvId = findViewById(R.id.tv_id);
        edtTitle = findViewById(R.id.edt_title);
        edtContent = findViewById(R.id.edt_content);
        btnSave = findViewById(R.id.btn_save);
        btnClose = findViewById(R.id.btn_close);
    }
}