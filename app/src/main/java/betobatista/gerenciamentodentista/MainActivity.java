package betobatista.gerenciamentodentista;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String NAME = "name";
    public static final String CLASS = "class";

    private TextView txtName;
    private TextView txtClass;

    private DocumentReference reference = FirebaseFirestore.getInstance().document("sampleData/listClass");

    @Override
    protected void onStart() {
        super.onStart();
        reference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    String nomeText = documentSnapshot.getString(NAME);
                    String classText = documentSnapshot.getString(CLASS);
                    txtName.setText(nomeText);
                    txtClass.setText(classText);
                } else if (e != null) {
                    Toast.makeText(getApplicationContext(), "Erro: " + e, Toast.LENGTH_LONG ).show();
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtName = findViewById(R.id.txtName);
        txtClass = findViewById(R.id.txtClass);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    public void saveData(View view) {
        EditText edtName = findViewById(R.id.edtName);
        EditText edtClass = findViewById(R.id.edtClass);
        String nameText = edtName.getText().toString();
        String classText =  edtClass.getText().toString();

        if(nameText.isEmpty() || classText.isEmpty()) {return;}
        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put(NAME, nameText);
        dataToSave.put(CLASS, classText);
        reference.set(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Sucesso", Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Falha", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void searchData(View view) {
        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

            }
        });
    }
}
