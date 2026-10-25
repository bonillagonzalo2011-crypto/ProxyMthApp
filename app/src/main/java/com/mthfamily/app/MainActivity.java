package com.mthfamily.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences prefs;
    private String selectedMode = "hs_pescoco_alto";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("MthSettings", MODE_PRIVATE);

        // Copiar el script desde assets a la memoria interna al iniciar la app
        extractScriptToInternalStorage();

        Button btnLaunch = findViewById(R.id.btn_launch);
        View cardPescocoAlto = findViewById(R.id.card_hs_pescoco_alto);

        cardPescocoAlto.setOnClickListener(v -> {
            selectedMode = "hs_pescoco_alto";
            Toast.makeText(this, "Modo seleccionado: HS Pescoço + Alto", Toast.LENGTH_SHORT).show();
        });

        btnLaunch.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("proxy_mode", selectedMode);
            editor.putBoolean("proxy_active", true);
            editor.apply();

            Toast.makeText(this, "PROXY MTH: Script aplicado, abriendo juego...", Toast.LENGTH_SHORT).show();

            // Abrir Free Fire (Normal o MAX)
            Intent launchIntent = getPackageManager().getLaunchIntentForPackage("com.dts.freefireth");
            if (launchIntent == null) {
                launchIntent = getPackageManager().getLaunchIntentForPackage("com.dts.freefiremax");
            }

            if (launchIntent != null) {
                startActivity(launchIntent);
            } else {
                Toast.makeText(this, "Free Fire no está instalado", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void extractScriptToInternalStorage() {
        try {
            File outFile = new File(getFilesDir(), "mth_script.lua");
            InputStream inputStream = getAssets().open("mth_script.lua");
            FileOutputStream outputStream = new FileOutputStream(outFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }
            inputStream.close();
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
