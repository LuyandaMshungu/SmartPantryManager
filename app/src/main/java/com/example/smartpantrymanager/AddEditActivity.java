package com.example.smartpantrymanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class AddEditActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_ID = "item_id";
    private static final String[] UNITS = {"g", "kg", "ml", "l", "pcs"};

    private DatabaseHelper db;
    private EditText editName, editQuantity, editExpiry;
    private Spinner spinnerUnit;
    private int itemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_edit);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.addEditRoot), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        db = new DatabaseHelper(this);

        editName = findViewById(R.id.editName);
        editQuantity = findViewById(R.id.editQuantity);
        editExpiry = findViewById(R.id.editExpiry);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        TextView textTitle = findViewById(R.id.textTitle);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnCancel = findViewById(R.id.btnCancel);
        Button btnClearDate = findViewById(R.id.btnClearDate);

        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item, UNITS);
        spinnerUnit.setAdapter(unitAdapter);

        itemId = getIntent().getIntExtra(EXTRA_ITEM_ID, -1);
        if (itemId != -1) {
            PantryItem existing = db.getPantryItem(itemId);
            if (existing != null) {
                textTitle.setText("Edit Ingredient");
                editName.setText(existing.getName());

                double q = existing.getQuantity();
                editQuantity.setText(q == Math.floor(q) ? String.valueOf((long) q) : String.valueOf(q));

                int unitIndex = Arrays.asList(UNITS).indexOf(existing.getUnit());
                if (unitIndex >= 0) {
                    spinnerUnit.setSelection(unitIndex);
                }
                if (existing.getExpiryDate() != null) {
                    editExpiry.setText(existing.getExpiryDate());
                }
            } else {
                itemId = -1;
            }
        }

        editExpiry.setOnClickListener(v -> showDatePicker());
        btnClearDate.setOnClickListener(v -> editExpiry.setText(""));
        btnSave.setOnClickListener(v -> saveItem());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) -> {
            String date = String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, day);
            editExpiry.setText(date);
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void saveItem() {
        String name = editName.getText().toString().trim();
        String qtyText = editQuantity.getText().toString().trim();

        if (name.isEmpty()) {
            editName.setError("Enter an ingredient name");
            editName.requestFocus();
            return;
        }

        double quantity;
        try {
            quantity = Double.parseDouble(qtyText);
        } catch (NumberFormatException e) {
            editQuantity.setError("Enter a valid number");
            editQuantity.requestFocus();
            return;
        }
        if (quantity <= 0) {
            editQuantity.setError("Quantity must be more than 0");
            editQuantity.requestFocus();
            return;
        }

        String unit = (String) spinnerUnit.getSelectedItem();
        String expiry = editExpiry.getText().toString().trim();
        if (expiry.isEmpty()) {
            expiry = null;
        }

        String normalized = IngredientUtils.normalize(name);

        if (itemId == -1) {
            db.addPantryItem(new PantryItem(0, name, normalized, quantity, unit, expiry));
            Toast.makeText(this, "Ingredient added", Toast.LENGTH_SHORT).show();
        } else {
            db.updatePantryItem(new PantryItem(itemId, name, normalized, quantity, unit, expiry));
            Toast.makeText(this, "Ingredient updated", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}
