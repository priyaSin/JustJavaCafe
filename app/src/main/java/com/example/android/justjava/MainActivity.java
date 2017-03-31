package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

import static android.R.attr.checkBoxPreferenceStyle;
import static android.R.attr.name;
import static android.R.attr.order;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class MainActivity extends AppCompatActivity {
    int quantity = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setBackgroundDrawableResource(R.drawable.coffee1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /* This method is called when the order button is clicked.
    */
    public void submitOrder(View view) {
        CheckBox whippedCreamCheckBox = (CheckBox) findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();

        CheckBox chocolateCheckBox = (CheckBox) findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = chocolateCheckBox.isChecked();

        EditText editName = (EditText) findViewById(R.id.edit_name);
        String name = editName.getText().toString();

        int price = calculatePrice(hasChocolate, hasWhippedCream);
        String priceMessage = createOrderSummary(price, hasWhippedCream, hasChocolate, name);

        displayMessage(priceMessage);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_order_summary,name));
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }

    }

    /*Calculating the price of the order*/
    private int calculatePrice(boolean hasChocolate, boolean hasWhippedCream) {
        int basePrice = 10;
        if (quantity <= 0) {
            quantity=0;
            basePrice = 0;
            return basePrice;
        } else {
            if (hasChocolate)
                basePrice += 2;
            if (hasWhippedCream)
                basePrice += 1;
            return quantity * basePrice;
        }
    }

    /*Creating a summary for the order and returns the summary /String*/
    private String createOrderSummary(int price, boolean hasWhippedCream, boolean hasChocolate, String name) {
        String priceMessage = "";
//        if (quantity <= 0) {
//            priceMessage = getString(R.string.order_summary_name,name);
//            priceMessage += "\n"+ getString(R.string.quantity) + getString(R.string.zero);
//            priceMessage += "\n"+ getString(R.string.add_whipped_cream,hasWhippedCream);
//            priceMessage += "\n"+ getString(R.string.add_chocolate,hasChocolate);
//            priceMessage += "\n"+ getString(R.string.total,NumberFormat.getCurrencyInstance().format(price));
//            priceMessage += "\n"+ getString(R.string.thank_you);
//        } else {
            priceMessage = getString(R.string.order_summary_name,name);
            priceMessage += "\n"+ getString(R.string.quantity_non_zero,quantity);
            priceMessage += "\n"+ getString(R.string.add_whipped_cream,hasWhippedCream);
            priceMessage += "\n"+ getString(R.string.add_chocolate,hasChocolate);
            priceMessage += "\n"+ getString(R.string.total,NumberFormat.getCurrencyInstance().format(price));
            priceMessage += "\n"+ getString(R.string.thank_you);

        return priceMessage;


    }

    /*This method increment the quantity*/
    public void increment(View view) {
        if (quantity == 100) {
            display(quantity);
            Toast.makeText(this, getText(R.string.toast_for_max), Toast.LENGTH_SHORT).show();
            return;
        } else {
            quantity++;
            display(quantity);
        }

    }

    /*This method decrements the quantity*/
    public void decrement(View view) {
        if (quantity == 0) {
            display(0);
            Toast.makeText(this, getText(R.string.toast_for_min), Toast.LENGTH_SHORT).show();
        } else {
            quantity--;
            display(quantity);
        }

    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(
                R.id.quantity_text_view);
        quantityTextView.setText(getString(R.string.set_quantity,number));
    }

    /**
     * This method displays the given text on the screen.
     */
    private void displayMessage(String message) {
        TextView orderSummaryTextView = (TextView) findViewById(R.id.order_summary_text_view);
        orderSummaryTextView.setText(message);
    }

}