package ru.senya.pixateka.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import ru.senya.pixateka.R;
import ru.senya.pixateka.databinding.ViewInputFieldBinding;


public class InputField extends LinearLayout {

    public ViewInputFieldBinding binding;

    public InputField(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(attrs);
    }

    public InputField(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs);
    }

    public InputField(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        binding = ViewInputFieldBinding.inflate(LayoutInflater.from(getContext()), this, true);
        TypedArray attributes = getContext().getTheme().obtainStyledAttributes(attrs,
                R.styleable.InputField,
                0, 0);
        try {
            String title = attributes.getString(R.styleable.InputField_title);
            binding.title.setText(title);
            int inputType = attributes.getInt(R.styleable.InputField_android_inputType, InputType.TYPE_NULL);
            binding.input.setInputType(inputType);
            binding.input.setHint(attributes.getString(R.styleable.InputField_hint));
            binding.input.setHintTextColor(attributes.getColor(R.styleable.InputField_hint_color, 1));

            try {
                binding.pic.setImageResource(attributes.getResourceId(R.styleable.InputField_src, 1));
            } catch (Exception e) {
                Log.e("MyTag", e.getLocalizedMessage());
            }
        } finally {
            attributes.recycle();
        }


    }

    public String getInputText() {
        return binding.input.getText().toString();
    }

    public void setInputType(int i) {
        binding.input.setInputType(i);
    }
}
